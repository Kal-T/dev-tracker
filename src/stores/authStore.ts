import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const user = ref<{ name: string; email: string } | null>(null)
  const isAuthenticated = computed(() => !!user.value)

  function login(email: string) {
    user.value = {
      name: 'Thiha Htet Zaw',
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
