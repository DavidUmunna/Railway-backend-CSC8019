// Entry file of the Vue application
// This file mounts the root component (App.vue) to the DOM

import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import './style.css';

//Project Entry Point
const app = createApp(App);
app.use(createPinia());
app.mount('#app');
