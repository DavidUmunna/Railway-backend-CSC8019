const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

class ApiClient {
  constructor(baseURL = API_BASE_URL) {
    this.baseURL = baseURL;
    this.defaultHeaders = {
      'Content-Type': 'application/json',
    };
  }

  async request(endpoint, options = {}) {
    const url = `${this.baseURL}${endpoint}`;
    const config = {
      headers: { ...this.defaultHeaders, ...options.headers },
      ...options,
    };

    // Add auth token if available
    const token = this.getAuthToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    try {
      const response = await fetch(url, config);

      if (!response.ok) {
        const errorPayload = await this.parseErrorResponse(response);
        const userMessage = this.getFriendlyErrorMessage(response.status, errorPayload?.message);
        const requestError = new Error(userMessage);
        requestError.status = response.status;
        requestError.serverMessage = errorPayload?.message || null;
        requestError.endpoint = endpoint;
        throw requestError;
      }

      // Handle empty responses
      const contentType = response.headers.get('content-type');
      if (contentType && contentType.includes('application/json')) {
        return await response.json();
      }

      return response;
    } catch (error) {
      console.error('API request failed:', error);
      if (error.name === 'TypeError') {
        const networkError = new Error('Unable to reach the server. Please check your connection and try again.');
        networkError.status = 0;
        networkError.endpoint = endpoint;
        throw networkError;
      }
      throw error;
    }
  }

  async parseErrorResponse(response) {
    const contentType = response.headers.get('content-type') || '';
    if (contentType.includes('application/json')) {
      try {
        return await response.json();
      } catch {
        return null;
      }
    }

    try {
      const text = await response.text();
      return text ? { message: text } : null;
    } catch {
      return null;
    }
  }

  getFriendlyErrorMessage(status, serverMessage) {
    switch (status) {
      case 400:
        return serverMessage || 'Some details are invalid. Please review and try again.';
      case 401:
        return 'Your session has expired. Please sign in again.';
      case 403:
        return 'You do not have permission to perform this action.';
      case 404:
        return 'The requested resource was not found.';
      case 409:
        return serverMessage || 'This action conflicts with existing data.';
      case 422:
        return serverMessage || 'Some input values are not valid.';
      case 500:
      case 502:
      case 503:
      case 504:
        return 'The server is currently unavailable. Please try again shortly.';
      default:
        return serverMessage || 'Something went wrong. Please try again.';
    }
  }

  async get(endpoint, options = {}) {
    return this.request(endpoint, { ...options, method: 'GET' });
  }

  async post(endpoint, data, options = {}) {
    return this.request(endpoint, {
      ...options,
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  async put(endpoint, data, options = {}) {
    return this.request(endpoint, {
      ...options,
      method: 'PUT',
      body: JSON.stringify(data),
    });
  }

  async delete(endpoint, options = {}) {
    return this.request(endpoint, { ...options, method: 'DELETE' });
  }

  setAuthToken(token) {
    if (token) {
      localStorage.setItem('authToken', token);
    } else {
      localStorage.removeItem('authToken');
    }
  }

  getAuthToken() {
    return localStorage.getItem('authToken');
  }

  clearAuthToken() {
    localStorage.removeItem('authToken');
  }
}

export default new ApiClient();