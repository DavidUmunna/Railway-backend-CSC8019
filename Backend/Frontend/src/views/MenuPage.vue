<template>
  <div class="menu-page">
    <div class="menu-header">
      <div class="menu-header-top">
        <button class="home-btn" @click="$emit('navigateHome')" aria-label="Back to home">
          <ArrowLeft size="18" />
          <span>Home</span>
        </button>

        <div class="station-status" aria-live="polite">
          <template v-if="stationName">
            <p class="station-name">{{ stationName }}</p>
            <p class="station-hours">
              <span class="status-pill" :class="{ closed: !isOpenNow }">{{ isOpenNow ? 'Open now' : 'Closed' }}</span>
              <span>{{ todaysHoursText }}</span>
            </p>
          </template>
          <template v-else>
            <p class="station-name">Station info unavailable</p>
            <p class="station-hours">{{ stationError || 'Unable to load opening hours.' }}</p>
          </template>
        </div>
      </div>

      <h2>Our Menu</h2>
      <p>Choose your favorite brew and size.</p>
    </div>

    <div v-if="isLoading" class="loading-wrap">
      <Loader2 size="30" class="spin" />
      <p>Brewing your menu...</p>
    </div>

    <div v-else-if="error" class="error-wrap">
      <AlertTriangle size="24" />
      <p>{{ error }}</p>
    </div>

    <div v-else class="menu-grid">
      <div v-for="item in menuItems" :key="item.id" class="menu-card">
        <h3>{{ item.name }}</h3>
        <p v-if="item.description" class="menu-desc">{{ item.description }}</p>
        <p class="price">Regular: £{{ Number(item.regPrice || 0).toFixed(2) }}</p>
        <p class="price">Large: £{{ Number(item.largePrice || item.regPrice || 0).toFixed(2) }}</p>

        <div class="actions">
          <button @click="addItem(item, 'Regular')">Add Regular</button>
          <button @click="addItem(item, 'Large')">Add Large</button>
        </div>
      </div>
    
    </div>
  </div>
</template>

<script setup>
import { computed, onBeforeUnmount, onMounted, ref } from 'vue';
import stationService from '../services/stationService.js';

const props = defineProps({
  menuItems: { type: Array, default: () => [] },
  isLoading: { type: Boolean, default: false },
  error: { type: String, default: '' },
});

const emit = defineEmits(['add', 'navigateHome']);

const station = ref(null);
const stationError = ref('');
const now = ref(new Date());
let clockTimer = null;

const parseHourRange = (rangeText) => {
  if (!rangeText || !rangeText.includes('-')) {
    return null;
  }

  const [openText, closeText] = rangeText.split('-').map((part) => part.trim());
  if (!openText || !closeText) {
    return null;
  }

  const [openHour, openMinute] = openText.split(':').map(Number);
  const [closeHour, closeMinute] = closeText.split(':').map(Number);

  if ([openHour, openMinute, closeHour, closeMinute].some((v) => Number.isNaN(v))) {
    return null;
  }

  return {
    openMinutes: openHour * 60 + openMinute,
    closeMinutes: closeHour * 60 + closeMinute,
  };
};

const todaysHoursRaw = computed(() => {
  if (!station.value) {
    return '';
  }

  const day = now.value.getDay();
  if (day === 0 && station.value.closedOnSunday) {
    return 'Closed';
  }

  return day === 6 ? station.value.saturdayOpeningHours : station.value.weekdayOpeningHours;
});

const stationName = computed(() => station.value?.name || '');

const todaysHoursText = computed(() => {
  if (!todaysHoursRaw.value) {
    return 'Hours unavailable';
  }
  return todaysHoursRaw.value;
});

const isOpenNow = computed(() => {
  if (!station.value) {
    return false;
  }

  const day = now.value.getDay();
  if (day === 0 && station.value.closedOnSunday) {
    return false;
  }

  const parsed = parseHourRange(todaysHoursRaw.value);
  if (!parsed) {
    return false;
  }

  const minutesNow = now.value.getHours() * 60 + now.value.getMinutes();
  return minutesNow >= parsed.openMinutes && minutesNow <= parsed.closeMinutes;
});

onMounted(async () => {
  try {
    const stations = await stationService.getAllStations();
    station.value = Array.isArray(stations) && stations.length > 0 ? stations[0] : null;
    if (!station.value) {
      stationError.value = 'No station data found.';
    }
  } catch (error) {
    stationError.value = error.message || 'Unable to load station information right now.';
  }

  clockTimer = setInterval(() => {
    now.value = new Date();
  }, 30000);
});

onBeforeUnmount(() => {
  if (clockTimer) {
    clearInterval(clockTimer);
  }
});


const addItem = (item, size) => {
  emit('add', { item, size });
};

</script>

<style scoped>
.menu-page {
  max-width: 980px;
  margin: 0 auto;
  min-height: calc(100vh - 160px);
  padding: 26px 16px 24px;
  background: rgba(253, 250, 248, 0.82);
  border-radius: 18px;
  box-shadow: 0 14px 36px rgba(45, 24, 16, 0.08);
}

.menu-header {
  margin-bottom: 22px;
}

.menu-header-top {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 10px;
}

.home-btn {
  border: 1px solid #e2d4ca;
  background: #fff;
  color: #5d4037;
  border-radius: 999px;
  padding: 6px 12px;
  display: inline-flex;
  gap: 6px;
  align-items: center;
  cursor: pointer;
}
.menu-desc {
      font-size: 0.98em;
      color: #5d4037;
      margin: 6px 0 8px 0;
    }

.station-status {
  background: rgba(255, 255, 255, 0.94);
  border: 1px solid #eadccf;
  border-radius: 12px;
  padding: 8px 10px;
  min-width: 220px;
  text-align: right;
}

.station-name {
  margin: 0;
  color: #3e2723;
  font-weight: 700;
  line-height: 1.2;
}

.station-hours {
  margin: 4px 0 0;
  color: #6f5b52;
  font-size: 0.9rem;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.status-pill {
  display: inline-flex;
  align-items: center;
  border-radius: 999px;
  padding: 2px 8px;
  font-size: 0.74rem;
  font-weight: 700;
  color: #0f5132;
  background: #d1e7dd;
}

.status-pill.closed {
  color: #842029;
  background: #f8d7da;
}

.menu-header h2 {
  margin: 0;
  color: #3e2723;
}

.menu-header p {
  margin: 6px 0 0;
  color: #6e5a50;
}

.menu-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 16px;
}

.menu-card {
  background: #fff;
  border: 1px solid #f0e5dd;
  border-radius: 14px;
  padding: 18px;
  box-shadow: 0 6px 14px rgba(45, 24, 16, 0.06);
  display: flex;
  flex-direction: column;
}

.menu-card h3 {
  margin: 0 0 8px;
  color: #3e2723;
}

.price {
  margin: 3px 0;
  color: #6f5b52;
}

.actions {
  margin-top: auto;
  padding-top: 10px;
  display: flex;
  gap: 8px;
  width: 100%;
}

.actions button {
  flex: 1;
  border: none;
  border-radius: 9px;
  padding: 8px;
  font-size: 0.9rem;
  cursor: pointer;
  background: #3e2723;
  color: #fff;
}

.actions button:hover {
  background: #5d4037;
}

.loading-wrap,
.error-wrap {
  display: grid;
  place-items: center;
  gap: 10px;
  color: #5d4037;
  padding: 40px 0;
}

.spin {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 768px) {
  .menu-page {
    min-height: calc(100vh - 150px);
    border-radius: 16px 16px 0 0;
    padding-bottom: 32px;
  }

  .menu-header-top {
    flex-direction: column;
    align-items: stretch;
  }

  .station-status {
    text-align: left;
  }
}
</style>
