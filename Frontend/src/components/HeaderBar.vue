<template>
  <header class="header-bar">
    <div class="header-content">
      <img src="https://cdn-icons-png.flaticon.com/512/924/924514.png" alt="logo" class="logo">
      <h1>Whistlestop Coffee</h1>
    </div>
  </header>
</template>

<style scoped>
.header-bar {
  width: 100%;
  background-color: #3e2723; /* Dark coffee brown */
  color: white;
  position: sticky;
  top: 0;
  z-index: 1000;
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
}

.header-content {
  max-width: 800px; /* Restrict width for better readability */
  margin: 0 auto;
  padding: 15px 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.logo {
  width: 30px;
  filter: brightness(0) invert(1); /* Turn black icon to white */
}

h1 {
  font-size: 1.3rem;
  margin: 0;
  letter-spacing: 1px;
}

@media (max-width: 768px) {
  .header-content {
    padding: 12px 14px;
    gap: 8px;
  }

  .logo {
    width: 24px;
  }

  h1 {
    font-size: 1rem;
    letter-spacing: 0.3px;
  }
}
</style>