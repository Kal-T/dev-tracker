<script setup lang="ts">
import { type Task } from '@/composables/useTasks'

defineProps<{
  task: Task
}>()

const priorityColors = {
  low: 'bg-green-50 text-green-700 border-green-200',
  medium: 'bg-yellow-50 text-yellow-700 border-yellow-200',
  high: 'bg-red-50 text-red-700 border-red-200'
}
</script>

<template>
  <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4 animate-in fade-in duration-150">
    <div>
      <span class="text-[10px] font-bold text-slate-400 uppercase tracking-wider">Sub Route: Info</span>
    </div>

    <div class="space-y-1">
      <span class="text-xs text-slate-400 font-bold block">Description:</span>
      <p class="text-slate-700 text-sm leading-relaxed whitespace-pre-wrap bg-slate-50 p-3 rounded-lg border border-slate-100">
        {{ task.description || 'No description provided.' }}
      </p>
    </div>

    <div class="grid grid-cols-3 gap-4 pt-2">
      <div>
        <span class="text-xs text-slate-400 font-bold block mb-1">Status:</span>
        <span class="inline-flex px-2 py-0.5 rounded text-xs font-semibold bg-slate-100 text-slate-600 border capitalize">
          {{ task.status }}
        </span>
      </div>

      <div>
        <span class="text-xs text-slate-400 font-bold block mb-1">Priority:</span>
        <span class="inline-flex px-2 py-0.5 rounded text-xs font-semibold border capitalize" :class="priorityColors[task.priority]">
          {{ task.priority }}
        </span>
      </div>

      <div>
        <span class="text-xs text-slate-400 font-bold block mb-1">Source:</span>
        <span class="inline-flex px-2 py-0.5 rounded text-xs font-semibold bg-indigo-50 text-indigo-700 border border-indigo-100 capitalize">
          {{ task.type }}
        </span>
      </div>
    </div>
  </div>
</template>
