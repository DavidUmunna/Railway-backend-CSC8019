// Entry file of the Vue application
// This file mounts the root component (App.vue) to the DOM

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import './styles/light-animations.css' // Import animation styles
import * as LucideVue from 'lucide-vue-next'

// Create Vue app instance
const app = createApp(App)

// Initialize Pinia for state management
app.use(createPinia())

// Register all Lucide icons globally
Object.entries(LucideVue).forEach(([name, component]) => {
  app.component(name, component)
})

app.mount('#app')
