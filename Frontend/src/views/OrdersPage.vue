<template>
  <div class="orders-container">
    <h2>Active Orders</h2>
    
    <div v-if="cart.orderHistory.length === 0" class="empty-state">
      <p>No orders yet. Start ordering coffee!</p>
    </div>
    
    <div v-for="order in cart.orderHistory" :key="order.id" class="order-card">
      <div class="header">
        <strong>Order #{{ order.id.toString().slice(-4) }}</strong>
        <span :class="'status-' + order.status.toLowerCase()">{{ order.status }}</span>
      </div>
      
      <div class="details">
        <p>Pick-up Time: {{ order.time }}</p>
        <p>Total Amount: £{{ order.total.toFixed(2) }}</p>
        
        <ul class="item-list">
          <li v-for="item in order.items" :key="item.id + item.size">
            {{ item.name }} ({{ item.size }}) x {{ item.quantity }}
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useCartStore } from '../store/cart';

//Access the centralized cart store
const cart = useCartStore();
</script>

<style scoped>
.orders-container { padding: 20px; }
.order-card { 
  padding: 15px; 
  margin-bottom: 15px; 
  border: 1px solid #ddd; 
  border-radius: 8px; 
  background-color: #fff;
}
.header { 
  display: flex; 
  justify-content: space-between; 
  margin-bottom: 10px; 
}
.details { font-size: 0.9rem; color: #555; }
.item-list { margin: 5px 0 0 0; padding-left: 20px; }
.empty-state { text-align: center; margin-top: 50px; color: #888; }
.status-confirmed { color: green; font-weight: bold; }
.status-in_progress { color: orange; font-weight: bold; }
.status-cancelled { color: red; font-weight: bold; }
</style>