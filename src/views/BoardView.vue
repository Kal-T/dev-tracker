<script setup lang="ts">
// Board view logic here
</script>

<template>
  <div class="p-6">
    <h1 class="text-3xl font-bold text-slate-800 mb-6">Project Board</h1>
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <!-- Placeholder columns -->
      <div class="bg-slate-100 p-4 rounded-lg min-h-[400px]">
        <h2 class="font-semibold mb-4 text-slate-600 uppercase text-sm tracking-wider">To Do</h2>
        <div class="bg-white p-4 rounded shadow-sm mb-3 border-l-4 border-blue-500">
          <p class="font-medium">Setup project structure</p>
          <p class="text-xs text-slate-500 mt-1">High Priority</p>
        </div>
      </div>
      <div class="bg-slate-100 p-4 rounded-lg min-h-[400px]">
        <h2 class="font-semibold mb-4 text-slate-600 uppercase text-sm tracking-wider">In Progress</h2>
      </div>
      <div class="bg-slate-100 p-4 rounded-lg min-h-[400px]">
        <h2 class="font-semibold mb-4 text-slate-600 uppercase text-sm tracking-wider">Done</h2>
      </div>
    </div>
  </div>
</template>
