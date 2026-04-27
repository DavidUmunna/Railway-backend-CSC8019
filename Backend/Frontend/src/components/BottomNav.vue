<template>
  <nav class="bottom-nav">
    <div class="nav-content">
      <button @click="$emit('changeTab', 'menu')" :class="{ active: activeTab === 'menu' }" title="View Menu">
        <Coffee size="18" class="nav-icon" />
        <span class="nav-label">Menu</span>
      </button>

      <button @click="$emit('changeTab', 'orders')" :class="{ active: activeTab === 'orders' }" title="View Orders">
        <List size="18" class="nav-icon" />
        <span class="nav-label">Orders</span>
      </button>

      <div v-if="cartCount > 0" class="cart-badge" @click="$emit('toggleCart')" title="View Cart">
        <ShoppingCart size="16" />
        <span class="cart-label">{{ cartCount }}</span>
      </div>
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
  background: #fff;
  border-top: 1px solid #eee;
  z-index: 900;
}

.nav-content {
  max-width: 900px;
  margin: 0 auto;
  height: 70px;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

button {
  border: none;
  background: none;
  color: #8d6e63;
  cursor: pointer;
  padding: 10px;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: 0.2s;
}

button:hover {
  color: #5d4037;
}

button.active {
  color: #3e2723;
  font-weight: 800;
  border-bottom: 3px solid #ffab40;
}

.nav-label {
  font-size: 0.9rem;
  font-weight: 600;
}

.cart-label {
  font-weight: 700;
  font-size: 0.9rem;
}

.cart-badge {
  background: #3e2723;
  color: #fff;
  padding: 8px 14px;
  border-radius: 20px;
  font-weight: bold;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: 0.2s;
}

.cart-badge:hover {
  background: #5d4037;
  transform: scale(1.05);
}

@media (max-width: 768px) {
  .nav-content {
    height: 64px;
    padding: 0 10px;
  }

  button {
    gap: 6px;
    padding: 8px;
  }

  .nav-label,
  .cart-label {
    font-size: 0.8rem;
  }

  .cart-badge {
    padding: 7px 10px;
    border-radius: 16px;
  }
}
</style>
