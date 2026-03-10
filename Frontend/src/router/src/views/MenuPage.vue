<template>
  <div class="menu-page">
    <div v-for="item in menuData" :key="item.id" class="coffee-card">
      
      <div class="info">
        <h3>{{ item.name }}</h3>
        <p>
          Regular: £{{ item.regularPrice.toFixed(2) }}
          
          <template v-if="item.largePrice">
             | Large: £{{ item.largePrice.toFixed(2) }}
          </template>
        </p>
      </div>

      <div class="actions">
        <button @click="handleAdd(item, 'Regular')">Add Reg</button>
        
        <button v-if="item.largePrice" @click="handleAdd(item, 'Large')">
          Add Large
        </button>
      </div>
      
    </div>
  </div>
</template>

<script setup>
import { useCartStore } from '../store/cart';

/**
 * Mock data representing items from the backend.
 * Note: 'largePrice' can be null for items that don't support a large size (e.g., Mineral Water).
 */
// src/views/MenuPage.vue (在 <script setup> 中)

const menuData = [
  { id: 1, name: 'Americano', regularPrice: 1.50, largePrice: 2.00 },
  { id: 2, name: 'Americano with milk', regularPrice: 2.00, largePrice: 2.50 },
  { id: 3, name: 'Latte', regularPrice: 2.50, largePrice: 3.00 },
  { id: 4, name: 'Cappuccino', regularPrice: 2.50, largePrice: 3.00 },
  { id: 5, name: 'Hot Chocolate', regularPrice: 2.00, largePrice: 2.50 },
  { id: 6, name: 'Mocha', regularPrice: 2.50, largePrice: 3.00 },
  { id: 7, name: 'Mineral Water', regularPrice: 1.00, largePrice: null }
];

// Initialize Pinia store to handle cart state
const cart = useCartStore();

/**
 * Handles adding an item to the cart.
 * @param {Object} item - The product being added.
 * @param {String} size - The selected size ('Regular' or 'Large').
 */
const handleAdd = (item, size) => {
  cart.addToCart(item, size);
};
</script>

<style scoped>
.coffee-card { 
  display: flex; 
  justify-content: space-between; 
  padding: 15px; 
  border-bottom: 1px solid #eee; 
}

.actions button { 
  margin-left: 5px; 
  background: #6F4E37; 
  color: white; 
  border: none; 
  padding: 5px 10px; 
  border-radius: 4px; 
  cursor: pointer;
}

.actions button:hover {
  background: #503928;
}
</style>