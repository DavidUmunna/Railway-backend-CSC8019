<template>
  <div class="app-wrapper">
    <template v-if="activeTab === 'staffLogin'">
      <StaffLogin
        :is-submitting="userStore.isLoading"
        :server-error="loginError"
        @login="handleStaffLogin"
        @cancel="activeTab = 'home'"
      />
    </template>

    <template v-else-if="activeTab === 'staffDashboard'">
      <StaffDashboard 
        :staff-user="userStore.staffUser" 
        @logout="handleStaffLogout" 
      />
    </template>

    <!-- Home and About pages don't use HeaderBar/BottomNav -->
    <template v-else-if="activeTab === 'home' || activeTab === 'about'">
      <HomePage 
        v-if="activeTab === 'home'" 
        @navigate="handleNavigateFromHome"
      />
      <AboutPage 
        v-if="activeTab === 'about'"
      />
      <!-- Quick Navigation Footer for About/Home pages -->
      <div class="quick-nav">
        <button @click="activeTab = 'home'" :class="{ active: activeTab === 'home' }">Home</button>
        <button @click="activeTab = 'menu'" :class="{ active: activeTab === 'menu' }">Menu</button>
        <button @click="activeTab = 'about'" :class="{ active: activeTab === 'about' }">About</button>
        <button @click="activeTab = 'staffLogin'" :class="{ active: activeTab === 'staffLogin' }">Staff Login</button>
      </div>
    </template>

    <!-- Regular app pages with header and bottom nav -->
    <template v-else>
      <HeaderBar />

      <main class="main-content">
        <MenuPage 
          v-if="activeTab === 'menu'" 
          :menuItems="menuData" 
          :is-loading="isLoading"
          :error="error"
          @addToCart="handleAddToCart" 
          @navigateHome="activeTab = 'home'"
        />
        <OrdersPage 
          v-if="activeTab === 'orders'" 
          :history="cartStore.orderHistory" 
          :is-loading="cartStore.isLoading"
          :error="cartStore.cartError"
        />
      </main>

      <CartPage 
        v-if="cartStore.showCart" 
        :cart="cartStore.cart" 
        :total="cartStore.cartTotal"
        :is-checking-out="isCheckingOut"
        @close="cartStore.showCart = false"
        @checkout="handleCheckout"
      />

      <BottomNav 
        :activeTab="activeTab" 
        :cartCount="cartStore.cartCount"
        @changeTab="tab => {
          if (tab === 'home' || tab === 'about') {
            activeTab = tab;
          } else {
            activeTab = tab;
          }
        }"
        @toggleCart="cartStore.showCart = true"
        @navigateHome="activeTab = 'home'"
        @navigateAbout="activeTab = 'about'"
      />
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useUserStore } from './stores/userStore.js';
import { useCartStore } from './stores/cartStore.js';
import HeaderBar from './components/HeaderBar.vue';
import BottomNav from './components/BottomNav.vue';
import MenuPage from './views/MenuPage.vue';
import OrdersPage from './views/OrdersPage.vue';
import CartPage from './views/CartPage.vue';
import HomePage from './views/HomePage.vue';
import AboutPage from './views/AboutPage.vue';
import StaffLogin from './views/StaffLogin.vue';
import StaffDashboard from './views/StaffDashboard.vue';

// Import API services
import menuService from './services/menuService.js';

// Initialize Pinia stores
const userStore = useUserStore();
const cartStore = useCartStore();

// Local component state
const activeTab = ref('home');
const menuData = ref([]);
const isLoading = ref(false);
const error = ref(null);
const loginError = ref('');
const isCheckingOut = ref(false);

// Load menu data on mount
const loadMenuData = async () => {
  try {
    isLoading.value = true;
    error.value = null;
    menuData.value = await menuService.getAllMenuItems();
  } catch (err) {
    error.value = 'Failed to load menu data';
    console.error('Menu loading error:', err);
  } finally {
    isLoading.value = false;
  }
};

// Initialize on mount
onMounted(async () => {
  await cartStore.loadOrderHistory();
});

watch(activeTab, async (tab) => {
  if (tab === 'menu') {
    await loadMenuData();
  }
});

// Navigation handler from home
const handleNavigateFromHome = (tab) => {
  activeTab.value = tab;
};

// Staff login handler
const handleStaffLogin = async (payload) => {
  try {
    loginError.value = '';
    await userStore.staffLogin({
      username: payload.username,
      password: payload.password
    });
    activeTab.value = 'staffDashboard';
  } catch (err) {
    console.error('Staff login failed:', err);
    loginError.value = userStore.userError || 'Unable to sign in. Please try again.';
  }
};

// Staff logout handler
const handleStaffLogout = () => {
  userStore.staffLogout();
  activeTab.value = 'staffLogin';
};

// Add to cart handler
const handleAddToCart = (item, size) => {
  cartStore.addToCart(item, size);
};

// Checkout handler
const handleCheckout = async () => {
  try {
    isCheckingOut.value = true;
    await cartStore.checkout();
    activeTab.value = 'orders';
  } catch (err) {
    console.error('Checkout failed:', err);
  } finally {
    isCheckingOut.value = false;
  }
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

.app-wrapper { 
  min-height: 100vh;
  position: relative;
  box-sizing: border-box;
}

.main-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px 100px; /* Space for bottom nav */
}

/* Quick Navigation for Home/About pages */
.quick-nav {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: flex-start;
  gap: 12px;
  padding: 12px 16px;
  z-index: 100;
}

.quick-nav button {
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.12);
  padding: 6px 12px;
  font-size: 14px;
  font-weight: 600;
  color: #333;
  cursor: pointer;
  border-radius: 999px;
  transition: all 0.2s ease;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.quick-nav button:hover {
  color: #ffbf00;
  background: rgba(255, 255, 255, 0.25);
}

.quick-nav button.active {
  color: #ffbf00;
  background: rgba(255, 255, 255, 0.35);
  box-shadow: 0 10px 24px rgba(0, 0, 0, 0.08);
}

@media (max-width: 768px) {
  .main-content {
    padding: 0 14px 92px;
  }

  .quick-nav {
    justify-content: flex-start;
    overflow-x: auto;
    white-space: nowrap;
    padding: 10px 12px;
    gap: 8px;
  }

  .quick-nav button {
    font-size: 12px;
    padding: 6px 10px;
    letter-spacing: 0.2px;
    flex: 0 0 auto;
  }
}
</style>