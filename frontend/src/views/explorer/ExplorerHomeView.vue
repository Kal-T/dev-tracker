<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>

<template>
  <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-6">
    <div>
      <h2 class="text-xl font-bold text-slate-900">Routing Demos Overview</h2>
      <p class="text-slate-500 text-sm mt-1">Select a routing concept below to view the implementation in action:</p>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div class="bg-slate-50 p-5 rounded-xl border border-slate-100 flex flex-col justify-between hover:border-slate-200 transition-all">
        <div>
          <h3 class="font-bold text-slate-800 text-sm mb-1">Dynamic Parameters</h3>
          <p class="text-xs text-slate-500 mb-4">Passing custom ID variables inside paths and nested tabs routing.</p>
        </div>
        <RouterLink to="/explorer/tasks" class="text-xs font-bold text-indigo-600 hover:text-indigo-800">
          View Demo &rarr;
        </RouterLink>
      </div>

      <div class="bg-slate-50 p-5 rounded-xl border border-slate-100 flex flex-col justify-between hover:border-slate-200 transition-all">
        <div>
          <h3 class="font-bold text-slate-800 text-sm mb-1">Reactivity Watchers</h3>
          <p class="text-xs text-slate-500 mb-4">Reacting to path updates on the same component instance.</p>
        </div>
        <RouterLink to="/explorer/tags/feature" class="text-xs font-bold text-indigo-600 hover:text-indigo-800">
          View Demo &rarr;
        </RouterLink>
      </div>
    </div>
  </div>
</template>
