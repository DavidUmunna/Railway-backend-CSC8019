<template>
  <div class="orders-page">
    <div class="orders-header-row">
      <h2 class="orders-title">Your Orders</h2>
      <button v-if="!showPhoneModal" class="reopen-modal-btn" @click="showPhoneModal = true">Enter Phone Number</button>
    </div>

    <!-- Phone Number Modal as component -->
    <PhoneNumberModal
      v-if="showPhoneModal"
      :show="showPhoneModal"
      :isLoading="isLoading"
      :error="modalError"
      :phoneNumber="phoneNumber"
      @submit="onPhoneModalSubmit"
      @close="showPhoneModal = false"
    />

    <div v-if="isLoading" class="state-wrap">
      <Loader2 size="26" class="spin" />
      <p>Loading your orders...</p>
    </div>

    <div v-else-if="error" class="state-wrap error">
      <AlertTriangle size="22" />
      <p>{{ error }}</p>
    </div>

    <div v-else-if="!orders.length" class="state-wrap empty">
      <Package size="24" />
      <p>No orders yet. Place your first one from the menu.</p>
    </div>

    <div v-else class="orders-list">
      <div
        v-for="order in orders"
        :key="order.id"
        class="order-card"
        role="button"
        tabindex="0"
        @click="toggleOrder(order.id)"
        @keydown.enter.prevent="toggleOrder(order.id)"
      >
        <div class="order-main">
          <div>
            <h3>Order #{{ order.id }}</h3>
            <p>{{ order.summary || 'Order placed' }}</p>
            <div class="order-meta">

              <small v-if="order.pickupTime">Pickup: {{ order.pickupTime }}</small>
              <small v-if="order.totalAmount !== undefined">Total: £{{ order.totalAmount.toFixed(2) }}</small>
            </div>
          </div>

          <span class="status" :class="statusClass(order.status)">
            {{ formatStatus(order.status) }}
          </span>
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
      </div>
    </div>
  </div>
</template>


<script setup>
import { ref } from 'vue';
import orderService from '../services/orderService.js';
import PhoneNumberModal from '../components/PhoneNumberModal.vue';

const orders = ref([]);
const isLoading = ref(false);
const error = ref('');

const showPhoneModal = ref(true);
const phoneNumber = ref('');
const modalError = ref('');

const expandedOrders = ref(new Set());
const orderItemsMap = ref({}); // { [orderId]: [items] }

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

const isExpanded = (orderId) => expandedOrders.value.has(String(orderId));

const statusClass = (status = '') => {
  const normalized = String(status).toUpperCase();
  if (normalized === 'COMPLETED' || normalized === 'READY') return 'done';
  if (normalized === 'IN_PROGRESS') return 'progress';
  if (normalized === 'CANCELLED') return 'cancelled';
  return 'accepted';
};

const formatStatus = (status = '') => String(status).replace('_', ' ');


const onPhoneModalSubmit = async (enteredPhoneNumber) => {
  modalError.value = '';
  phoneNumber.value = enteredPhoneNumber;
  isLoading.value = true;
  try {
    const response = await orderService.getOrdersByPhone(phoneNumber.value);
    const result = Array.isArray(response.data) ? response.data : response;
    orders.value = result.map(order => ({
      id: order.orderId?.toString() || order.id?.toString() || '',
      summary: ` Thank You for your order`,
      arrivalTime: order.pickupTime || '--:--',
      status: order.orderStatus || order.status || 'ACCEPTED',
      orderDate: order.orderDate || null,
      pickupTime: order.pickupTime || null,
      totalAmount: Number(order.totalAmount || 0),
      items: order.items || []
    }));
    showPhoneModal.value = false;
    error.value = '';
  } catch (err) {
    modalError.value = err.message || 'Failed to fetch orders.';
    orders.value = [];
    error.value = '';
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.orders-header-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 15px;
}

.reopen-modal-btn {
  background: #3e2723;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 4px 10px;
  font-size: 0.92em;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.2s;

}
.reopen-modal-btn:hover {
  background: #5d4037;
}

.orders-title {
  margin: 0;
  color: #3e2723;
  font-size: 1.5rem;
  font-weight: 700;
}
.orders-page {
  max-width: 900px;
  margin: 0 auto;
  padding: 24px 16px;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: #fff;
  padding: 32px 24px;
  border-radius: 8px;
  box-shadow: 0 2px 16px rgba(0,0,0,0.15);
  min-width: 320px;
  max-width: 90vw;
  text-align: center;
}

.phone-input {
  width: 100%;
  padding: 8px 12px;
  margin: 16px 0;
  font-size: 1.1em;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.modal-actions {
  margin-top: 12px;
}

.modal-actions button {
  padding: 8px 20px;
  font-size: 1em;
  border: none;
  border-radius: 4px;
  background: #007bff;
  color: #fff;
  cursor: pointer;
}
.modal-actions button:disabled {
  background: #aaa;
  cursor: not-allowed;
}
.modal-error {
  color: #d32f2f;
  margin-top: 8px;
}

h2 {
  margin: 0 0 16px;
  color: #3e2723;
}

.orders-list {
  display: grid;
  gap: 12px;
}

.order-card {
  background: #fff;
  border: 1px solid #efdfd3;
  border-radius: 12px;
  padding: 14px;
  display: grid;
  gap: 10px;
  cursor: pointer;
}

.order-main {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
}

.order-card h3 {
  margin: 0 0 4px;
  color: #3e2723;
}

.order-card p {
  margin: 0 0 4px;
  color: #6f5b52;
}

.order-details {
  border-top: 1px solid #f2e8e1;
  padding-top: 10px;
}

.order-meta {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  font-size: 0.9rem;
  color: #5f4b43;
}

.order-details h4 {
  margin: 0 0 8px;
  color: #3e2723;
  font-size: 0.92rem;
}

.items-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: grid;
  gap: 6px;
}

.items-list li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #5f4b43;
}

.no-items {
  margin: 0;
  color: #8a756b;
  font-size: 0.9rem;
}

.status {
  border-radius: 999px;
  padding: 6px 10px;
  font-size: 0.8rem;
  font-weight: 700;
  text-transform: uppercase;
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

.cancelled {
  background: #ffebee;
  color: #b71c1c;
}

.state-wrap {
  display: grid;
  place-items: center;
  gap: 8px;
  color: #5d4037;
  padding: 28px 0;
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
