import axios from 'axios';

// Base configuration for Axios
const apiClient = axios.create({
  baseURL: '/api', // This should match your backend server address
  headers: {
    'Content-Type': 'application/json'
  }
});

export default {
  // Get the coffee menu from backend [cite: 9]
  getMenu() {
    return apiClient.get('/menu');
  },
  
  // Submit a new order [cite: 11, 12]
  createOrder(orderData) {
    return apiClient.post('/orders', orderData);
  },

  // Get all orders (for Staff Dashboard) [cite: 13]
  getAllOrders() {
    return apiClient.get('/orders');
  },

  // Update order status (Staff Action) 
  updateOrderStatus(orderId, status) {
    return apiClient.patch(`/orders/${orderId}/status`, { status });
  }
};