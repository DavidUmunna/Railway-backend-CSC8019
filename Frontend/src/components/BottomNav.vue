<template>
  <nav class="bottom-nav">
    <div class="nav-content">
      <button @click="$emit('changeTab', 'menu')" :class="{ active: activeTab === 'menu' }">
        Menu
      </button>
      <button @click="$emit('changeTab', 'orders')" :class="{ active: activeTab === 'orders' }">
        Orders
      </button>
      
      <div v-if="cartCount > 0" class="cart-badge" @click="$emit('toggleCart')">
        🛒 {{ cartCount }}
      </div>
      
      <button @click="$emit('changeTab', 'staff')" :class="{ active: activeTab === 'staff' }">
        Staff
      </button>
    </div>
  </nav>
</template>

<script setup>
defineProps(['activeTab', 'cartCount']);
defineEmits(['changeTab', 'toggleCart']);
</script>

<style scoped>
.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: white;
  border-top: 1px solid #eee;
  z-index: 900;
  width: 100%;
}

.nav-content {
  max-width: 800px;
  margin: 0 auto;
  height: 70px;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

button {
  border: none;
  background: none;
  font-size: 1rem;
  color: #8d6e63;
  cursor: pointer;
  padding: 10px;
  transition: 0.2s;
}

button:hover {
  color: #3e2723;
}

button.active {
  color: #3e2723;
  font-weight: 800;
  border-bottom: 3px solid #ffab40;
}

.cart-badge {
  background: #3e2723;
  color: white;
  padding: 8px 16px;
  border-radius: 20px;
  font-weight: bold;
  cursor: pointer;
  transition: 0.2s;
}

.cart-badge:hover {
  background: #6F4E37;
}
</style>