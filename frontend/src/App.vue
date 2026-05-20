<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRoute, RouterView } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import MainLayout from './layouts/MainLayout.vue'
import AuthLayout from './layouts/AuthLayout.vue'

const route = useRoute()
const authStore = useAuthStore()

const layouts = {
  default: MainLayout,
  auth: AuthLayout
}

const layout = computed(() => {
  const layoutName = route.meta.layout as keyof typeof layouts
  return layouts[layoutName] || layouts.default
})

onMounted(async () => {
  const hasUserSession = localStorage.getItem('devtracker-user')
  if (hasUserSession && !authStore.isAuthenticated) {
    try {
      console.log('[DevTracker] Restoring active authentication session...')
      await authStore.refreshSession()
    } catch (e) {
      console.warn('[DevTracker] Active session check failed.')
    }
  }
})
</script>

<template>
  <component :is="layout">
    <RouterView v-slot="{ Component }">
      <transition name="fade" mode="out-in">
        <KeepAlive :include="['StatsView']">
          <Suspense>
            <!-- Main Content -->
            <component :is="Component" />

            <!-- Loading State -->
            <template #fallback>
              <div class="flex items-center justify-center py-20">
                <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
              </div>
            </template>
          </Suspense>
        </KeepAlive>
      </transition>
    </RouterView>
  </component>
</template>
