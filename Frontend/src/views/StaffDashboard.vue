<template>
  <div class="staff-dashboard">
    <h1>Kiosk Management Dashboard</h1>
    
    <div class="order-grid">
      <div v-for="order in cart.orderHistory" :key="order.id" class="staff-order-card">
        <h3>Order #{{ order.id.toString().slice(-4) }} - {{ order.time }}</h3>
        <p>Status: <strong>{{ order.status }}</strong></p>
        <ul>
          <li v-for="item in order.items" :key="item.id + item.size">
            {{ item.name }} ({{ item.size }}) x {{ item.quantity }}
          </li>
        </ul>
        
        <div class="status-actions">
          <button @click="updateOrderStatus(order.id, 'Accepted')">Accept</button>
          <button @click="updateOrderStatus(order.id, 'In Progress')">Start</button>
          <button @click="updateOrderStatus(order.id, 'Ready')">Ready</button>
          <button @click="updateOrderStatus(order.id, 'Collected')">Collected</button>
          <button class="btn-cancel" @click="updateOrderStatus(order.id, 'Cancelled')">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useCartStore } from '../store/cart';

const cart = useCartStore();

// Logic to update status directly in the store
const updateOrderStatus = (id, newStatus) => {
  const order = cart.orderHistory.find(o => o.id === id);
  if (order) {
    order.status = newStatus;
  }
};
</script>

<style scoped>
.staff-order-card { 
  background: #f9f9f9; 
  padding: 20px; 
  border-left: 5px solid #6F4E37; 
  margin: 10px; 
  border-radius: 8px;
}
.status-actions button { margin-right: 5px; padding: 5px 12px; cursor: pointer; }
.btn-cancel { color: white; background-color: #d9534f; border: none; }
</style>