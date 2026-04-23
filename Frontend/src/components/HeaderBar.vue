<template>
  <header class="header-bar">
    <div class="header-content">
      <span class="logo-icon">☕</span>
      <h1>Whistlestop Coffee Hut</h1>
    </div>
    <div class="station-tag">Cramlington Station</div>
  </header>
</template>

<style scoped>
.header-bar {
  width: 100%;
  background-color: #3e2723;
  color: #f4ece2;
  position: sticky;
  top: 0;
  z-index: 1000;
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
}

.header-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 15px 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.logo-icon {
  font-size: 1.5rem;
}

h1 {
  font-size: 1.3rem;
  margin: 0;
  letter-spacing: 1px;
  font-weight: 700;
}

.station-tag {
  text-align: center;
  font-size: 0.85rem;
  opacity: 0.9;
  padding: 8px 0;
  border-top: 1px solid rgba(244, 236, 226, 0.2);
}
</style>