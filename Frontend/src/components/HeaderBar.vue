<template>
  <header class="header-bar">
    <div class="logo-container">
      <span class="logo-icon">☕</span>
      <h1>Whistlestop Coffee Hut</h1>
    </div>
    <div class="station-tag">Cramlington Station</div>
  </header>
</template>

<style scoped>
.header-bar {
  background-color: #4a3728; /* Coffee brown */
  color: #f4ece2;
  padding: 1rem;
  text-align: center;
  box-shadow: 0 2px 5px rgba(0,0,0,0.2);
}
.logo-icon { font-size: 1.5rem; margin-right: 8px; }
h1 { font-size: 1.2rem; margin: 0; display: inline; font-weight: 700; }
.station-tag { font-size: 0.8rem; opacity: 0.8; margin-top: 4px; }
</style>