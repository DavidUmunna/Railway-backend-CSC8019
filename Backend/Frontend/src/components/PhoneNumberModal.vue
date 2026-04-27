<template>
  <div class="modal-overlay">
    <div class="modal-content">
      <button class="modal-close" @click="$emit('close')" aria-label="Close">×</button>
      <h3>Enter Your Phone Number To View Your Orders</h3>
      <input
        v-model="localPhoneNumber"
        type="tel"
        placeholder="Phone Number"
        maxlength="15"
        class="phone-input"
        @keyup.enter="submit"
      />
      <div class="modal-actions">
        <button class="feature-btn" @click="submit" :disabled="isLoading || !localPhoneNumber">Submit</button>
      </div>
      <p v-if="modalError" class="modal-error">{{ modalError }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
const props = defineProps({
  show: Boolean,
  isLoading: Boolean,
  error: String,
  phoneNumber: String
});
const emit = defineEmits(['submit', 'update:phoneNumber']);

const localPhoneNumber = ref(props.phoneNumber || '');
const modalError = ref('');

watch(() => props.phoneNumber, (val) => {
  localPhoneNumber.value = val;
});

const submit = () => {
  modalError.value = '';
  if (!localPhoneNumber.value) {
    modalError.value = 'Please enter your phone number.';
    return;
  }
  emit('submit', localPhoneNumber.value);
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-content {
  background: #fff;
  padding: 32px 24px;
  border-radius: 8px;
  box-shadow: 0 2px 16px rgba(0,0,0,0.15);
  min-width: 320px;
  max-width: 90vw;
  text-align: center;
}
.phone-input {
  width: 100%;
  padding: 8px 12px;
  margin: 16px 0;
  font-size: 1.1em;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.modal-actions {
  margin-top: 12px;
}
.modal-actions .feature-btn {
  padding: 8px 20px;
  font-size: 1em;
  border: none;
  border-radius: 8px;
  background: #3e2723;
  color: #fff;
  cursor: pointer;
  font-weight: 600;
  transition: background 0.2s;
}
.modal-actions .feature-btn:disabled {
  background: #aaa;
  cursor: not-allowed;
}

.modal-close {
  position: absolute;
  top: 12px;
  right: 16px;
  background: transparent;
  border: none;
  font-size: 1.3rem;
  color: #3e2723;
  cursor: pointer;
  z-index: 10;
  line-height: 1;
  padding: 0;
}
.modal-content {
  position: relative;
}
.modal-error {
  color: #d32f2f;
  margin-top: 8px;
}
</style>
