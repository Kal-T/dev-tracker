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
      <transition 
        name="fade" 
        mode="out-in"
      >
        <component :is="Component" />
      </transition>
    </RouterView>
  </component>
</template>
