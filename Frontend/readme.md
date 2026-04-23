Whistlestop Coffee - Frontend Prototype
CSC8019 Project | Vue 3 + Vite

A mobile-optimized web application designed for seamless coffee ordering at Cramlington Station. This prototype focuses on a fast, intuitive user experience for commuters.

Project Overview
This is the frontend implementation of the Whistlestop Coffee ordering system. It features a reactive menu, persistent shopping cart (via Pinia), and a dedicated dashboard for station staff to manage incoming orders.

Project Structure
The source code is organized as follows:

src/App.vue: The root component. Orchestrates high-level navigation and coordinates between different views.

src/components/: Houses reusable UI building blocks (e.g., HeaderBar.vue, BottomNav.vue).

src/views/: Contains the main page views:

MenuPage.vue: Browse and select coffee.

CartPage.vue: Manage selected items.

CheckoutPage.vue: Finalize and place orders.

OrdersPage.vue: User order history.

StaffDashboard.vue: Administrative interface.

src/store/: Centralized state management using Pinia .

src/assets/: Static assets including background image.

How to Run the Project
1. Prerequisites

Ensure you have Node.js installed on your machine.

2. Installation

Open your terminal in the project root folder and run:

npm install

3. Start Development Server

Launch the Vite development environment:

npm run dev

4. Access the App

Open your browser to the URL provided in the terminal:

Default: http://localhost:5173

🛠 Tech Stack

Framework: Vue 3 

Build Tool: Vite

State Management: Pinia

Styles: Scoped CSS with a focus on responsive layouts.
