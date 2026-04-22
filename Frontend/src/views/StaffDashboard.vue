<template>
  <div class="staff-dashboard">

    <div class="staff-greeting">
      <div class="greeting-content">
        <h2>Good day! Every cup you make brings a smile to someone!</h2>
      </div>
    </div>

    <h1>Kiosk Management Dashboard</h1>
    
    <div class="order-grid">
      <div v-for="order in cart.orderHistory" :key="order.id" class="staff-order-card">
        
        <div class="order-header">
          <h3>Order #{{ order.id.toString().slice(-4) }}</h3>
          <span class="pickup-time">Pick-up: {{ order.time }}</span>
        </div>
        
        <p class="status-indicator">
          Status: <strong :class="order.status.toLowerCase().replace(' ', '-')">{{ order.status }}</strong>
        </p>
        
        <ul class="item-list">
          <li v-for="item in order.items" :key="item.id + item.size">
            {{ item.name }} ({{ item.size }}) x {{ item.quantity }}
          </li>
        </ul>
        
        <div class="status-actions">
          
          <button 
            @click="updateOrderStatus(order.id, 'Accepted')"
            :disabled="order.status !== 'Pending'"
            class="btn-accept"
          >
            Accept
          </button>
          
          <button 
            @click="updateOrderStatus(order.id, 'In Progress')"
            :disabled="order.status !== 'Accepted'"
          >
            Start
          </button>
          
          <button 
            @click="updateOrderStatus(order.id, 'Ready')"
            :disabled="!['Accepted', 'In Progress'].includes(order.status)"
          >
            Ready
          </button>
          
          <button 
            @click="updateOrderStatus(order.id, 'Collected')"
            :disabled="!['Accepted', 'In Progress', 'Ready'].includes(order.status)"
            class="btn-collected"
          >
            Collected
          </button>
          
          <button 
            @click="updateOrderStatus(order.id, 'Cancelled')"
            :disabled="order.status !== 'Pending'"
            class="btn-cancel"
          >
            Cancel
          </button>
          
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useCartStore } from '../store/cart';

//Access the shared global state
const cart = useCartStore();

//Update the status of a specific order
const updateOrderStatus = (id, newStatus) => {
  const order = cart.orderHistory.find(o => o.id === id);
  if (order) {
    order.status = newStatus;
  }
};
</script>

<style scoped>
/* Dashboard Layout */
.staff-dashboard {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;

}

/* Order Card Styling */
.staff-order-card { 
  background: #fff; 
  padding: 20px; 
  border-left: 6px solid #6F4E37; 
  margin-bottom: 20px; 
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.staff-greeting {
  background: white;
  border-left: 6px solid #6F4E37;
  padding: 20px;
  margin-bottom: 30px;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.greeting-content h2 {
  margin: 0;
  font-size: 1.2rem;
  color: #4a3728;
  font-style: italic; 
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
  margin-bottom: 10px;
}

.pickup-time {
  font-weight: bold;
  color: #555;
}

.item-list {
  list-style-type: none;
  padding: 0;
  margin-bottom: 15px;
}

.item-list li {
  padding: 4px 0;
  color: #333;
}

/* Button Group Styling */
.status-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.status-actions button { 
  padding: 8px 16px; 
  cursor: pointer; 
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: #f8f9fa;
  font-weight: 500;
  transition: all 0.2s ease;
}

/* Specific Button Colors */
.btn-accept { border-color: #28a745; color: #28a745; }
.btn-cancel { border-color: #dc3545; color: #dc3545; }
.btn-collected { border-color: #007bff; color: #007bff; }

.status-actions button:hover:not(:disabled) {
  background-color: #e2e6ea;
}

/* Disabled State */
.status-actions button:disabled {
  background-color: #e9ecef !important;
  color: #adb5bd !important;
  border-color: #dee2e6 !important;
  cursor: not-allowed;
  opacity: 0.8;
}

/* Status Text Colors */
.status-indicator strong.pending { color: #f0ad4e; }
.status-indicator strong.accepted { color: #28a745; }
.status-indicator strong.cancelled { color: #dc3545; }
.status-indicator strong.collected { color: #6c757d; }
</style>