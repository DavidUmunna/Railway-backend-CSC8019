<template>
  <div class="app-wrapper">
    <HeaderBar />

    <main class="main-content">
      <MenuPage 
        v-if="activeTab === 'menu'" 
        :menuItems="menuData" 
        @addToCart="handleAddToCart" 
      />
      <OrdersPage 
        v-if="activeTab === 'orders'" 
        :history="orderHistory" 
      />
    </main>

    <CartPage 
      v-if="showCart" 
      :cart="currentCart" 
      :total="cartTotal"
      @close="showCart = false"
      @checkout="handleCheckout"
    />

    <BottomNav 
      :activeTab="activeTab" 
      :cartCount="currentCart.length"
      @changeTab="tab => activeTab = tab"
      @toggleCart="showCart = true"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import HeaderBar from './components/HeaderBar.vue';
import BottomNav from './components/BottomNav.vue';
import MenuPage from './views/MenuPage.vue';
import OrdersPage from './views/OrdersPage.vue';
import CartPage from './views/CartPage.vue';

// 1. App State
const activeTab = ref('menu');
const showCart = ref(false);
const currentCart = ref([]);
const orderHistory = ref([
  { id: '1001', summary: '1x Latte (Large)', arrivalTime: '08:30', status: 'READY' }
]);

// 2. Menu Data based on Project Brief
const menuData = ref([
  { id: 1, name: 'Americano', regPrice: 1.50, largePrice: 2.00 },
  { id: 2, name: 'Latte', regPrice: 2.50, largePrice: 3.00 },
  { id: 3, name: 'Mocha', regPrice: 2.50, largePrice: 3.00 },
  { id: 4, name: 'Mineral Water', regPrice: 1.00, largePrice: null }
]);

// 3. Computed Total
const cartTotal = computed(() => currentCart.value.reduce((sum, item) => sum + item.price, 0));

// 4. Logic Handlers
const handleAddToCart = (item, size) => {
  const price = size === 'Regular' ? item.regPrice : item.largePrice;
  currentCart.value.push({ name: item.name, size, price });
};

const handleCheckout = () => {
  const newOrder = {
    id: Math.floor(Math.random() * 9000) + 1000,
    summary: `${currentCart.value.length} items`,
    arrivalTime: 'Now',
    status: 'ACCEPTED'
  };
  orderHistory.value.unshift(newOrder);
  currentCart.value = [];
  showCart.value = false;
  activeTab.value = 'orders';
};
</script>

<style>
/* Global Styles */
body {
  margin: 0;
  font-family: 'Inter', sans-serif;
  background-color: #fdfaf8;
  background-image: radial-gradient(#d7ccc8 1px, transparent 1px);
  background-size: 20px 20px;
}

.app-wrapper { min-height: 100vh; }

.main-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px 100px; /* Space for bottom nav */
}
</style>