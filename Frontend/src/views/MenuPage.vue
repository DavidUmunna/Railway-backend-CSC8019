<template>
  <div class="page-container">
    <div class="menu-top-row">
      <button class="back-home-btn" @click="$emit('navigateHome')">
        <ArrowLeft size="18" />
        Back to Home
      </button>
      <div class="location-tag">
        <MapPin size="18" />
        Cramlington Station Coffee Hut
      </div>
    </div>
    
    <div v-if="isLoading" class="loading-wrap">
      <div class="state-card loading-state">
        <Loader2 size="20" class="spin" />
        <span>Loading menu items...</span>
      </div>

      <div class="skeleton-list" aria-hidden="true">
        <div v-for="n in 5" :key="`menu-skeleton-${n}`" class="item-card skeleton-card">
          <div class="skeleton skeleton-text"></div>
          <div class="button-group">
            <div class="skeleton skeleton-btn"></div>
            <div class="skeleton skeleton-btn"></div>
          </div>
        </div>
      </div>
    </div>

    <div v-else-if="error" class="state-card error-state">
      <AlertCircle size="20" />
      <span>{{ error }}</span>
    </div>

    <div v-else class="menu-list">
      <div v-for="item in menuItems" :key="item.id" class="item-card">
        <span class="item-name">{{ item.name }}</span>
        
        <div class="button-group">
          <button @click="$emit('addToCart', item, 'Regular')" class="price-btn" :title="`Add ${item.name} Regular`">
            <Plus size="14" />
            <span class="size">Reg</span>
            <span class="price">£{{ item.regPrice.toFixed(2) }}</span>
          </button>
          
          <button v-if="item.largePrice" @click="$emit('addToCart', item, 'Large')" class="price-btn" :title="`Add ${item.name} Large`">
            <Plus size="14" />
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
defineProps(['menuItems', 'isLoading', 'error'])
defineEmits(['addToCart', 'navigateHome'])
</script>

<style scoped>
.page-container { padding: 20px 0; }
.menu-top-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
}

.back-home-btn {
  border: 1px solid rgba(141, 110, 99, 0.35);
  background: white;
  color: #3e2723;
  border-radius: 999px;
  padding: 10px 18px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.9rem;
  font-weight: 700;
}

.back-home-btn:hover {
  background: #fff3e0;
  border-color: #ffbf00;
}

.location-tag {
  text-align: center;
  color: #8d6e63;
  margin-bottom: 0;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.state-card {
  background: white;
  border-radius: 12px;
  padding: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.loading-wrap {
  display: grid;
  gap: 12px;
}

.skeleton-list {
  display: grid;
  gap: 12px;
}

.skeleton-card {
  pointer-events: none;
}

.skeleton {
  border-radius: 10px;
  background: linear-gradient(90deg, #efe7e1 25%, #f7f2ee 50%, #efe7e1 75%);
  background-size: 200% 100%;
  animation: shimmer 1.1s linear infinite;
}

.skeleton-text {
  height: 18px;
  width: 180px;
}

.skeleton-btn {
  height: 36px;
  width: 110px;
}

.loading-state {
  color: #6d4c41;
}

.error-state {
  color: #b23b3b;
}

.spin {
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

@keyframes shimmer {
  from { background-position: 200% 0; }
  to { background-position: -200% 0; }
}

.item-card {
  background: white;
  margin-bottom: 12px;
  padding: 20px;
  border-radius: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.item-name { font-weight: 700; color: #3e2723; }
.button-group { display: flex; gap: 10px; }

.price-btn {
  border: 1.5px solid #d7ccc8;
  background: #fdfaf9;
  border-radius: 10px;
  padding: 10px 16px;
  cursor: pointer;
  transition: 0.2s;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.85rem;
}

.price-btn:hover {
  background: #fff3e0;
  border-color: #ffab40;
}

.price-btn:active { transform: scale(0.95); }

.size { display: block; font-size: 0.7rem; color: #a1887f; }
.price { font-weight: 800; }

@media (max-width: 768px) {
  .menu-top-row {
    flex-direction: column;
    align-items: stretch;
  }

  .location-tag {
    justify-content: flex-start;
  }

  .item-card {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
    padding: 16px;
  }

  .button-group {
    width: 100%;
    flex-wrap: wrap;
  }

  .price-btn {
    flex: 1 1 140px;
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .page-container {
    padding: 14px 0;
  }

  .back-home-btn {
    width: 100%;
    justify-content: center;
  }
}
</style>