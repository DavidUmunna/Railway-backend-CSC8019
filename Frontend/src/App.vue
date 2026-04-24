<template>
  <div class="app-container">
    <HeaderBar v-if="activeTab === 'menu' || activeTab === 'orders'" />

    <StaffLogin
      v-if="activeTab === 'staffLogin'"
      :is-submitting="userStore.isLoading"
      :server-error="userStore.userError"
      @login="handleStaffLogin"
      @cancel="handleStaffLoginCancel"
    />

    <StaffDashboard
      v-else-if="activeTab === 'staffDashboard'"
      :staff-user="userStore.staffUser"
      @logout="handleStaffLogout"
    />

    <HomePage v-else-if="activeTab === 'home'" @navigate="setActiveTab" />

    <AboutPage v-else-if="activeTab === 'about'" @navigate="setActiveTab" />

    <MenuPage
      v-else-if="activeTab === 'menu'"
      :menu-items="menuItems"
      :is-loading="menuLoading"
      :error="menuError"
      @add="handleAddToCart"
      @navigate-home="setActiveTab('home')"
    />

    <OrdersPage
      v-else-if="activeTab === 'orders'"
      :orders="cartStore.orderHistory"
      :is-loading="cartStore.isLoading"
      :error="cartStore.cartError"
    />

    <CheckoutPage
      v-else-if="activeTab === 'checkout'"
      :cart="cartStore.cart"
      :is-submitting="cartStore.isLoading"
      :error="cartStore.cartError"
      @confirm="handleConfirmCheckout"
      @cancel="setActiveTab('menu')"
    />

    <CartPage
      v-if="cartStore.showCart"
      :cart="cartStore.cart"
      :total="cartStore.formattedTotal"
      :is-checking-out="cartStore.isLoading"
      @close="cartStore.showCart = false"
      @checkout="handleCheckout"
      @remove="handleRemoveFromCart"
    />

    <BottomNav
      v-if="activeTab === 'menu' || activeTab === 'orders'"
      :active-tab="activeTab"
      :cart-count="cartStore.cartCount"
      @change-tab="handleBottomNavTab"
      @toggle-cart="cartStore.showCart = true"
    />


  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import HeaderBar from './components/HeaderBar.vue';
import BottomNav from './components/BottomNav.vue';
import MenuPage from './views/MenuPage.vue';
import CartPage from './views/CartPage.vue';
import OrdersPage from './views/OrdersPage.vue';
import CheckoutPage from './views/CheckoutPage.vue';
import StaffLogin from './views/StaffLogin.vue';
import HomePage from './views/HomePage.vue';
import AboutPage from './views/AboutPage.vue';
import StaffDashboard from './views/StaffDashboard.vue';
import { useCartStore } from './stores/cartStore.js';
import { useStationStore } from './stores/stationStore.js';
import { useUserStore } from './stores/userStore.js';
import menuService from './services/menuService.js';

const cartStore = useCartStore();
const stationStore = useStationStore();
const userStore = useUserStore();

const activeTab = ref('home');
const menuItems = ref([]);
const menuLoading = ref(false);
const menuError = ref('');

const setActiveTab = (tab) => {
  activeTab.value = tab;
  if (tab === 'menu' && (menuItems.value.length === 0 || menuError.value)) {
    loadMenu();
  }
};

const handleBottomNavTab = (tab) => {
  if (tab === 'staff') {
    setActiveTab('staffLogin');
    return;
  }
  setActiveTab(tab);
};

const loadMenu = async () => {
  menuLoading.value = true;
  menuError.value = '';
  try {
    menuItems.value = await menuService.getAllMenuItems();
  } catch (error) {
    menuError.value = error.message || 'Failed to load menu items.';
  } finally {
    menuLoading.value = false;
  }
};

const handleAddToCart = ({ item, size }) => {
  cartStore.addToCart(item, size);
};

const handleRemoveFromCart = (group) => {
  const index = cartStore.cart.findIndex(
    (item) => item.id === group.id && item.size === group.size
  );

  if (index === -1) {
    return;
  }

  const item = cartStore.cart[index];
  const nextQty = (item.quantity || 1) - 1;
  cartStore.updateCartItemQuantity(index, nextQty);
};

const handleCheckout = () => {
  if (cartStore.cart.length === 0) {
    return;
  }

  cartStore.showCart = false;
  activeTab.value = 'checkout';
};

const handleConfirmCheckout = async (checkoutDetails) => {
  try {
    await cartStore.checkout(checkoutDetails);
    activeTab.value = 'orders';
  } catch (error) {
    console.error('Checkout failed:', error);
  }
};

const handleStaffLogin = async (credentials) => {
  try {
    await userStore.staffLogin(credentials);
    activeTab.value = 'staffDashboard';
  } catch (error) {
    console.error('Login failed:', error);
  }
};

const handleStaffLoginCancel = () => {
  userStore.clearError();
  activeTab.value = 'home';
};

const handleStaffLogout = () => {
  userStore.staffLogout();
  activeTab.value = 'home';
};

onMounted(async () => {
  await Promise.all([loadMenu(), cartStore.loadOrderHistory(), stationStore.loadStations().catch(() => null)]);

  if (userStore.isStaffLoggedIn) {
    activeTab.value = 'staffDashboard';
  }
});
</script>

<style scoped>
.app-container {
  min-height: 100vh;
  background: transparent;
  padding-bottom: 88px;
}

.staff-login-fab {
  position: fixed;
  right: 16px;
  bottom: 92px;
  z-index: 901;
  border: none;
  border-radius: 999px;
  padding: 10px 14px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  background: #3e2723;
  color: #fff;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
  font-weight: 700;
}

.staff-login-fab:hover {
  background: #5d4037;
}

@media (max-width: 768px) {
  .app-container {
    padding-bottom: 84px;
  }

  .staff-login-fab {
    right: 12px;
    bottom: 84px;
    padding: 9px 12px;
    font-size: 0.85rem;
  }
}
</style>
