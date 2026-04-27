import axios from 'axios';

// Base configuration for Axios
const apiClient = axios.create({
  baseURL: '/api', 
  headers: {
    'Content-Type': 'application/json'
  }
});

export default {
  //Fetch the full coffee menu.
  getMenu() {
    return apiClient.get('/menu');
  },
  
  //Submit a new order
  createOrder(orderData) {
    return apiClient.post('/orders', orderData);
  },

  //Get all orders for staff dashboard
  getAllOrders() {
    return apiClient.get('/orders');
  },

  //Update order status
  updateOrderStatus(orderId, status) {
    return apiClient.patch(`/orders/${orderId}/status`, { status });
  },

  //Get orders by phone number
  getOrdersByPhone(phoneNumber) {
    // Adjust the endpoint as needed to match your backend
    return apiClient.get(`/orders?phone=${encodeURIComponent(phoneNumber)}`);
  }
};