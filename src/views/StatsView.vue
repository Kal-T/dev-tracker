<script setup lang="ts">
// Stats logic here
</script>

<template>
  <div class="p-6">
    <h1 class="text-3xl font-bold text-slate-800 mb-8">Performance Stats</h1>
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <div class="bg-white p-6 rounded-xl shadow-sm border border-slate-100">
        <p class="text-slate-500 text-sm font-medium">Total Tasks</p>
        <p class="text-4xl font-bold text-slate-900 mt-2">24</p>
      </div>
      <div class="bg-white p-6 rounded-xl shadow-sm border border-slate-100">
        <p class="text-slate-500 text-sm font-medium">Completed</p>
        <p class="text-4xl font-bold text-green-600 mt-2">18</p>
      </div>
      <div class="bg-white p-6 rounded-xl shadow-sm border border-slate-100">
        <p class="text-slate-500 text-sm font-medium">In Progress</p>
        <p class="text-4xl font-bold text-blue-600 mt-2">4</p>
      </div>
      <div class="bg-white p-6 rounded-xl shadow-sm border border-slate-100">
        <p class="text-slate-500 text-sm font-medium">Overdue</p>
        <p class="text-4xl font-bold text-red-600 mt-2">2</p>
      </div>
    </div>
    
    <div class="mt-12 bg-white p-8 rounded-xl shadow-sm border border-slate-100">
      <h2 class="text-xl font-semibold mb-6">Velocity Trend</h2>
      <div class="h-64 bg-slate-50 rounded flex items-end justify-between p-4">
        <!-- Mock chart bars -->
        <div class="w-12 bg-blue-400 rounded-t h-1/2"></div>
        <div class="w-12 bg-blue-400 rounded-t h-3/4"></div>
        <div class="w-12 bg-blue-400 rounded-t h-2/3"></div>
        <div class="w-12 bg-blue-400 rounded-t h-full"></div>
        <div class="w-12 bg-blue-400 rounded-t h-4/5"></div>
      </div>
    </div>
  </div>
</template>
