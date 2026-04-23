<template>
  <div class="page-container">
    <div class="location-tag">Cramlington Station</div>
    
    <div class="menu-list">
      <div v-for="item in menuItems" :key="item.id" class="item-card">
        <div class="info">
          <span class="item-name">{{ item.name }}</span>
        </div>
        
        <div class="button-group">
          <button @click="$emit('addToCart', item, 'Regular')" class="price-btn">
            <span class="size">Reg</span>
            <span class="price">£{{ item.regPrice.toFixed(2) }}</span>
          </button>
          
          <button v-if="item.largePrice" @click="$emit('addToCart', item, 'Large')" class="price-btn">
            <span class="size">Large</span>
            <span class="price">£{{ item.largePrice.toFixed(2) }}</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// Props for menu data passed from main App state
defineProps({
  menuItems: {
    type: Array,
    required: true
  }
});

defineEmits(['addToCart']);
</script>

<style scoped>
.page-container { 
  padding: 20px 0; 
}

.location-tag { 
  text-align: center; 
  color: #8d6e63; 
  margin-bottom: 20px; 
  font-weight: bold; 
}

.menu-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.item-card {
  background: white;
  padding: 15px;
  border-radius: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  border-bottom: 1px solid #eee;
}

.info {
  flex: 1;
}

.item-name { 
  font-weight: 700; 
  color: #3e2723; 
  font-size: 1rem;
}

.button-group { 
  display: flex; 
  gap: 10px;
  flex-shrink: 0;
}

.price-btn {
  border: 1.5px solid #d7ccc8;
  background: #fdfaf9;
  border-radius: 10px;
  padding: 8px 12px;
  cursor: pointer;
  transition: 0.2s;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.price-btn:hover {
  background: #f5f1f0;
  border-color: #8d6e63;
}

.price-btn:active { 
  transform: scale(0.95); 
}

.size { 
  display: block; 
  font-size: 0.7rem; 
  color: #a1887f;
  font-weight: 600;
}

.price { 
  font-weight: 800;
  color: #6F4E37;
}

/* Responsive layout */
@media (max-width: 480px) {
  .item-card {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .button-group {
    width: 100%;
    justify-content: flex-start;
  }
}
</style>