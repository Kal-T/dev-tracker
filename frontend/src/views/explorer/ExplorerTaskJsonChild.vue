<script setup lang="ts">
import { type Task } from '@/composables/useTasks'

defineProps<{
  task: Task
}>()
</script>

<template>
  <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4 animate-in fade-in duration-150">
    <div>
      <span class="text-[10px] font-bold text-slate-400 uppercase tracking-wider">Sub Route: JSON Data</span>
    </div>

    <pre class="bg-slate-900 text-indigo-300 p-4 rounded-xl text-xs font-mono overflow-x-auto leading-relaxed">{{ JSON.stringify(task, null, 2) }}</pre>
  </div>
</template>
