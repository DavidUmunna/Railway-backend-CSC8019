import apiClient from './apiClient.js';

class MenuService {
  async getAllMenuItems() {
    try {
      const items = await apiClient.get('/api/v1/menu-items');
      return items.map(item => ({
        id: item.id,
        name: item.name,
        description: item.description,
        regPrice: this.getTypeBySize(item.types, 'REGULAR')?.price || 0,
        largePrice: this.getTypeBySize(item.types, 'LARGE')?.price || 0,
        regTypeId: this.getTypeBySize(item.types, 'REGULAR')?.menuItemTypeId || null,
        largeTypeId: this.getTypeBySize(item.types, 'LARGE')?.menuItemTypeId || null,
      }));
    } catch (error) {
      console.error('Failed to fetch menu items:', error);
      throw new Error(error.message || 'Unable to load menu items right now.');
    }
  }

  getTypeBySize(types = [], size = 'REGULAR') {
    if (!Array.isArray(types)) {
      return null;
    }

    return types.find(type =>
      String(type?.size || '').toUpperCase() === size
    );
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