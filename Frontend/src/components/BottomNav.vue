<template>
  <nav class="bottom-nav">
    <button :class="{ active: activeTab === 'menu' }" @click="$emit('changeTab', 'menu')">
      <span>Menu</span>
    </button>
    
    <button @click="$emit('toggleCart')">
      <span>Cart ({{ cartCount }})</span>
    </button>

    <button :class="{ active: activeTab === 'orders' }" @click="$emit('changeTab', 'orders')">
      <span>My Orders</span>
    </button>

    <button :class="{ active: activeTab === 'staff' }" @click="$emit('changeTab', 'staff')">
      <span>Staff</span>
    </button>
  </nav>
</template>

<script setup>
//Component Props
defineProps(['activeTab', 'cartCount']);
//Component Emits
defineEmits(['changeTab', 'toggleCart']);
</script>

<style scoped>
.bottom-nav {
  position: fixed;
  bottom: 0;
  width: 100%;
  height: 60px;
  display: flex;
  justify-content: space-around;
  background: #fff;
  border-top: 1px solid #ddd;
}
.bottom-nav button { flex: 1; border: none; background: none; }
/* Primary brand color for the active state */
.bottom-nav button.active { color: #6F4E37; font-weight: bold; }
</style>