import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import orderService from '../services/orderService.js';
import customerService from '../services/customerService.js';
import { useStationStore } from './stationStore.js';

export const useCartStore = defineStore('cart', () => {
  // State
  const cart = ref(JSON.parse(localStorage.getItem('cart')) || []);
  const orderHistory = ref([]);
  const isLoading = ref(false);
  const cartError = ref(null);
  const showCart = ref(false);

  // Getters
  const cartCount = computed(() => {
    return cart.value.reduce((sum, item) => sum + Number(item.quantity || 1), 0);
  });
  const cartTotal = computed(() => {
    return cart.value.reduce((sum, item) => {
      const price = item.price || 0;
      const quantity = item.quantity || 1;
      return sum + (price * quantity);
    }, 0);
  });
  const formattedTotal = computed(() => {
    return cartTotal.value.toFixed(2);
  });

  const parseHoursRange = (hoursRange) => {
    if (!hoursRange || !hoursRange.includes('-')) {
      return null;
    }

    const [openText, closeText] = hoursRange.split('-').map((part) => part.trim());
    if (!openText || !closeText) {
      return null;
    }

    const [openHour, openMinute] = openText.split(':').map(Number);
    const [closeHour, closeMinute] = closeText.split(':').map(Number);

    if ([openHour, openMinute, closeHour, closeMinute].some((value) => Number.isNaN(value))) {
      return null;
    }

    return {
      openMinutes: openHour * 60 + openMinute,
      closeMinutes: closeHour * 60 + closeMinute,
    };
  };

  const isPickupWithinStationHours = (pickupTime, station) => {
    if (!pickupTime || !station) {
      return { valid: false, reason: 'Station information is unavailable. Please return to menu and try again.' };
    }

    const now = new Date();
    const day = now.getDay();

    if (day === 0 && station.closedOnSunday) {
      return { valid: false, reason: `Station ${station.name} is closed today.` };
    }

    const hoursRange = day === 6 ? station.saturdayOpeningHours : station.weekdayOpeningHours;
    const parsed = parseHoursRange(hoursRange);
    if (!parsed) {
      return { valid: false, reason: `Opening hours are unavailable for station ${station.name}.` };
    }

    const [pickupHour, pickupMinute] = String(pickupTime).split(':').map(Number);
    if ([pickupHour, pickupMinute].some((value) => Number.isNaN(value))) {
      return { valid: false, reason: 'Please select a valid pickup time.' };
    }

    const pickupMinutes = pickupHour * 60 + pickupMinute;
    const nowMinutes = now.getHours() * 60 + now.getMinutes();

    if (pickupMinutes < nowMinutes) {
      return { valid: false, reason: 'Pickup time cannot be earlier than the current time.' };
    }

    if (pickupMinutes < parsed.openMinutes || pickupMinutes > parsed.closeMinutes) {
      return { valid: false, reason: `Pickup time is outside station hours. Valid hours today: ${hoursRange}.` };
    }

    return { valid: true, reason: '' };
  };

  const normalizeSizeForApi = (size) => {
    if (!size) {
      return 'REGULAR';
    }
    return String(size).trim().toUpperCase();
  };

  // Actions
  const addToCart = (item, size = 'Regular') => {
    const price = size === 'Regular' ? item.regPrice : (item.largePrice || item.regPrice);
    const menuItemTypeId =
      size === 'Regular'
        ? (item.regTypeId || item.largeTypeId)
        : (item.largeTypeId || item.regTypeId);
    
    // Check if item already in cart
    const existingItem = cart.value.find(
      cartItem => cartItem.id === item.id && cartItem.size === size
    );

    if (existingItem) {
      existingItem.quantity = (existingItem.quantity || 1) + 1;
    } else {
      cart.value.push({
        id: item.id,
        name: item.name,
        size: size,
        price: price,
        quantity: 1,
        menuItemTypeId,
      });
    }

    // Persist to localStorage
    localStorage.setItem('cart', JSON.stringify(cart.value));
  };

  const removeFromCart = (index) => {
    cart.value.splice(index, 1);
    localStorage.setItem('cart', JSON.stringify(cart.value));
  };

  const updateCartItemQuantity = (index, quantity) => {
    if (quantity <= 0) {
      removeFromCart(index);
    } else {
      cart.value[index].quantity = quantity;
      localStorage.setItem('cart', JSON.stringify(cart.value));
    }
  };

  const clearCart = () => {
    cart.value = [];
    localStorage.removeItem('cart');
  };

  const checkout = async (checkoutDetails) => {
    const stationStore = useStationStore();

    if (cart.value.length === 0) {
      const err = new Error('Your cart is empty.');
      cartError.value = err.message;
      throw err;
    }

    if (!checkoutDetails?.pickupTime) {
      const err = new Error('Please select a pickup time.');
      cartError.value = err.message;
      throw err;
    }

    if (!checkoutDetails?.stationId) {
      const err = new Error('Station information is unavailable. Please return to menu and try again.');
      cartError.value = err.message;
      throw err;
    }

    if (!checkoutDetails?.customerFirstName || !checkoutDetails?.customerLastName || !checkoutDetails?.customerPhoneNumber) {
      const err = new Error('Please provide your first name, last name, and phone number.');
      cartError.value = err.message;
      throw err;
    }

    const station = stationStore.currentStation
      || stationStore.stations.find((item) => Number(item.id) === Number(checkoutDetails.stationId));
    const timeValidation = isPickupWithinStationHours(checkoutDetails.pickupTime, station);
    if (!timeValidation.valid) {
      const err = new Error(timeValidation.reason);
      cartError.value = err.message;
      throw err;
    }

    const missingTypeId = cart.value.find(item => !item.menuItemTypeId);
    if (missingTypeId) {
      clearCart();
      const err = new Error('Your cart contained outdated items and has been cleared. Please re-add items from the menu.');
      cartError.value = err.message;
      throw err;
    }

    isLoading.value = true;
    cartError.value = null;
    let createdCustomerId = null;

    try {
      const customer = await customerService.createCustomer({
        customerId: null,
        customerFirstName: checkoutDetails.customerFirstName,
        customerLastName: checkoutDetails.customerLastName,
        customerPhoneNumber: checkoutDetails.customerPhoneNumber,
      });
      createdCustomerId = customer.customerId;

      const orderData = {
        customerId: customer.customerId,
        stationId: Number(checkoutDetails.stationId),
        pickupTime: checkoutDetails.pickupTime,
        orderItems: cart.value.map(item => ({
          menuItemTypeId: item.menuItemTypeId,
          quantity: item.quantity || 1,
        })),
      };

      const newOrder = await orderService.createOrder(orderData);

      // Add to order history
      orderHistory.value.unshift({
        id: newOrder.orderId?.toString() || Date.now().toString(),
        summary: `${cart.value.length} items - $${formattedTotal.value}`,
        arrivalTime: newOrder.pickupTime || '--:--',
        status: newOrder.orderStatus || 'ACCEPTED',
        items: cart.value,
        totalAmount: Number(newOrder.totalAmount || cartTotal.value),
        orderDate: newOrder.orderDate || null,
        pickupTime: newOrder.pickupTime || null,
        customerId: newOrder.customerId || customer.customerId,
        stationId: newOrder.stationId || Number(checkoutDetails.stationId),
        createdAt: new Date().toISOString(),
      });

      // Clear cart
      clearCart();
      return newOrder;
    } catch (error) {
      const serverMessage = String(error.serverMessage || '').toLowerCase();
      const userMessage = String(error.message || '').toLowerCase();
      const isPickupTimeError = serverMessage.includes('pickup time') || userMessage.includes('pickup time');

      if (createdCustomerId && isPickupTimeError) {
        try {
          await customerService.deleteCustomer(createdCustomerId);
        } catch (cleanupError) {
          console.error('Failed to clean up customer after invalid pickup time:', cleanupError);
        }
      }

      cartError.value = error.message || 'Checkout failed';
      throw error;
    } finally {
      isLoading.value = false;
    }
  };

  const loadOrderHistory = async () => {
    isLoading.value = true;
    try {
      orderHistory.value = await orderService.getAllOrders();
    } catch (error) {
      console.error('Failed to load order history:', error);
      orderHistory.value = [];
      cartError.value = error.message || 'Failed to load order history';
    } finally {
      isLoading.value = false;
    }
  };

  const updateOrderStatus = async (orderId, status) => {
    try {
      await orderService.updateOrderStatus(orderId, status);
      const order = orderHistory.value.find(o => o.id === orderId.toString());
      if (order) {
        order.status = status;
      }
    } catch (error) {
      console.error('Failed to update order status:', error);
      throw error;
    }
  };

  const clearError = () => {
    cartError.value = null;
  };

  return {
    // State
    cart,
    orderHistory,
    isLoading,
    cartError,
    showCart,
    // Getters
    cartCount,
    cartTotal,
    formattedTotal,
    // Actions
    addToCart,
    removeFromCart,
    updateCartItemQuantity,
    clearCart,
    checkout,
    loadOrderHistory,
    updateOrderStatus,
    clearError
  };
});