import { defineStore } from 'pinia';

/**
 * Cart Store for Whistlestop Coffee Hut
 * Manages coffee selection, sizes (Regular/Large), pick-up time, and order history.
 */
export const useCartStore = defineStore('cart', {
  state: () => ({
    items: [],            // Format: { id, name, size, price, quantity }
    selectedTime: '',     // Pick-up time selected by the customer
    orderHistory: [],     // Archive for completed orders
  }),
  
  getters: {
    // Calculates the total price of all items currently in the cart
    totalPrice: (state) => state.items.reduce((sum, item) => sum + (item.price * item.quantity), 0),
    
    // Calculates total count of items in the cart
    totalCount: (state) => state.items.reduce((sum, item) => sum + item.quantity, 0),
  },

  actions: {
    /**
     * Adds a coffee item to the cart or increments quantity if it exists.
     */
    addToCart(product, size) {
      const price = size === 'Large' ? product.largePrice : product.regularPrice;
      const existing = this.items.find(i => i.id === product.id && i.size === size);
      
      if (existing) {
        existing.quantity++;
      } else {
        this.items.push({ 
          id: product.id, 
          name: product.name, 
          size, 
          price, 
          quantity: 1 
        });
      }
    },

    /**
     * Increments the quantity of a specific cart item.
     */
    increment(item) {
      item.quantity++;
    },

    /**
     * Decrements the quantity of a specific cart item. 
     * Removes the item if quantity reaches zero.
     */
    decrement(item) {
      if (item.quantity > 1) {
        item.quantity--;
      } else {
        this.removeItem(item);
      }
    },

    /**
     * Removes an item from the cart entirely.
     */
    removeItem(item) {
      const index = this.items.findIndex(i => i.id === item.id && i.size === item.size);
      if (index !== -1) {
        this.items.splice(index, 1);
      }
    },

    /**
     * Finalizes the order: 
     * 1. Saves current cart to orderHistory.
     * 2. Clears the current cart for the next order.
     */
    submitOrder() {
      const newOrder = {
        id: Date.now(), 
        items: [...this.items], 
        total: this.totalPrice,
        time: this.selectedTime,
        status: 'Pending' // Initial status for staff dashboard
      };
      
      this.orderHistory.push(newOrder);
      this.clearCart();
    },

    /**
     * Updates the status of an order to 'Ready' for staff tracking.
     */
    markAsReady(orderId) {
      const order = this.orderHistory.find(o => o.id === orderId);
      if (order) {
        order.status = 'Ready'; 
      }
    },

    /**
     * Clears cart items and resets selection state.
     */
    clearCart() {
      this.items = [];
      this.selectedTime = '';
    }
  }
});