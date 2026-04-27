<template>
  <div class="staff-login-page">
    <div class="login-shell">
      <div class="login-panel">
        <div class="brand-block">
          <div class="brand-mark"><User size="24" /></div>
          <div>
            <h1>Staff Login</h1>
            <p>Sign in to manage Cramlington Station orders.</p>
          </div>
        </div>

        <form @submit.prevent="submitLogin" class="login-form">
          <div class="field">
            <label>Email or Staff ID</label>
            <div class="input-wrap">
              <User size="18" />
              <input
                type="text"
                placeholder="e.g. staff@whistlestop.co"
                v-model="username"
              />
            </div>
          </div>

          <div class="field">
            <label>Password</label>
            <div class="input-wrap">
              <Lock size="18" />
              <input
                :type="showPassword ? 'text' : 'password'"
                placeholder="Enter password"
                v-model="password"
              />
              <button
                type="button"
                class="visibility-btn"
                :title="showPassword ? 'Hide password' : 'Show password'"
                :aria-label="showPassword ? 'Hide password' : 'Show password'"
                @click="togglePasswordVisibility"
              >
                <EyeOff v-if="showPassword" size="18" />
                <Eye v-else size="18" />
              </button>
            </div>
          </div>

          <p v-if="displayError" class="error-message">{{ displayError }}</p>

          <button class="sign-in-btn" type="submit" :disabled="isSubmitting">
            <Loader2 v-if="isSubmitting" size="18" class="spin" />
            <span>{{ isSubmitting ? 'Signing In...' : 'Sign In' }}</span>
            <ArrowRight v-if="!isSubmitting" size="18" />
          </button>
        </form>

        <button class="cancel-btn" @click="cancelLogin">
          Back to Store
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';

const props = defineProps({
  isSubmitting: {
    type: Boolean,
    default: false,
  },
  serverError: {
    type: String,
    default: '',
  },
});

const emit = defineEmits(['login', 'cancel']);

const username = ref('');
const password = ref('');
const showPassword = ref(false);
const errorMessage = ref('');
const displayError = computed(() => props.serverError || errorMessage.value);

const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value;
};

const submitLogin = () => {
  if (props.isSubmitting) {
    return;
  }

  if (!username.value || !password.value) {
    errorMessage.value = 'Please enter your email or ID and password.';
    return;
  }

  errorMessage.value = '';
  emit('login', {
    username: username.value,
    password: password.value,
    role: 'Staff',
    name: username.value.includes('@') ? username.value.split('@')[0] : username.value
  });
};

const cancelLogin = () => {
  emit('cancel');
};
</script>

<style scoped>
.staff-login-page {
  display: flex;
  min-height: 100vh;
  align-items: center;
  justify-content: center;
  background: linear-gradient(180deg, #f7f4f0 0%, #efe6dc 100%);
  padding: 40px 20px;
}

.login-shell {
  width: 100%;
  max-width: 520px;
}

.login-panel {
  background: white;
  border-radius: 28px;
  box-shadow: 0 24px 80px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.brand-block {
  padding: 32px 32px 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  border-bottom: 1px solid #f0ece7;
}

.brand-mark {
  width: 56px;
  height: 56px;
  border-radius: 18px;
  background: #ffbf00;
  display: grid;
  place-items: center;
  color: #3e2723;
}

.brand-block h1 {
  margin: 0 0 6px;
  font-size: 28px;
  letter-spacing: -0.03em;
}

.brand-block p {
  margin: 0;
  color: #6f5b52;
  line-height: 1.6;
}

.login-form {
  padding: 32px;
  display: grid;
  gap: 18px;
}

.field label {
  display: block;
  margin-bottom: 10px;
  font-size: 0.9rem;
  color: #867169;
}

.input-wrap {
  display: flex;
  align-items: center;
  gap: 12px;
  border: 1px solid #e6ded4;
  border-radius: 14px;
  padding: 14px 16px;
  background: #faf5f1;
}

.input-wrap input {
  border: none;
  outline: none;
  width: 100%;
  font-size: 1rem;
  background: transparent;
  color: #3e2723;
}

.visibility-btn {
  border: none;
  background: transparent;
  color: #7a665c;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  padding: 0;
}

.visibility-btn:hover {
  color: #3e2723;
}

.error-message {
  margin: 0;
  color: #d04d4d;
  font-size: 0.9rem;
}

.sign-in-btn,
.cancel-btn {
  width: 100%;
  border: none;
  border-radius: 14px;
  font-size: 1rem;
  font-weight: 700;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  cursor: pointer;
}

.sign-in-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.sign-in-btn {
  padding: 16px 20px;
  background: #3e2723;
  color: white;
}

.sign-in-btn:hover {
  background: #5d4037;
}

.cancel-btn {
  padding: 14px 20px;
  margin: 0;
  background: transparent;
  color: #3e2723;
  border: 1px solid #ddc6b2;
}

.cancel-btn:hover {
  background: #f6ede7;
}

.spin {
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

@media (max-width: 480px) {
  .staff-login-page {
    padding: 20px 12px;
  }

  .login-panel {
    border-radius: 20px;
  }

  .brand-block,
  .login-form {
    padding: 20px 16px;
  }

  .brand-block h1 {
    font-size: 22px;
  }

  .input-wrap {
    padding: 12px 12px;
  }
}
</style>
