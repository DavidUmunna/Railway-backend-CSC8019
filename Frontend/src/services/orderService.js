import apiClient from './apiClient.js';

class OrderService {
  async createOrder(orderData) {
    try {
      const response = await apiClient.post('/api/v1/orders', orderData);
      return response;
    } catch (error) {
      throw new Error(error.message || 'Unable to create your order right now.');
    }
  }

  async getOrderById(id) {
    try {
      return await apiClient.get(`/api/v1/orders/${id}`);
    } catch (error) {
      throw new Error(error.message || 'Unable to load this order right now.');
    }
  }

  async getAllOrders() {
    try {
      const orders = await apiClient.get('/api/v1/orders');
      return orders.map(order => ({
        id: order.orderId?.toString() || '',
        summary: `Pickup at ${order.pickupTime || '--:--'}`,
        arrivalTime: order.pickupTime || '--:--',
        status: order.orderStatus || 'ACCEPTED',
        orderDate: order.orderDate || null,
        pickupTime: order.pickupTime || null,
        totalAmount: Number(order.totalAmount || 0)
      }));
    } catch (error) {
      console.error('Failed to fetch orders:', error);
      throw new Error(error.message || 'Unable to load orders right now.');
    }
  }

  async updateOrderStatus(orderId, status) {
    try {
      return await apiClient.put(`/api/v1/orders/${orderId}/status`, { orderStatus: status });
    } catch (error) {
      throw new Error(error.message || 'Unable to update order status right now.');
    }
  }
}

export default new OrderService();