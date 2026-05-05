import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const user = ref<{ name: string; email: string } | null>(null)
  const isAuthenticated = computed(() => !!user.value)

  function login(email: string) {
    // Mock login
    user.value = {
      name: 'John Doe',
      email: email
    }
  }

  function logout() {
    user.value = null
  }

  return {
    user,
    isAuthenticated,
    login,
    logout
  }
})
