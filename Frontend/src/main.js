// Entry file of the Vue application
// This file mounts the root component (App.vue) to the DOM

import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import './style.css';
import './styles/light-animations.css';
import * as LucideVue from 'lucide-vue-next';

// Project Entry Point
const app = createApp(App);
app.use(createPinia());

Object.entries(LucideVue).forEach(([name, component]) => {
  app.component(name, component);
});

app.mount('#app');
