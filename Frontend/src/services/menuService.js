import apiClient from './apiClient.js';

class MenuService {
  async getAllMenuItems() {
    try {
      const items = await apiClient.get('/api/v1/menu-items');
      return items.map(item => ({
        id: item.id,
        name: item.name,
        regPrice: this.getPriceBySize(item.types, 'REGULAR'),
        largePrice: this.getPriceBySize(item.types, 'LARGE'),
      }));
    } catch (error) {
      console.error('Failed to fetch menu items:', error);
      throw new Error(error.message || 'Unable to load menu items right now.');
    }
  }

  getPriceBySize(types = [], size = 'REGULAR') {
    if (!Array.isArray(types)) {
      return 0;
    }

    const match = types.find(type =>
      String(type?.size || '').toUpperCase() === size
    );

    return typeof match?.price === 'number' ? match.price : 0;
  }

  async getMenuItemById(id) {
    try {
      return await apiClient.get(`/api/v1/menu-items/${id}`);
    } catch (error) {
      throw new Error(error.message || 'Unable to load this menu item right now.');
    }
  }
}

export default new MenuService();