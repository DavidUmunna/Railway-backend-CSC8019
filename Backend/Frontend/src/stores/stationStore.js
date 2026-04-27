import { defineStore } from 'pinia';
import { computed, ref } from 'vue';
import stationService from '../services/stationService.js';

export const useStationStore = defineStore('station', () => {
  const stations = ref([]);
  const currentStationId = ref(null);
  const isLoading = ref(false);
  const stationError = ref('');

  const currentStation = computed(() => {
    if (!currentStationId.value) {
      return stations.value[0] || null;
    }

    return stations.value.find((station) => station.id === currentStationId.value) || stations.value[0] || null;
  });

  const loadStations = async () => {
    isLoading.value = true;
    stationError.value = '';

    try {
      const result = await stationService.getAllStations();
      stations.value = Array.isArray(result) ? result : [];

      if (stations.value.length > 0 && !currentStationId.value) {
        currentStationId.value = Number(stations.value[0].id);
      }
    } catch (error) {
      stationError.value = error.message || 'Unable to load station information right now.';
      throw error;
    } finally {
      isLoading.value = false;
    }
  };

  const setCurrentStationId = (stationId) => {
    currentStationId.value = Number(stationId);
  };

  return {
    stations,
    currentStationId,
    currentStation,
    isLoading,
    stationError,
    loadStations,
    setCurrentStationId,
  };
});
