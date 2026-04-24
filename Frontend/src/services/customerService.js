import apiClient from './apiClient.js';

class CustomerService {
  async createCustomer(customerData) {
    try {
      return await apiClient.post('/api/v1/customers/create', customerData);
    } catch (error) {
      throw new Error(error.message || 'Unable to save customer details right now.');
    }
  }

  async deleteCustomer(customerId) {
    try {
      return await apiClient.delete(`/api/v1/customers/${customerId}`);
    } catch (error) {
      throw new Error(error.message || 'Unable to remove customer details right now.');
    }
  }
}

export default new CustomerService();
