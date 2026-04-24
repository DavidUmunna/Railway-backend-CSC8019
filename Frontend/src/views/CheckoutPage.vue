<template>
  <div class="checkout-page">
    <h2>Order Summary</h2>

    <div v-if="cart.length === 0" class="empty-state">
      <p>Your cart is empty.</p>
      <button @click="$emit('cancel')">Back to Menu</button>
    </div>

    <div v-else class="checkout-card">
      <ul>
        <li v-for="item in cart" :key="item.id + '-' + item.size">
          <span>{{ item.name }} ({{ item.size }}) x {{ item.quantity || 1 }}</span>
          <strong>${{ (Number(item.price || 0) * Number(item.quantity || 1)).toFixed(2) }}</strong>
        </li>
      </ul>

      <div class="form-grid">
        <label>
          First Name
          <input type="text" v-model.trim="customerFirstName" placeholder="Jane" />
        </label>

        <label>
          Last Name
          <input type="text" v-model.trim="customerLastName" placeholder="Doe" />
        </label>

        <label>
          Phone Number
          <input type="tel" v-model.trim="customerPhoneNumber" placeholder="07123 456789" />
        </label>

        <label>
          Pickup Time
          <input type="time" v-model="pickupTime" />
        </label>
      </div>

      <p v-if="validationError || error" class="error-text">{{ validationError || error }}</p>
      <p class="total">Total: ${{ cartTotal.toFixed(2) }}</p>

      <div class="actions">
        <button class="secondary" @click="$emit('cancel')">Back</button>
        <button @click="handleConfirm" :disabled="isSubmitting">
          {{ isSubmitting ? 'Submitting...' : 'Place Order' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue';
import { useCartStore } from '../stores/cartStore.js';
import { useStationStore } from '../stores/stationStore.js';

const cartStore = useCartStore();
const stationStore = useStationStore();

const props = defineProps({
  cart: {
    type: Array,
    default: () => [],
  },
  isSubmitting: {
    type: Boolean,
    default: false,
  },
  error: {
    type: String,
    default: '',
  },
});

const emit = defineEmits(['confirm', 'cancel']);

const customerFirstName = ref('');
const customerLastName = ref('');
const customerPhoneNumber = ref('');
const pickupTime = ref('');
const validationError = ref('');

onMounted(() => {
  cartStore.cartError = null;
});

watch([customerFirstName, customerLastName, customerPhoneNumber, pickupTime], () => {
  validationError.value = '';
});

const cartTotal = computed(() => {
  return props.cart.reduce((sum, item) => sum + Number(item.price || 0) * Number(item.quantity || 1), 0);
});

const handleConfirm = () => {
  validationError.value = '';

  if (!customerFirstName.value || !customerLastName.value || !customerPhoneNumber.value) {
    validationError.value = 'Please provide your first name, last name, and phone number.';
    return;
  }

  if (!pickupTime.value) {
    validationError.value = 'Please select a pickup time.';
    return;
  }

  if (!stationStore.currentStationId) {
    validationError.value = 'Station information is unavailable. Please return to menu and try again.';
    return;
  }

  emit('confirm', {
    customerFirstName: customerFirstName.value,
    customerLastName: customerLastName.value,
    customerPhoneNumber: customerPhoneNumber.value,
    pickupTime: pickupTime.value,
    stationId: Number(stationStore.currentStationId),
  });
};
</script>

<style scoped>
.checkout-page {
  max-width: 760px;
  margin: 0 auto;
  padding: 24px 16px;
}

h2 {
  color: #3e2723;
  margin-bottom: 14px;
}

.checkout-card {
  border: 1px solid #efdfd3;
  border-radius: 12px;
  padding: 16px;
  background: #fff;
}

ul {
  margin: 0 0 14px;
  padding: 0;
  list-style: none;
}

li {
  display: flex;
  justify-content: space-between;
  gap: 8px;
  padding: 8px 0;
  border-bottom: 1px solid #f2e8e1;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  margin-top: 12px;
}

label {
  display: grid;
  gap: 6px;
  color: #5d4037;
}

input {
  padding: 8px;
  border-radius: 8px;
  border: 1px solid #d7c5b9;
}

.error-text {
  margin-top: 12px;
  color: #b03c3c;
  font-size: 0.92rem;
}

.total {
  font-weight: 700;
  color: #3e2723;
  margin-top: 14px;
}

.actions {
  margin-top: 14px;
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

button {
  border: none;
  border-radius: 8px;
  background: #3e2723;
  color: #fff;
  padding: 10px 12px;
  cursor: pointer;
}

button.secondary {
  background: #8d6e63;
}

button:disabled {
  opacity: 0.65;
  cursor: not-allowed;
}

.empty-state {
  border: 1px dashed #ccb8ad;
  border-radius: 12px;
  padding: 20px;
  text-align: center;
}

@media (max-width: 640px) {
  .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>
