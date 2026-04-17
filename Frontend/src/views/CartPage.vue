<template>
  <div class="overlay" @click.self="$emit('close')">
    <div class="cart-modal">
      <div class="modal-header">
        <h3>Confirm Your Order</h3>
        <button class="close-btn" @click="$emit('close')">✕</button>
      </div>

      <ul class="cart-items">
        <li v-for="(item, index) in cart" :key="index">
          <span>{{ item.name }} ({{ item.size }})</span>
          <span>£{{ item.price.toFixed(2) }}</span>
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
defineProps(['cart', 'total']);
defineEmits(['close', 'checkout']);
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
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.cart-items { list-style: none; padding: 0; margin-bottom: 20px; }
.cart-items li {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #eee;
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
}
</style>