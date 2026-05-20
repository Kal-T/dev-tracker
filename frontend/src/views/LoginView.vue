<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

const router = useRouter()
const authStore = useAuthStore()

const isRegister = ref(false)
const displayName = ref('')
const email = ref('')
const password = ref('')
const isLoading = ref(false)
const errorMessage = ref('')

const toggleMode = () => {
  isRegister.value = !isRegister.value
  errorMessage.value = ''
}

const handleSubmit = async () => {
  if (!email.value || !password.value) {
    errorMessage.value = 'Please fill out all fields.'
    return
  }

  isLoading.value = true
  errorMessage.value = ''

  try {
    if (isRegister.value) {
      if (!displayName.value) {
        errorMessage.value = 'Display name is required.'
        isLoading.value = false
        return
      }
      await authStore.register(displayName.value, email.value, password.value)
    } else {
      await authStore.login(email.value, password.value)
    }
    router.push('/board')
  } catch (error: any) {
    console.error('Auth failure:', error)
    errorMessage.value = error.response?.data?.message || 'Authentication failed. Please verify your credentials.'
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div class="bg-white p-10 rounded-2xl shadow-xl w-full border border-slate-100">
    <div class="text-center mb-10">
      <h1 class="text-3xl font-bold text-slate-900">
        {{ isRegister ? 'Create Account' : 'Welcome Back' }}
      </h1>
      <p class="text-slate-500 mt-2">
        {{ isRegister ? 'Register your DevTracker session' : 'Sign in to DevTracker' }}
      </p>
    </div>

    <!-- Error Alert Message -->
    <div
      v-if="errorMessage"
      class="mb-6 p-4 bg-red-50 border-l-4 border-red-500 rounded-lg text-sm text-red-700 font-medium"
    >
      {{ errorMessage }}
    </div>

    <form class="space-y-6" @submit.prevent="handleSubmit">
      <!-- Display Name (Register Mode Only) -->
      <div v-if="isRegister">
        <label class="block text-sm font-semibold text-slate-700 mb-2">Display Name</label>
        <input
          v-model="displayName"
          type="text"
          class="w-full px-4 py-3 rounded-lg border border-slate-200 focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
          placeholder="Alice Vance"
          required
        />
      </div>

      <div>
        <label class="block text-sm font-semibold text-slate-700 mb-2">Email Address</label>
        <input
          v-model="email"
          type="email"
          class="w-full px-4 py-3 rounded-lg border border-slate-200 focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
          placeholder="name@company.com"
          required
        />
      </div>

      <div>
        <label class="block text-sm font-semibold text-slate-700 mb-2">Password</label>
        <input
          v-model="password"
          type="password"
          class="w-full px-4 py-3 rounded-lg border border-slate-200 focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
          placeholder="••••••••"
          required
        />
      </div>

      <button
        type="submit"
        :disabled="isLoading"
        class="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 rounded-lg shadow-lg hover:shadow-blue-200 transition-all transform hover:-translate-y-0.5 disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2"
      >
        <svg
          v-if="isLoading"
          class="animate-spin h-5 w-5 text-white"
          fill="none"
          viewBox="0 0 24 24"
        >
          <circle
            class="opacity-25"
            cx="12"
            cy="12"
            r="10"
            stroke="currentColor"
            stroke-width="4"
          />
          <path
            class="opacity-75"
            fill="currentColor"
            d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
          />
        </svg>
        {{ isLoading ? 'Please wait...' : (isRegister ? 'Sign Up' : 'Sign In') }}
      </button>
    </form>

    <div class="mt-8 text-center text-sm text-slate-500">
      {{ isRegister ? 'Already have an account?' : "Don't have an account?" }}
      <button
        type="button"
        @click="toggleMode"
        class="text-blue-600 font-semibold hover:underline bg-transparent border-none p-0 cursor-pointer ml-1 outline-none"
      >
        {{ isRegister ? 'Sign In' : 'Create one' }}
      </button>
    </div>
  </div>
</template>
