<script setup lang="ts">
import NavBar from '../components/NavBar.vue'
</script>

<template>
  <div class="min-h-screen flex flex-col bg-slate-50 font-sans text-slate-900">
    <NavBar />

    <main class="flex-grow max-w-7xl mx-auto w-full px-4 sm:px-6 lg:px-8 py-8">
      <slot />
    </main>

    <footer class="py-12 border-t border-slate-200 bg-white">
      <div class="max-w-7xl mx-auto px-4 text-center text-slate-400 text-sm">
        &copy; 2026 DevTracker. Built with Vue 3 & Tailwind CSS.
      </div>
    </footer>
  </div>
</template>
