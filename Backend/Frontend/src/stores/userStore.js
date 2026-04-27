import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import authService from '../services/authService.js';

export const useUserStore = defineStore('user', () => {
  // State
  const staffUser = ref(null);
  const customerUser = ref(null);
  const authToken = ref(localStorage.getItem('authToken') || null);
  const isStaffLoggedIn = ref(!!authToken.value);
  const isCustomerLoggedIn = ref(false);
  const userError = ref(null);
  const isLoading = ref(false);

  // Getters
  const currentUser = computed(() => staffUser.value || customerUser.value);
  const userType = computed(() => {
    if (staffUser.value) return 'staff';
    if (customerUser.value) return 'customer';
    return null;
  });

  // Actions
  const staffLogin = async (credentials) => {
    isLoading.value = true;
    userError.value = null;
    try {
      const response = await authService.login(credentials);
      authToken.value = response.token;
      staffUser.value = {
        id: response.id || credentials.username,
        username: credentials.username,
        name: response.username || credentials.username.split('@')[0],
        role: response.role || 'Staff',
        email: credentials.username
      };
      isStaffLoggedIn.value = true;
      localStorage.setItem('authToken', response.token);
      return response;
    } catch (error) {
      userError.value = error.message || 'Login failed';
      isStaffLoggedIn.value = false;
      throw error;
    } finally {
      isLoading.value = false;
    }
  };

  const staffLogout = () => {
    authService.logout();
    staffUser.value = null;
    authToken.value = null;
    isStaffLoggedIn.value = false;
    localStorage.removeItem('authToken');
  };

  const customerLogin = (userData) => {
    customerUser.value = {
      id: userData.id || Date.now().toString(),
      email: userData.email || 'customer@local',
      name: userData.name || 'Guest Customer',
      phone: userData.phone || null
    };
    isCustomerLoggedIn.value = true;
  };

  const customerLogout = () => {
    customerUser.value = null;
    isCustomerLoggedIn.value = false;
  };

  const updateCurrentUser = (updates) => {
    if (staffUser.value) {
      staffUser.value = { ...staffUser.value, ...updates };
    } else if (customerUser.value) {
      customerUser.value = { ...customerUser.value, ...updates };
    }
  };

  const clearError = () => {
    userError.value = null;
  };

  return {
    // State
    staffUser,
    customerUser,
    authToken,
    isStaffLoggedIn,
    isCustomerLoggedIn,
    userError,
    isLoading,
    // Getters
    currentUser,
    userType,
    // Actions
    staffLogin,
    staffLogout,
    customerLogin,
    customerLogout,
    updateCurrentUser,
    clearError
  };
});