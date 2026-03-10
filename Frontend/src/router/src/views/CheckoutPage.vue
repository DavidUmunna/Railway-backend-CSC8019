<template>
  <div class="checkout-page">
    <h2>Order Summary</h2>
    
    <div class="order-details">
      <div v-for="item in cart.items" :key="item.id + item.size" class="order-item">
        <span>{{ item.name }} ({{ item.size }}) x {{ item.quantity }}</span>
        <span>£{{ (item.price * item.quantity).toFixed(2) }}</span>
      </div>
      
      <div class="total-price">
        <strong>Total: £{{ cart.totalPrice.toFixed(2) }}</strong>
      </div>
    </div>

    <div class="checkout-actions">
      <label for="time">Select Pick-up Time:</label>
      <input type="time" id="time" v-model="selectedTime" class="time-input" />
      
      <button @click="handleConfirm" class="confirm-btn">Confirm Order</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useCartStore } from '../store/cart';

// Access the shared cart state
const cart = useCartStore();

// Local state for the selected time
const selectedTime = ref('');

// Define the event to tell App.vue to switch tabs
const emit = defineEmits(['changeTab']);

/**
 * Handles order confirmation.
 * This will move the cart items to orderHistory and clear the cart.
 */
const handleConfirm = () => {
  if (!selectedTime.value) {
    alert('Please select a pick-up time!');
    return;
  }
  
  // 1. Update the store with the selected time
  cart.selectedTime = selectedTime.value;
  
  // 2. Archive the order and clear the cart
  cart.submitOrder(); 
  
  // 3. Optional: Provide feedback to the user
  alert('Order placed successfully!');
  
  // 4. Redirect to the Orders page to show the history
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

.confirm-btn:hover { 
  background: #503928; 
}
</style>