<script setup lang="ts">
import { RouterLink, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

const authStore = useAuthStore()
const router = useRouter()

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<template>
  <nav class="bg-white border-b border-slate-200 sticky top-0 z-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between h-16">
        <div class="flex items-center space-x-8">
          <RouterLink to="/" class="flex items-center">
            <span
              class="text-2xl font-black bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent"
              >DevTracker</span
            >
          </RouterLink>

          <div class="hidden md:flex space-x-4">
            <RouterLink
              to="/board"
              class="px-3 py-2 rounded-md text-sm font-medium text-slate-600 hover:text-blue-600 hover:bg-blue-50 transition-colors"
              active-class="text-blue-600 bg-blue-50"
            >
              Board
            </RouterLink>
            <RouterLink
              to="/stats"
              class="px-3 py-2 rounded-md text-sm font-medium text-slate-600 hover:text-blue-600 hover:bg-blue-50 transition-colors"
              active-class="text-blue-600 bg-blue-50"
            >
              Stats
            </RouterLink>
          </div>
        </div>

        <div class="flex items-center space-x-4">
          <template v-if="!authStore.isAuthenticated">
            <RouterLink
              to="/login"
              class="px-4 py-2 rounded-lg text-sm font-semibold text-slate-700 hover:bg-slate-100 transition-colors"
            >
              Sign In
            </RouterLink>
            <button
              class="bg-blue-600 text-white px-4 py-2 rounded-lg text-sm font-semibold hover:bg-blue-700 shadow-md hover:shadow-blue-200 transition-all"
            >
              Get Started
            </button>
          </template>
          <div v-else class="flex items-center gap-4">
            <div class="flex items-center gap-2">
              <div
                class="w-8 h-8 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-700 font-bold text-xs border border-indigo-200"
              >
                {{ authStore.user?.name.charAt(0) }}
              </div>
              <span class="text-sm font-semibold text-slate-700">{{ authStore.user?.name }}</span>
            </div>
            <button
              class="text-sm font-semibold text-red-500 hover:text-red-700 transition-colors"
              @click="handleLogout"
            >
              Logout
            </button>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<style scoped>
/* Additional styles if needed, though Tailwind covers most */
</style>
