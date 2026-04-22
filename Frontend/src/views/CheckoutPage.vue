<template>
  <div class="checkout-page">
    <h2>Order Summary</h2>
    
    <div v-if="cart.items.length > 0" class="order-details">
      <div v-for="item in cart.items" :key="item.id + item.size" class="order-item">
        <span>{{ item.name }} ({{ item.size }}) x {{ item.quantity }}</span>
        <span>£{{ (item.price * item.quantity).toFixed(2) }}</span>
      </div>
      
      <div class="total-price">
        <strong>Total: £{{ cart.totalPrice.toFixed(2) }}</strong>
      </div>
    </div>

    <div v-else class="empty-cart-message">
      <p>Your cart is empty. Please add some coffee first!</p>
      <button @click="$emit('changeTab', 'menu')" class="go-back-btn">Go to Menu</button>
    </div>

    <div class="checkout-actions">
      <label for="time">Select Pick-up Time:</label>
      <input 
        type="time" 
        id="time" 
        v-model="selectedTime" 
        class="time-input" 
        :disabled="cart.items.length === 0"
      />
      
      <button 
        @click="handleConfirm" 
        class="confirm-btn" 
        :disabled="cart.items.length === 0"
      >
        Confirm Order
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useCartStore } from '../store/cart';

//Access the shared cart state
const cart = useCartStore();

//Local state for the selected time
const selectedTime = ref('');

//Define the event to tell App.vue to switch tabs
const emit = defineEmits(['changeTab']);

/**
 * Validates and finalizes the order.
 * Ensures the cart is not empty and a pick-up time is selected.
 */
const handleConfirm = () => {
  //Check for empty cart
  if (cart.items.length === 0) {
    alert('Your cart is empty. Please add items before placing an order.');
    return;
  }

  //Check for missing pick-up time
  if (!selectedTime.value) {
    alert('Please select a pick-up time!');
    return;
  }
  
  //Update store with time and submit order
  cart.selectedTime = selectedTime.value;
  cart.submitOrder(); 
  
  //Feedback and navigation
  alert('Order placed successfully!');
  emit('changeTab', 'orders');
};
</script>

<style scoped>
.checkout-page { 
  padding: 20px; 
  max-width: 500px; 
  margin: 0 auto; 
}

.order-details { 
  background: #f9f9f9; 
  padding: 15px; 
  border-radius: 8px; 
  margin-bottom: 20px; 
}

.empty-cart-message {
  text-align: center;
  padding: 40px 20px;
  background: #fff5f5;
  border-radius: 8px;
  margin-bottom: 20px;
}

.go-back-btn {
  margin-top: 10px;
  background: none;
  border: 1px solid #6F4E37;
  color: #6F4E37;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.order-item { 
  display: flex; 
  justify-content: space-between; 
  padding: 5px 0; 
}

.total-price { 
  border-top: 2px solid #ddd; 
  margin-top: 10px; 
  padding-top: 10px; 
  text-align: right; 
}

.checkout-actions { 
  display: flex; 
  flex-direction: column; 
  gap: 10px; 
}

.time-input { 
  padding: 10px; 
  font-size: 1rem; 
  border: 1px solid #ccc; 
  border-radius: 4px; 
}

.confirm-btn { 
  background: #6F4E37; 
  color: white; 
  border: none; 
  padding: 12px; 
  border-radius: 6px; 
  cursor: pointer; 
  font-size: 1.1rem; 
}

.confirm-btn:hover:not(:disabled) { 
  background: #503928; 
}

/* UI for disabled state */
.confirm-btn:disabled, .time-input:disabled {
  background-color: #ccc;
  cursor: not-allowed;
  opacity: 0.6;
}
</style>