import apiClient from './apiClient.js';

class StationService {
  async getAllStations() {
    try {
      return await apiClient.get('/api/v1/stations');
    } catch (error) {
      throw new Error(error.message || 'Unable to load station information right now.');
    }
  }
}

export default new StationService();
