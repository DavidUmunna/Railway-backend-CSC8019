
<template>
  <section class="staff-dashboard">
    <header class="dashboard-header">
      <div>
        <p class="eyebrow">Staff Console</p>
        <h2>Order Operations</h2>
        <p class="subtitle">
          {{ staffUser?.name ? `Signed in as ${staffUser.name}` : 'Track and process live orders.' }}
        </p>
      </div>
      <button class="logout-btn" @click="$emit('logout')">
        <LogOut size="16" />
        <span>Logout</span>
      </button>
    </header>

    <div class="toolbar">
      <button
        v-for="status in statusFilters"
        :key="status"
        :class="['filter-btn', { active: activeFilter === status }]"
        @click="activeFilter = status"
      >
        {{ prettyStatus(status) }}
      </button>
      <button class="refresh-btn" @click="loadOrders" :disabled="isLoading">
        <Loader2 v-if="isLoading" size="16" class="spin" />
        <RefreshCw v-else size="16" />
        <span>{{ isLoading ? 'Refreshing...' : 'Refresh' }}</span>
      </button>
    </div>

    <div v-if="error" class="error-banner">
      <AlertCircle size="16" />
      <span>{{ error }}</span>
    </div>

    <div v-if="isLoading && !filteredOrders.length" class="state-wrap">
      <Loader2 size="24" class="spin" />
      <p>Loading orders...</p>
    </div>

    <div v-else-if="!filteredOrders.length" class="state-wrap empty">
      <Package size="24" />
      <p>No {{ prettyStatus(activeFilter).toLowerCase() }} orders at the moment.</p>
    </div>

    <div v-else class="orders-grid">
      <article
        v-for="order in filteredOrders"
        :key="order.id"
        class="order-card"
        role="button"
        tabindex="0"
        @click="toggleOrder(order.id)"
        @keydown.enter.prevent="toggleOrder(order.id)"
      >
        <header class="card-top">
          <h3>Order #{{ order.id }}</h3>
          <span :class="['status-chip', chipClass(order.status)]">{{ prettyStatus(order.status) }}</span>
        </header>

        <p class="summary"><span>Total : </span>{{ order.totalAmount ? `£${order.totalAmount.toFixed(2)}` : 'Order in progress' }}</p>
        <p class="meta">Pickup: {{ order.pickupTime || order.arrivalTime || '--:--' }}</p>

        <div class="actions">
          <template v-if="order.status !== 'COMPLETED'">
            <button
              v-if="order.status === 'ACCEPTED'"
              class="action-btn in-progress"
              @click.stop="changeStatus(order.id, staffUser.id, 'IN_PROGRESS')"
            >
              Start
            </button>

            <button
              v-if="order.status === 'COLLECTED'"
              class="action-btn revert"
              @click.stop="changeStatus(order.id, staffUser.id, 'IN_PROGRESS')"
            >
              Revert
            </button>

            <button
              v-if="order.status === 'IN_PROGRESS'"
              class="action-btn ready"
              @click.stop="changeStatus(order.id, staffUser.id, 'COLLECTED')"
            >
              Mark Complete
            </button>

            <button
            v-if="order.status === 'COLLECTED'"
              class="action-btn ready"
              @click.stop="changeStatus(order.id, staffUser.id, 'COMPLETED')"
            >

            </button>

            <button
              v-if="order.status !== 'CANCELLED' && order.status !== 'COLLECTED' && order.status !== 'COMPLETED'"
              class="action-btn cancel"
              @click.stop="changeStatus(order.id, staffUser.id, 'CANCELLED')"
            >
              Cancel
            </button>
          </template>
        
        </div>

        <div v-if="isExpanded(order.id)" class="order-details">
          <h4>Items</h4>
          <ul v-if="orderItemsMap[order.id]?.length" class="items-list">
            <li v-for="(item, index) in orderItemsMap[order.id]" :key="`${order.id}-${index}`">
              <span>{{ item.itemName || 'Item' }} ({{ item.size || 'Regular' }})</span>
              <div class="item-desc" v-if="item.description">{{ item.description }}</div>
              <span>Unit: £{{ item.unitPrice.toFixed(2) }}</span>
              <strong>x{{ item.quantity || 1 }}</strong>
            </li>
          </ul>
          <p v-else class="no-items">Item details are unavailable for this order.</p>
        </div>
      </article>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import orderService from '../services/orderService.js';



const props = defineProps({
  staffUser: {
    type: Object,
    default: null,
  },
});

defineEmits(['logout']);

const statusFilters = ['ACCEPTED', 'IN_PROGRESS', 'COLLECTED','COMPLETED', 'CANCELLED'];
const activeFilter = ref('ACCEPTED');

const orders = ref([]);
const isLoading = ref(false);
const error = ref('');


// Use same structure as OrdersPage
const filteredOrders = computed(() => {
  return orders.value.filter((order) => order.status === activeFilter.value);
});

// Expand/collapse logic and order items map
const expandedOrders = ref(new Set());
const orderItemsMap = ref({});

const toggleOrder = async (orderId) => {
  const key = String(orderId);
  if (expandedOrders.value.has(key)) {
    expandedOrders.value.delete(key);
  } else {
    expandedOrders.value.add(key);
    // Always fetch items on expand
    try {
      const items = await orderService.getOrderItems(orderId);
      orderItemsMap.value[key] = Array.isArray(items.data) ? items.data : items;
    } catch (err) {
      orderItemsMap.value[key] = [];
    }
  }
  expandedOrders.value = new Set(expandedOrders.value);
};

const loadOrders = async () => {
  isLoading.value = true;
  error.value = '';
  try {
    orders.value = await orderService.getAllOrders();
  } catch (err) {
    error.value = err.message || 'Unable to load orders.';
  } finally {
    isLoading.value = false;
  }
};

const isExpanded = (orderId) => expandedOrders.value.has(String(orderId));

const prettyStatus = (status = '') => String(status).replace('_', ' ');

const chipClass = (status = '') => {
  if (status === 'COMPLETED') return 'done';
  if (status === 'IN_PROGRESS') return 'progress';
  if (status === 'CANCELLED') return 'cancelled';
  if (status === 'COLLECTED') return 'collected';
  return 'accepted';
};

onMounted(loadOrders);
</script>

<style scoped>
.staff-dashboard {
  max-width: 980px;
  margin: 0 auto;
  padding: 20px 16px 28px;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 14px;
  margin-bottom: 16px;
}

.eyebrow {
  margin: 0;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  font-size: 0.75rem;
  color: #967f76;
}

h2 {
  margin: 4px 0;
  color: #3e2723;
}

.subtitle {
  margin: 0;
  color: #6e5a50;
}

.logout-btn {
  border: 1px solid #e7d9cf;
  background: #fff;
  color: #5d4037;
  border-radius: 10px;
  padding: 8px 10px;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
}

.toolbar {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 14px;
}

.filter-btn,
.refresh-btn {
  border: 1px solid #e6d6cd;
  border-radius: 999px;
  background: #fff;
  color: #5d4037;
  padding: 7px 12px;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.filter-btn.active {
  border-color: #3e2723;
  color: #3e2723;
  font-weight: 700;
}

.error-banner {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: #fff1f1;
  color: #b03c3c;
  border: 1px solid #f0c9c9;
  border-radius: 8px;
  padding: 8px 10px;
  margin-bottom: 12px;
}

.orders-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 12px;
}

.order-card {
  border: 1px solid #efe1d8;
  border-radius: 12px;
  background: #fff;
  padding: 12px;
}

.card-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.card-top h3 {
  margin: 0;
  font-size: 1rem;
  color: #3e2723;
}

.status-chip {
  border-radius: 999px;
  font-size: 0.72rem;
  font-weight: 700;
  padding: 4px 8px;
}

.accepted {
  background: #fff3e0;
  color: #b26a00;
}

.progress {
  background: #e3f2fd;
  color: #0f5ca8;
}

.done {
  background: #e8f5e9;
  color: #2d7d32;
}
/* Add collected status button style */
.collected {
  background: #e0f7fa;
  color: #00838f;
}


.cancelled {
  background: #ffebee;
  color: #b71c1c;
}

.summary,
.meta {
  margin: 0;
  color: #6e5a50;
  font-size: 0.9rem;
}

.meta {
  margin-top: 4px;
}

.actions {
  margin-top: 10px;
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.action-btn {
  border: none;
  border-radius: 8px;
  padding: 6px 10px;
  color: #fff;
  cursor: pointer;
  font-size: 0.82rem;
}

.in-progress {
  background: #0f5ca8;
}

.ready {
  background: #2d7d32;
}

.cancel {
  background: #b03c3c;
}

.state-wrap {
  display: grid;
  place-items: center;
  gap: 8px;
  color: #6f5b52;
  padding: 26px 0;
}

.spin {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
