<template>
  <div class="checkout-page">
    <h2>
      Choose Pickup Time
    </h2>

    <div class="input-group">
      <input type="time" v-model="pickupTime" />
    </div>

    <button @click="confirmOrder" class="confirm-btn">
      Confirm Order
    </button>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const pickupTime = ref('')

const confirmOrder = () => {
  if (pickupTime.value) {
    alert("Order confirmed for " + pickupTime.value)
  } else {
    alert("Please select a pickup time")
  }
}
</script>

<style scoped>
.checkout-page {
  background: rgba(255,255,255,0.9);
  padding: 25px;
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

h2 {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #3e2723;
  margin: 0 0 10px 0;
}

.input-group {
  display: flex;
  align-items: center;
  gap: 10px;
  background: white;
  padding: 10px 15px;
  border-radius: 10px;
  border: 1.5px solid #d7ccc8;
}

input[type="time"] {
  border: none;
  background: transparent;
  font-size: 1rem;
  color: #3e2723;
  flex: 1;
}

input[type="time"]:focus {
  outline: none;
}

.confirm-btn {
  background: #3e2723;
  color: white;
  padding: 15px 20px;
  border-radius: 10px;
  font-weight: bold;
  font-size: 1rem;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  transition: 0.2s;
}

.confirm-btn:hover {
  background: #5d4037;
}

@media (max-width: 480px) {
  .checkout-page {
    padding: 16px;
    border-radius: 14px;
  }

  .confirm-btn {
    width: 100%;
    padding: 14px 16px;
  }
}
</style>