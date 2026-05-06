<script setup lang="ts">
// Auth layout for login/register pages
</script>

<template>
  <div class="min-h-screen bg-slate-100 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
    <div class="sm:mx-auto sm:w-full sm:max-w-md mb-8">
      <div class="flex justify-center">
        <span
          class="text-3xl font-black bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent"
          >DevTracker</span
        >
      </div>
    </div>

    <div class="sm:mx-auto sm:w-full sm:max-w-md">
      <slot />
    </div>
  </div>
</template>
