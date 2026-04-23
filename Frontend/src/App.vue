<template>
  <div class="app-wrapper">
    <HeaderBar />

    <main class="main-content">
      <MenuPage 
        v-if="activeTab === 'menu'" 
        :menuItems="menuData" 
        @addToCart="handleAddToCart" 
      />
      
      <CartPage 
        v-if="activeTab === 'cart'" 
        :cart="currentCart" 
        :total="cartTotal"
        @close="activeTab = 'menu'"
        @checkout="handleCheckout"
        @remove="handleRemoveFromCart"
      />
      
      <CheckoutPage 
        v-if="activeTab === 'checkout'" 
        :cart="currentCart"
        @changeTab="tab => activeTab = tab" 
      />
      
      <OrdersPage 
        v-if="activeTab === 'orders'" 
        :history="orderHistory" 
      />

      <StaffDashboard v-if="activeTab === 'staff'" />
    </main>

    <BottomNav 
      :activeTab="activeTab" 
      :cartCount="currentCart.length" 
      @changeTab="tab => activeTab = tab"
      @toggleCart="activeTab = 'cart'"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
//Import Layout Components
import HeaderBar from './components/HeaderBar.vue';
import BottomNav from './components/BottomNav.vue';
//Import View Components
import MenuPage from './views/MenuPage.vue';
import CartPage from './views/CartPage.vue';
import CheckoutPage from './views/CheckoutPage.vue';
import OrdersPage from './views/OrdersPage.vue';
import StaffDashboard from './views/StaffDashboard.vue';

// App state - combining both versions
const activeTab = ref('menu');
const currentCart = ref([]);
const orderHistory = ref([
  { id: '1001', summary: '1x Latte (Large)', arrivalTime: '08:30', status: 'READY' }
]);

// Menu Data based on Project Brief
const menuData = ref([
  { id: 1, name: 'Americano', regPrice: 1.50, largePrice: 2.00 },
  { id: 2, name: 'Latte', regPrice: 2.50, largePrice: 3.00 },
  { id: 3, name: 'Mocha', regPrice: 2.50, largePrice: 3.00 },
  { id: 4, name: 'Mineral Water', regPrice: 1.00, largePrice: null }
]);

// Computed total
const cartTotal = computed(() => currentCart.value.reduce((sum, item) => sum + item.price, 0));

// Logic handlers
const handleAddToCart = (item, size) => {
  const price = size === 'Regular' ? item.regPrice : item.largePrice;
  currentCart.value.push({ name: item.name, size, price });
};

const handleRemoveFromCart = (groupKey) => {
  const index = currentCart.value.findIndex(
    item => `${item.name}__${item.size}` === groupKey
  );

  if (index === -1) {
    return;
  }

  currentCart.value.splice(index, 1);
  if (currentCart.value.length === 0 && activeTab.value === 'cart') {
    activeTab.value = 'menu';
  }
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
  activeTab.value = 'orders';
};
</script>

<style>
.app-wrapper {
  background: rgba(255, 255, 255, 0.95); 
  min-height: 100vh;
}

.main-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px 100px; /* Space for bottom nav */
}
</style>