<template>
  <div class="checkout-page">
    <h2>Order Summary</h2>
    
    <div v-if="cart.length > 0" class="order-details">
      <div v-for="(item, index) in cart" :key="index" class="order-item">
        <span>{{ item.name }} ({{ item.size }})</span>
        <span>£{{ item.price.toFixed(2) }}</span>
      </div>
      
      <div class="total-price">
        <strong>Total: £{{ cartTotal.toFixed(2) }}</strong>
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
        :disabled="cart.length === 0"
      />
      
      <button 
        @click="handleConfirm" 
        class="confirm-btn" 
        :disabled="cart.length === 0"
      >
        Confirm Order
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

defineProps({
  cart: {
    type: Array,
    default: () => []
  }
});

const emit = defineEmits(['changeTab']);

const selectedTime = ref('');
const cartTotal = computed(() => {
  return (Array.isArray(cart) ? cart : []).reduce((sum, item) => sum + (item.price || 0), 0);
});

const handleConfirm = () => {
  // 1. Check for empty cart
  if (cart.length === 0) {
    alert('Your cart is empty. Please add items before placing an order.');
    return;
  }

  // 2. Check for missing pick-up time
  if (!selectedTime.value) {
    alert('Please select a pick-up time!');
    return;
  }
  
  // 3. Feedback and navigation
  alert(`Order confirmed for pickup at ${selectedTime.value}`);
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
  font-weight: 600;
}

.go-back-btn:hover {
  background: #6F4E37;
  color: white;
}

.order-item { 
  display: flex; 
  justify-content: space-between; 
  padding: 10px 0; 
  border-bottom: 1px solid #eee;
}

.total-price { 
  border-top: 2px solid #ddd; 
  margin-top: 10px; 
  padding-top: 10px; 
  text-align: right;
  font-size: 1.2rem;
}

.checkout-actions { 
  display: flex; 
  flex-direction: column; 
  gap: 15px; 
  margin-top: 20px;
}

.checkout-actions label {
  font-weight: 600;
  color: #3e2723;
}

.time-input { 
  padding: 12px; 
  font-size: 1rem; 
  border: 2px solid #ddd; 
  border-radius: 8px;
  transition: border-color 0.2s;
}

.time-input:focus {
  outline: none;
  border-color: #6F4E37;
}

.confirm-btn { 
  background: #6F4E37; 
  color: white; 
  border: none; 
  padding: 14px; 
  border-radius: 8px; 
  cursor: pointer; 
  font-size: 1.1rem;
  font-weight: 600;
  transition: background-color 0.2s;
}

.confirm-btn:hover:not(:disabled) { 
  background: #503928; 
}

.confirm-btn:disabled, .time-input:disabled {
  background-color: #ccc;
  cursor: not-allowed;
  opacity: 0.6;
}
</style>
