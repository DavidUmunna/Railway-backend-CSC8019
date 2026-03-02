<template>
  <div class="checkout-page">
    <h2>Choose Pickup Time</h2>

    <input type="time" v-model="pickupTime" />

    <button @click="confirmOrder">
      Confirm Order
    </button>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const pickupTime = ref('')

const confirmOrder = () => {
  alert("Order confirmed for " + pickupTime.value)
}
</script>

<style scoped>
.checkout-page {
  background: rgba(255,255,255,0.9);
  padding: 25px;
  border-radius: 20px;
}
</style>