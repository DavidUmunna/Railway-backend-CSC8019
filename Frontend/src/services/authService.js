import apiClient from './apiClient.js';

class AuthService {
  async login(credentials) {
    try {
      const response = await apiClient.post('/api/v1/auth/login', credentials);
      if (response.token) {
        apiClient.setAuthToken(response.token);
      }
      return response;
    } catch (error) {
      throw new Error(error.message || 'Unable to sign in right now.');
    }
  }

  logout() {
    apiClient.clearAuthToken();
  }

  isAuthenticated() {
    return !!apiClient.getAuthToken();
  }
}

export default new AuthService();