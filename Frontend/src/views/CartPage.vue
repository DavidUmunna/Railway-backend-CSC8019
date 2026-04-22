<template>
  <div class="cart-page">
    <h2>Your Cart</h2>
    
    <div v-for="item in cart.items" :key="item.id + item.size" class="cart-item">
      <div>
        <h3>{{ item.name }} ({{ item.size }})</h3>
        <p>Price: £{{ (item.price * item.quantity).toFixed(2) }}</p>
      </div>
      
      <div class="controls">
        <button @click="cart.decrement(item)">-</button>
        <span class="qty">{{ item.quantity }}</span>
        <button @click="cart.increment(item)">+</button>
        <button @click="cart.removeItem(item)" class="remove-btn">Remove</button>
      </div>
    </div>

    <div class="cart-summary">
      <strong>Total: £{{ cart.totalPrice.toFixed(2) }}</strong>
      <p class="notice">Next: Select arrival time for pick-up</p>
      
      <button class="checkout-btn" @click="$emit('changeTab', 'checkout')">
        Proceed to Checkout
      </button>
    </div>
  </div>
</template>


<script setup>
//Shopping cart page component
import { useCartStore } from '../store/cart';

//Access the centralized store for cart state and actions
const cart = useCartStore();

//Event emitted to App.vue to switch navigation tabs
defineEmits(['changeTab']);
</script>

<style scoped>
.cart-item { 
  display: flex; 
  justify-content: space-between; 
  align-items: center; 
  padding: 15px; 
  border-bottom: 1px solid #eee; 
}

.controls { display: flex; align-items: center; gap: 10px; }
.qty { font-weight: bold; width: 20px; text-align: center; }

.remove-btn {
  background-color: transparent; 
  color: #777; 
  text-decoration: underline;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: color 0.2s;
}

.remove-btn:hover {
  color: #d9534f; 
}

.cart-summary { margin-top: 20px; padding: 15px; border-top: 2px solid #333; }

.checkout-btn { 
  width: 100%; 
  padding: 12px; 
  background-color: #6F4E37; 
  color: white; 
  border: none; 
  border-radius: 5px; 
  cursor: pointer;
  font-size: 16px;
}

.checkout-btn:hover { background-color: #503928; }
</style>