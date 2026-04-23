<template>
  <div class="overlay" v-if="cart && cart.length > 0" @click.self="$emit('close')">
    <div class="cart-modal">
      <div class="modal-header">
        <h3>Confirm Your Order</h3>
        <button class="close-btn" @click="$emit('close')">✕</button>
      </div>

      <ul class="cart-items">
        <li v-for="item in groupedCart" :key="item.key">
          <span class="item-info">{{ item.name }} ({{ item.size }}) x {{ item.quantity }}</span>
          <div class="item-actions">
            <span class="item-price">£{{ item.lineTotal.toFixed(2) }}</span>
            <button class="remove-btn" @click="$emit('remove', item.key)">Remove</button>
          </div>
        </li>
      </ul>

      <div class="total-box">
        <span>Grand Total</span>
        <span class="total-price">£{{ total.toFixed(2) }}</span>
      </div>

      <button class="pay-btn" @click="$emit('checkout')">Confirm & Pay</button>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  cart: {
    type: Array,
    default: () => []
  },
  total: {
    type: Number,
    default: 0
  }
});

defineEmits(['close', 'checkout', 'remove']);

const groupedCart = computed(() => {
  const map = new Map();

  for (const item of props.cart) {
    const key = `${item.name}__${item.size}`;
    const existing = map.get(key);

    if (existing) {
      existing.quantity += 1;
      existing.lineTotal += item.price;
    } else {
      map.set(key, {
        key,
        name: item.name,
        size: item.size,
        quantity: 1,
        lineTotal: item.price
      });
    }
  }

  return Array.from(map.values());
});
</script>

<style scoped>
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: flex-end;
  z-index: 2000;
}

.cart-modal {
  background: white;
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
  padding: 30px 20px 100px;
  border-radius: 24px 24px 0 0;
  animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  border-bottom: 2px solid #eee;
  padding-bottom: 15px;
}

.modal-header h3 {
  margin: 0;
  color: #3e2723;
  font-size: 1.3rem;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #8d6e63;
}

.close-btn:hover {
  color: #3e2723;
}

.cart-items {
  list-style: none;
  padding: 0;
  margin-bottom: 20px;
  max-height: 300px;
  overflow-y: auto;
}

.cart-items li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

.item-info {
  flex: 1;
  color: #3e2723;
  font-weight: 500;
}

.item-price {
  font-weight: 700;
  color: #6F4E37;
  min-width: 70px;
  text-align: right;
}

.item-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.remove-btn {
  border: 1px solid #d7ccc8;
  background: #fff;
  color: #8d6e63;
  border-radius: 8px;
  padding: 6px 10px;
  font-size: 0.85rem;
}

.remove-btn:hover {
  color: #c62828;
  border-color: #ef9a9a;
}

.total-box {
  background: #efebe9;
  padding: 20px;
  border-radius: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  font-weight: 600;
  color: #3e2723;
}

.total-price {
  font-size: 1.6rem;
  font-weight: 900;
  color: #3e2723;
}

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
  transition: 0.2s;
}

.pay-btn:hover {
  background: #6F4E37;
}

.pay-btn:active {
  transform: scale(0.98);
}
</style>