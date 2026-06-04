<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="max-w-7xl mx-auto py-4">

    <div class="grid grid-cols-1 lg:grid-cols-4 gap-6">
      <!-- Sidebar -->
      <aside class="lg:col-span-1 space-y-3">
        <div class="bg-white p-4 rounded-2xl border border-slate-200 shadow-sm flex flex-col gap-1">
          <RouterLink
            to="/explorer"
            class="px-4 py-2.5 rounded-xl text-sm font-semibold transition-all text-slate-600 hover:text-indigo-600 hover:bg-slate-50"
            active-class="text-indigo-600 bg-indigo-50"
            exact-active-class="text-indigo-600 bg-indigo-50"
          >
            Overview
          </RouterLink>

          <RouterLink
            to="/explorer/tasks"
            class="px-4 py-2.5 rounded-xl text-sm font-semibold transition-all text-slate-600 hover:text-indigo-600 hover:bg-slate-50"
            active-class="text-indigo-600 bg-indigo-50"
          >
            Tasks Details (/tasks/:id)
          </RouterLink>

          <RouterLink
            to="/explorer/tags/feature"
            class="px-4 py-2.5 rounded-xl text-sm font-semibold transition-all text-slate-600 hover:text-indigo-600 hover:bg-slate-50"
            active-class="text-indigo-600 bg-indigo-50"
          >
            Tag Filter (/tags/:tag)
          </RouterLink>
        </div>
      </aside>

      <!-- Main Content -->
      <main class="lg:col-span-3">
        <RouterView v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </RouterView>
      </main>
    </div>
  </div>
</template>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.15s ease, transform 0.15s ease;
}
.fade-enter-from {
  opacity: 0;
  transform: translateY(4px);
}
.fade-leave-to {
  opacity: 0;
  transform: translateY(-4px);
}
</style>
