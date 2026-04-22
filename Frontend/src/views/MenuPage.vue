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

//Menu data 
const menuData = [
  { id: 1, name: 'Americano', regularPrice: 1.50, largePrice: 2.00 },
  { id: 2, name: 'Americano with milk', regularPrice: 2.00, largePrice: 2.50 },
  { id: 3, name: 'Latte', regularPrice: 2.50, largePrice: 3.00 },
  { id: 4, name: 'Cappuccino', regularPrice: 2.50, largePrice: 3.00 },
  { id: 5, name: 'Hot Chocolate', regularPrice: 2.00, largePrice: 2.50 },
  { id: 6, name: 'Mocha', regularPrice: 2.50, largePrice: 3.00 },
  { id: 7, name: 'Mineral Water', regularPrice: 1.00, largePrice: null }
];

//Initialize Pinia store to manage cart state
const cart = useCartStore();

//Handles the 'Add to Cart' action.
const handleAdd = (item, size) => {
  cart.addToCart(item, size);
};
</script>

<style scoped>
/* Base container style */
.coffee-card { 
  display: flex; 
  justify-content: space-between; 
  align-items: center; 
  padding: 15px; 
  border-bottom: 1px solid #eee; 
}

.actions {
  display: flex;
  gap: 8px;
}

.actions button { 
  background: #6F4E37; 
  color: white; 
  border: none; 
  padding: 8px 12px; 
  border-radius: 4px; 
  cursor: pointer;
  white-space: nowrap;
}

.actions button:hover {
  background: #503928;
}

/* Responsive layout */
@media (max-width: 480px) {
  .coffee-card {
    flex-direction: column; 
    align-items: flex-start; 
    gap: 10px;
  }

  .actions {
    width: 100%;
    justify-content: flex-start;
  }
}
</style>