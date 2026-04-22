<template>
  <div class="app-wrapper" >
    <HeaderBar />

    <main class="main-content">
      <MenuPage v-if="activeTab === 'menu'" />
      
      <CartPage 
      v-if="activeTab === 'cart'" 
      @changeTab="tab => activeTab = tab" 
      />
      
      <CheckoutPage 
      v-if="activeTab === 'checkout'" 
      @changeTab="tab => activeTab = tab" 
      />
      
      <OrdersPage v-if="activeTab === 'orders'" />

      <StaffDashboard v-if="activeTab === 'staff'" />
    </main>

      

    <BottomNav 
      :activeTab="activeTab" 
      :cartCount="cart.totalCount" 
      @changeTab="tab => activeTab = tab"
      @toggleCart="activeTab = 'cart'"
    />
  </div>
</template>

<script setup>
/**
 * Root Application Component
 * Manages the top-level state for navigation and coordinates between different views.
 */
import { ref } from 'vue';
import { useCartStore } from './store/cart';
//Import Layout Components
import HeaderBar from './components/HeaderBar.vue';
import BottomNav from './components/BottomNav.vue';
//Import View Components
import MenuPage from './views/MenuPage.vue';
import CartPage from './views/CartPage.vue';
import CheckoutPage from './views/CheckoutPage.vue';
import OrdersPage from './views/OrdersPage.vue';
import StaffDashboard from './views/StaffDashboard.vue';
//Initialize the global shopping cart state
const cart = useCartStore();
const activeTab = ref('menu');
</script>

<style>
body {
  margin: 0;
  padding: 0;
  /* Full-screen coffee-themed background image */
  background-image: url('./assets/background2.jpg');
  background-size: cover;          
  background-position: center;     
  background-attachment: fixed;    
  background-repeat: no-repeat;
}

.app-wrapper {
  background: rgba(255, 255, 255, 0.85); 
  min-height: 100vh;
}


</style>