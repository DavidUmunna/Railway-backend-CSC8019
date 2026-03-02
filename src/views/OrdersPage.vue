<template>
  <div class="page-container">
    <h2 class="title">My Orders</h2>
    
    <div v-if="history.length === 0" class="empty">No orders placed yet.</div>
    
    <div v-for="order in history" :key="order.id" class="order-card">
      <div class="order-header">
        <span class="order-id">#{{ order.id }}</span>
        <span :class="['status-tag', order.status.toLowerCase()]">
          {{ order.status }}
        </span>
      </div>
      <div class="order-body">
        <p>{{ order.summary }}</p>
        <p class="arrival">Est. Arrival: {{ order.arrivalTime }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps(['history']);
</script>

<style scoped>
.page-container { padding: 20px 0; }
.title { text-align: center; margin-bottom: 20px; color: #3e2723; }

.order-card {
  background: white;
  margin-bottom: 15px;
  padding: 20px;
  border-radius: 12px;
  border-left: 6px solid #ffab40;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.order-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  border-bottom: 1px solid #eee;
  padding-bottom: 8px;
}

.order-id { font-weight: 900; font-size: 1.1rem; }

.status-tag {
  font-size: 0.75rem;
  padding: 4px 10px;
  border-radius: 20px;
  text-transform: uppercase;
  font-weight: bold;
}

/* Status colors as per Project Brief */
.accepted { background: #fff3e0; color: #ef6c00; }
.ready { background: #e8f5e9; color: #2e7d32; }
.arrival { font-size: 0.85rem; color: #8d6e63; margin-top: 5px; }
</style>