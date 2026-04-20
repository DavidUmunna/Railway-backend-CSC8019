<template>
  <div class="overlay" @click.self="$emit('close')">
    <div class="cart-modal">
      <div class="modal-header">
        <h3><ShoppingCart size="20" /> Confirm Your Order</h3>
        <button class="close-btn" @click="$emit('close')" :title="'Close cart'">
          <X size="20" />
        </button>
      </div>

      <ul class="cart-items">
        <li v-for="(item, index) in cart" :key="index">
          <span>
            {{ item.name }} ({{ item.size }})
          </span>
          <span>£{{ item.price.toFixed(2) }}</span>
        </li>
      </ul>

      <div class="total-box">
        <span>Grand Total</span>
        <span class="total-price">£{{ formattedTotal }}</span>
      </div>

      <button class="pay-btn" :disabled="isCheckingOut" @click="$emit('checkout')">
        <Loader2 v-if="isCheckingOut" size="18" class="spin" />
        <CreditCard v-else size="18" />
        {{ isCheckingOut ? 'Processing...' : 'Confirm & Pay' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps(['cart', 'total', 'isCheckingOut'])
defineEmits(['close', 'checkout'])

const formattedTotal = computed(() => {
  const amount = Number(props.total)
  return Number.isFinite(amount) ? amount.toFixed(2) : '0.00'
})
</script>

<style scoped>
.overlay {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.6);
  display: flex;
  align-items: flex-end;
  z-index: 2000;
}

.cart-modal {
  background: white;
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
  padding: 30px 30px 100px;
  border-radius: 24px 24px 0 0;
  max-height: 85vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.close-btn {
  background: none;
  border: none;
  cursor: pointer;
  color: #8d6e63;
  padding: 4px;
  display: flex;
  align-items: center;
  transition: 0.2s;
}

.close-btn:hover {
  color: #5d4037;
}

.cart-items { list-style: none; padding: 0; margin-bottom: 20px; }
.cart-items li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #eee;
}

.cart-items li span:first-child {
  display: flex;
  align-items: center;
  gap: 8px;
}

.total-box {
  background: #efebe9;
  padding: 20px;
  border-radius: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.total-price { font-size: 1.6rem; font-weight: 900; color: #3e2723; }

.pay-btn {
  width: 100%;
  background: #3e2723;
  color: white;
  padding: 18px;
  border-radius: 12px;
  font-weight: bold;
  font-size: 1.1rem;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  transition: 0.2s;
}

.pay-btn:hover {
  background: #5d4037;
}

.pay-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.spin {
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

@media (max-width: 768px) {
  .cart-modal {
    padding: 20px 16px 84px;
    border-radius: 18px 18px 0 0;
  }

  .modal-header h3 {
    font-size: 1rem;
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .total-price {
    font-size: 1.3rem;
  }

  .pay-btn {
    font-size: 1rem;
    padding: 14px;
  }
}
</style>