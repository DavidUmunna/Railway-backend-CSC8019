<template>
  <div class="about-page">
    <!-- Header Section -->
    <div class="header-section">
      <div class="nav-header">
        <div class="logo-text"><Coffee size="22" /> whistlestop</div>
        <nav class="nav-links">
          <a href="#" class="nav-link">CATALOG</a>
          <a href="#" class="nav-link">SHOP</a>
          <a href="#" class="nav-link">CONTACT</a>
          <a href="#" class="nav-link active">ABOUT US</a>
        </nav>
      </div>

      <h1 class="header-title">ABOUT US</h1>
    </div>

    <!-- About Content -->
    <div class="about-content">
      <p class="about-intro">
        whistlestop coffee hut is the station-side coffee spot at Cramlington Station, where passion for coffee meets warm hospitality. 
        Since opening, we've been committed to serving exceptional coffee with attention to detail and care for our customers. 
        We source quality beans from trusted suppliers and take pride in our craft, ensuring every cup is crafted to perfection. 
        Our values center on community, sustainability, and delivering an unforgettable coffee experience to our neighbors.
      </p>
    </div>

    <!-- Story Section -->
    <div class="story-section">
      <div class="story-image-container">
        <img 
          alt="whistlestop coffee hut interior" 
          src="https://images.unsplash.com/photo-1453614512568-c4024d13c247?w=500&q=80"
          class="story-image"
        />
      </div>

      <div class="story-text">
        <h2 class="story-title">whistlestop<br/>coffee hut</h2>
        
        <p class="story-paragraph">
          Welcome to whistlestop coffee hut, your local haven for exceptional coffee in a warm and inviting atmosphere. 
          As soon as you step through our door, you'll be greeted with the rich aroma of freshly prepared coffee and 
          the welcoming smiles of our dedicated team.
        </p>

        <p class="story-paragraph">
          Our baristas are passionate professionals who take pride in perfecting their craft. We partner with quality 
          suppliers to bring you consistently excellent coffee, carefully selected and responsibly sourced. We believe 
          that coffee is more than just a beverage—it's a moment of connection and comfort.
        </p>

        <p class="story-paragraph">
          That's why we're dedicated to creating the perfect cup for every customer. Whether you're looking for your 
          go-to morning espresso or a leisurely afternoon pour-over, our skilled team is here to craft it just right for you. 
          Come in, relax, and be part of the whistlestop family.
        </p>
      </div>
    </div>

    <!-- Footer -->
    <footer class="footer">
      <div class="footer-logo">whistlestop</div>
      
      <div class="footer-content">
        <div class="footer-column">
          <h3>ABOUT US</h3>
          <ul>
            <li><a href="#">Our Story</a></li>
            <li><a href="#">Our Services</a></li>
            <li><a href="#">Vacancies</a></li>
            <li><a href="#">Partner Interviews</a></li>
          </ul>
        </div>

        <div class="footer-column">
          <h3>SUPPORT</h3>
          <ul>
            <li><a href="#">Contact Us</a></li>
            <li><a href="#">FAQs</a></li>
            <li><a href="#">Where to buy</a></li>
            <li><a href="#">Shipping & Returns</a></li>
          </ul>
        </div>

        <div class="footer-social">
          <h3>FOLLOW US ON SOCIAL MEDIA</h3>
          <div class="social-links">
            <a href="#"><Instagram size="14" /></a>
            <a href="#"><Facebook size="14" /></a>
            <a href="#"><Twitter size="14" /></a>
          </div>
          <p class="contact-info">
            <Phone size="14" /> +(50) 7931 724 589<br/>
            <Mail size="14" /> info@whistlestopcoffeehut.com
          </p>
        </div>
      </div>

      <div class="footer-bottom">
        <p>&copy; 2024 whistlestop coffee hut. All Rights Reserved.</p>
        <div class="payment-icons">
          <img alt="PayPal" src="https://imgs.search.brave.com/paypal-icon" class="payment-icon" />
          <img alt="Visa" src="https://imgs.search.brave.com/visa-icon" class="payment-icon" />
          <img alt="Mastercard" src="https://imgs.search.brave.com/mastercard-icon" class="payment-icon" />
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
defineProps({
  // Component props if needed
});
</script>

<style scoped>
.about-page {
  width: 100%;
  background: #fdfaf8;
}

/* Header Section */
.header-section {
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
  padding: 40px 0;
  position: relative;
}

.nav-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 40px;
  max-width: 1440px;
  margin: 0 auto;
  gap: 30px;
}

.logo-text {
  font-size: 18px;
  font-weight: 700;
  letter-spacing: 1px;
  color: #ffbf00;
  text-transform: uppercase;
  flex-shrink: 0;
}

.nav-links {
  display: flex;
  gap: 30px;
  flex: 1;
  justify-content: center;
}

.nav-link {
  color: white;
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.5px;
  transition: color 0.2s ease;
  position: relative;
}

.nav-link:hover,
.nav-link.active {
  color: #ffbf00;
}

.nav-link.active::after {
  content: '';
  position: absolute;
  bottom: -5px;
  left: 0;
  width: 100%;
  height: 2px;
  background: #ffbf00;
}



.header-title {
  text-align: center;
  font-size: 48px;
  font-weight: 900;
  color: #ffbf00;
  margin: 40px 0 0 0;
  letter-spacing: 2px;
}

/* About Content */
.about-content {
  padding: 60px 40px;
  max-width: 1000px;
  margin: 0 auto;
  background: white;
}

.about-intro {
  font-size: 16px;
  line-height: 1.8;
  color: #333;
  text-align: center;
  margin: 0;
}

/* Story Section */
.story-section {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  padding: 80px 40px;
  max-width: 1400px;
  margin: 0 auto;
  align-items: center;
}

.story-image-container {
  overflow: hidden;
  border-radius: 8px;
  aspect-ratio: 4/5;
}

.story-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.story-text {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.story-title {
  font-size: 36px;
  font-weight: 900;
  color: #1a1a1a;
  margin: 0;
  letter-spacing: 2px;
  line-height: 1.2;
}

.story-paragraph {
  font-size: 15px;
  line-height: 1.8;
  color: #555;
  margin: 0;
}

/* Footer */
.footer {
  background: #1a1a1a;
  color: white;
  padding: 60px 40px;
}

.footer-logo {
  text-align: center;
  font-size: 48px;
  margin-bottom: 40px;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 40px;
  max-width: 1400px;
  margin: 0 auto 40px;
  padding-bottom: 40px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.footer-column h3,
.footer-social h3 {
  font-size: 14px;
  font-weight: 700;
  letter-spacing: 1px;
  margin: 0 0 20px 0;
  text-transform: uppercase;
  color: #ffbf00;
}

.footer-column ul {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.footer-column a {
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  font-size: 13px;
  transition: color 0.2s ease;
}

.footer-column a:hover {
  color: #ffbf00;
}

.footer-social {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.social-links {
  display: flex;
  gap: 16px;
}

.social-links a {
  display: inline-block;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 191, 0, 0.1);
  border-radius: 50%;
  color: #ffbf00;
  font-size: 14px;
  text-decoration: none;
  transition: all 0.2s ease;
}

.social-links a:hover {
  background: #ffbf00;
  color: #1a1a1a;
}

.contact-info {
  font-size: 13px;
  line-height: 1.6;
  color: rgba(255, 255, 255, 0.7);
  margin: 0;
}

.footer-bottom {
  text-align: center;
  padding-top: 40px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1400px;
  margin: 0 auto;
}

.footer-bottom p {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
  margin: 0;
}

.payment-icons {
  display: flex;
  gap: 12px;
}

.payment-icon {
  width: 24px;
  height: 24px;
  opacity: 0.5;
  transition: opacity 0.2s ease;
}

/* Responsive */
@media (max-width: 768px) {
  .nav-header {
    flex-wrap: wrap;
    padding: 20px;
    gap: 20px;
  }

  .nav-links {
    order: 3;
    width: 100%;
    gap: 12px;
    font-size: 12px;
  }

  .header-title {
    font-size: 36px;
  }

  .story-section {
    grid-template-columns: 1fr;
    padding: 40px 20px;
    gap: 40px;
  }

  .story-image-container {
    aspect-ratio: 1;
  }

  .footer-content {
    grid-template-columns: 1fr;
    gap: 30px;
  }

  .footer-bottom {
    flex-direction: column;
    gap: 20px;
  }

  .about-content {
    padding: 36px 20px;
  }

  .footer {
    padding: 44px 20px;
  }
}

@media (max-width: 480px) {
  .header-section {
    padding: 20px 0;
  }

  .nav-header {
    padding: 14px;
    gap: 12px;
  }

  .logo-text {
    font-size: 14px;
  }

  .nav-links {
    justify-content: flex-start;
    overflow-x: auto;
    white-space: nowrap;
    padding-bottom: 4px;
  }

  .header-title {
    font-size: 28px;
    margin-top: 20px;
    letter-spacing: 1px;
  }

  .about-intro,
  .story-paragraph {
    font-size: 14px;
    line-height: 1.65;
  }

  .story-title {
    font-size: 28px;
    letter-spacing: 1px;
  }

  .footer-logo {
    font-size: 32px;
    margin-bottom: 24px;
  }

  .footer-bottom {
    align-items: flex-start;
    text-align: left;
  }
}
</style>
