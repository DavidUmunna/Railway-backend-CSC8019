# Whistlestop Coffee Frontend

Vue 3 + Vite frontend for the CSC8019 coffee ordering project.

## Features
- Menu browsing with size-based pricing
- Cart management with grouped items and quantity-aware checkout
- Order history view
- Staff login and order operations dashboard
- Pinia store for cart and user state

## Run Locally
1. Install dependencies:
   npm install
2. Start the dev server:
   npm run dev
3. Open the URL shown in terminal (typically http://localhost:5173)

## Structure
- src/App.vue: app orchestration and navigation
- src/views: page-level views (menu, cart, orders, staff)
- src/components: reusable UI parts
- src/stores: Pinia stores
- src/services: API access layer
