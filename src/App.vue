<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, RouterView } from 'vue-router'
import MainLayout from './layouts/MainLayout.vue'
import AuthLayout from './layouts/AuthLayout.vue'

const route = useRoute()

// Map of layout components
const layouts = {
  default: MainLayout,
  auth: AuthLayout,
}

// Compute the active layout based on route meta, fallback to default
const layout = computed(() => {
  const layoutName = route.meta.layout as keyof typeof layouts
  return layouts[layoutName] || layouts.default
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
