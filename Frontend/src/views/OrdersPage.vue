<template>
  <div class="page-container">
    <h2 class="title">
      <ShoppingBag size="24" class="orders-icon" />
      My Orders
    </h2>

    <div v-if="isLoading" class="loading-wrap">
      <div class="state-card loading-state">
        <Loader2 size="20" class="spin" />
        <span>Loading orders...</span>
      </div>

      <div class="skeleton-list" aria-hidden="true">
        <div v-for="n in 4" :key="`order-skeleton-${n}`" class="order-card skeleton-order-card">
          <div class="order-header">
            <div class="skeleton skeleton-id"></div>
            <div class="skeleton skeleton-status"></div>
          </div>
          <div class="order-body">
            <div class="skeleton skeleton-line"></div>
            <div class="skeleton skeleton-line short"></div>
          </div>
        </div>
      </div>
    </div>

    <div v-else-if="error" class="state-card error-state">
      <AlertCircle size="20" />
      <span>{{ error }}</span>
    </div>
    
    <div v-else-if="history.length === 0" class="empty">
      <PackageOpen size="40" />
      No orders placed yet.
    </div>
    
    <div v-else v-for="order in history" :key="order.id" class="order-card">
      <div class="order-header">
        <span class="order-id">#{{ order.id }}</span>
        <span :class="['status-tag', order.status.toLowerCase()]">
          <CheckCircle2 v-if="order.status === 'READY'" size="14" />
          <Clock v-else size="14" />
          {{ order.status }}
        </span>
      </div>
      <div class="order-body">
        <p>{{ order.summary }}</p>
        <p class="arrival">
          <Clock size="14" />
          Est. Arrival: {{ order.arrivalTime }}
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps(['history', 'isLoading', 'error'])
</script>

<style scoped>
.page-container { padding: 20px 0; }
.title { 
  text-align: center; 
  margin-bottom: 20px; 
  color: #3e2723;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.empty {
  text-align: center;
  color: #a1887f;
  padding: 40px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.state-card {
  background: white;
  border-radius: 12px;
  padding: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.loading-wrap {
  display: grid;
  gap: 12px;
}

.skeleton-list {
  display: grid;
  gap: 12px;
}

.skeleton-order-card {
  pointer-events: none;
}

.skeleton {
  border-radius: 10px;
  background: linear-gradient(90deg, #efe7e1 25%, #f7f2ee 50%, #efe7e1 75%);
  background-size: 200% 100%;
  animation: shimmer 1.1s linear infinite;
}

.skeleton-id {
  width: 70px;
  height: 16px;
}

.skeleton-status {
  width: 90px;
  height: 24px;
  border-radius: 20px;
}

.skeleton-line {
  width: 80%;
  height: 14px;
  margin-bottom: 8px;
}

.skeleton-line.short {
  width: 55%;
  margin-bottom: 0;
}

.loading-state {
  color: #6d4c41;
}

.error-state {
  color: #b23b3b;
}

.spin {
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

@keyframes shimmer {
  from { background-position: 200% 0; }
  to { background-position: -200% 0; }
}

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
  padding: 6px 12px;
  border-radius: 20px;
  text-transform: uppercase;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 6px;
}

.arrival { 
  font-size: 0.85rem; 
  color: #8d6e63; 
  margin-top: 5px;
}

/* Status colors as per Project Brief */
.accepted { background: #fff3e0; color: #ef6c00; }
.ready { background: #e8f5e9; color: #2e7d32; }

@media (max-width: 768px) {
  .order-card {
    padding: 16px;
  }

  .order-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .status-tag {
    width: fit-content;
  }
}

@media (max-width: 480px) {
  .title {
    font-size: 1.2rem;
  }

  .order-id {
    font-size: 1rem;
  }
}
</style>