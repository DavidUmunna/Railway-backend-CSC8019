<template>
  <div class="home-page">
    <!-- Hero Section -->
    <div class="hero-section">
      <div class="hero-bg">
        <img 
          alt="whistlestop coffee hut" 
          src="https://images.unsplash.com/photo-1511920170033-f8396924c348?w=1440&q=80" 
          class="hero-image"
        />
        <div class="hero-overlay"></div>
      </div>
      
      <div class="hero-content">
        <Coffee class="hero-icon" size="56" />
        <h1 class="hero-title">whistlestop<br/>coffee hut</h1>
        <p class="hero-subtitle">Your Cramlington Station Coffee Hut</p>
        
        <p class="hero-description">
          Savor expertly crafted coffee in a welcoming station-side atmosphere. 
          Come by Cramlington Station for the perfect brew, every time.
        </p>
        
        <div class="hero-actions">
          <button class="order-now-btn" @click="handleOrderNow">
            <span>Order Now</span>
            <ArrowRight class="button-icon" size="18" />
          </button>
        </div>
      </div>
    </div>

    <!-- Features Section -->
    <div class="features-section">
      <div class="feature-card">
        <Coffee class="feature-icon" size="28" />
        <h3>Sourced Daily</h3>
        <p>Fresh beans sourced from the finest suppliers</p>
      </div>
      <div class="feature-card">
        <Star class="feature-icon" size="28" />
        <h3>Expert Craft</h3>
        <p>Our baristas bring years of experience</p>
      </div>
      <div class="feature-card">
        <Heart class="feature-icon" size="28" />
        <h3>Local Love</h3>
        <p>Proudly serving our community since day one</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineEmits } from 'vue';

const emit = defineEmits(['navigate']);

const handleOrderNow = () => {
  emit('navigate', 'menu');
};
</script>

<style scoped>
.home-page {
  width: 100%;
  background: #fdfaf8;
}

/* Hero Section */
.hero-section {
  position: relative;
  height: 100vh;
  min-height: 600px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.hero-bg {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
}

.hero-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.hero-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    135deg,
    rgba(0, 0, 0, 0.7) 0%,
    rgba(0, 0, 0, 0.5) 100%
  );
}

.hero-content {
  position: relative;
  z-index: 10;
  text-align: center;
  color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
  padding: 40px 20px;
  max-width: 600px;
}



.hero-icon {
  color: #ffbf00;
  margin-bottom: 16px;
}

.hero-actions {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
  justify-content: center;
}

.hero-title {
  font-size: 72px;
  font-weight: 900;
  margin: 0;
  letter-spacing: 4px;
  color: #ffbf00;
  text-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.button-icon {
  margin-left: 10px;
}

.hero-subtitle {
  font-size: 28px;
  font-weight: 300;
  margin: 0;
  letter-spacing: 2px;
  color: #f5f5f5;
}

.hero-description {
  font-size: 16px;
  line-height: 1.6;
  margin: 20px 0 0 0;
  color: #e0e0e0;
  max-width: 400px;
}

.order-now-btn {
  margin-top: 40px;
  padding: 16px 48px;
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 1px;
  color: #000;
  background: linear-gradient(135deg, #ffbf00 0%, #ffa500 100%);
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
  text-transform: uppercase;
}

.order-now-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.4);
  background: linear-gradient(135deg, #ffa500 0%, #ff9500 100%);
}

.order-now-btn:active {
  transform: translateY(-1px);
}

/* Features Section */
.features-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 40px;
  padding: 80px 40px;
  max-width: 1200px;
  margin: 0 auto;
}

.feature-card {
  text-align: center;
  padding: 40px 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
}

.feature-icon {
  color: #3e2723;
  margin-bottom: 16px;
}

.feature-card h3 {
  font-size: 20px;
  font-weight: 700;
  color: #1a1a1a;
  margin: 12px 0 8px 0;
}

.feature-card p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin: 0;
}

/* Responsive */
@media (max-width: 768px) {
  .hero-section {
    min-height: 72vh;
    height: auto;
  }

  .hero-title {
    font-size: 48px;
  }

  .hero-subtitle {
    font-size: 20px;
  }

  .hero-description {
    font-size: 14px;
  }

  .order-now-btn {
    padding: 14px 36px;
    font-size: 16px;
  }

  .features-section {
    grid-template-columns: 1fr;
    padding: 40px 20px;
    gap: 24px;
  }
}

@media (max-width: 480px) {
  .hero-content {
    gap: 14px;
    padding: 28px 14px;
  }

  .hero-title {
    font-size: 34px;
    letter-spacing: 1.2px;
    line-height: 1.05;
  }

  .hero-subtitle {
    font-size: 16px;
    letter-spacing: 0.8px;
  }

  .hero-description {
    max-width: 100%;
    font-size: 13px;
  }

  .order-now-btn {
    width: 100%;
    margin-top: 24px;
    padding: 14px 20px;
    font-size: 14px;
  }
}
</style>
