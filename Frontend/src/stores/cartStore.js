import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import orderService from '../services/orderService.js';

export const useCartStore = defineStore('cart', () => {
  // State
  const cart = ref(JSON.parse(localStorage.getItem('cart')) || []);
  const orderHistory = ref([]);
  const isLoading = ref(false);
  const cartError = ref(null);
  const showCart = ref(false);

  // Getters
  const cartCount = computed(() => cart.value.length);
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

  const normalizeSizeForApi = (size) => {
    if (!size) {
      return 'REGULAR';
    }
    return String(size).trim().toUpperCase();
  };

  // Actions
  const addToCart = (item, size = 'Regular') => {
    const price = size === 'Regular' ? item.regPrice : (item.largePrice || item.regPrice);
    
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
        quantity: 1
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

  const checkout = async () => {
    if (cart.value.length === 0) {
      cartError.value = 'Cart is empty';
      return;
    }

    isLoading.value = true;
    cartError.value = null;

    try {
      const orderData = {
        items: cart.value.map(item => ({
          menuItemId: item.id,
          quantity: item.quantity || 1,
          size: normalizeSizeForApi(item.size)
        })),
        totalAmount: cartTotal.value
      };

      const newOrder = await orderService.createOrder(orderData);

      // Add to order history
      orderHistory.value.unshift({
        id: newOrder.id?.toString() || Date.now().toString(),
        summary: `${cart.value.length} items - $${formattedTotal.value}`,
        arrivalTime: 'processing',
        status: newOrder.status || 'ACCEPTED',
        items: cart.value,
        totalAmount: cartTotal.value,
        createdAt: new Date().toISOString()
      });

      // Clear cart
      clearCart();
      return newOrder;
    } catch (error) {
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