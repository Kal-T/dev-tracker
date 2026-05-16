
<script setup lang="ts">
import { onMounted, onUnmounted, onUpdated, onActivated, onDeactivated } from 'vue'
import { TASK_PRIORITY } from '@/stores/taskStore'
import { useTaskStats } from '@/composables/useTaskStats'

const { stats, getBarWidth } = useTaskStats()

onMounted(() => {
  console.log('[StatsView] Component mounted')
})

onUpdated(() => {
  console.log('[StatsView] Component updated')
})

onUnmounted(() => {
  console.log('[StatsView] Component onUnmounted')
})

onActivated(() => console.log('StatsView activated (restored from KeepAlive)'))
onDeactivated(() => console.log('StatsView deactivated (kept-alive)'))
</script>

<template>
  <div class="space-y-8 animate-in fade-in slide-in-from-bottom-4 duration-700">
    <div class="flex items-center justify-between">
      <h1 class="text-3xl font-black text-slate-900 tracking-tight">Performance Stats</h1>
      <div
        class="bg-blue-50 text-blue-700 px-4 py-1 rounded-full text-sm font-bold border border-blue-100"
      >
        Live Metrics
      </div>
    </div>

    <!-- Summary Cards -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div class="bg-white p-6 rounded-2xl border border-slate-100 shadow-sm">
        <p class="text-xs font-black text-slate-400 uppercase tracking-widest mb-2">Total Tasks</p>
        <p class="text-4xl font-black text-slate-900">{{ stats.total }}</p>
      </div>
      <div class="bg-white p-6 rounded-2xl border border-slate-100 shadow-sm">
        <p class="text-xs font-black text-slate-400 uppercase tracking-widest mb-2">
          Completion Rate
        </p>
        <div class="flex items-end gap-2">
          <p class="text-4xl font-black text-green-600">{{ stats.completionRate }}%</p>
          <p class="text-sm text-slate-400 mb-1 font-bold">({{ stats.done }} tasks)</p>
        </div>
      </div>
      <div class="bg-white p-6 rounded-2xl border border-slate-100 shadow-sm">
        <p class="text-xs font-black text-slate-400 uppercase tracking-widest mb-2">Health Score</p>
        <p class="text-4xl font-black text-blue-600">Great</p>
      </div>
    </div>

    <!-- Priority Distribution (CSS Charts) -->
    <div class="bg-white p-8 rounded-2xl border border-slate-100 shadow-sm">
      <h2 class="text-xl font-bold text-slate-800 mb-8">Priority Distribution</h2>

      <div class="space-y-6">
        <div>
          <div class="flex justify-between text-sm font-bold text-slate-600 mb-2">
            <span class="flex items-center gap-2">
              <span class="w-3 h-3 rounded-full bg-red-500"></span>
              High Priority
            </span>
            <span>{{ stats.priorityCounts[TASK_PRIORITY.HIGH] }}</span>
          </div>
          <div class="h-4 bg-slate-100 rounded-full overflow-hidden">
            <div
              class="h-full bg-red-500 transition-all duration-1000 ease-out"
              :style="{ width: getBarWidth(stats.priorityCounts[TASK_PRIORITY.HIGH]) }"
            ></div>
          </div>
        </div>

        <div>
          <div class="flex justify-between text-sm font-bold text-slate-600 mb-2">
            <span class="flex items-center gap-2">
              <span class="w-3 h-3 rounded-full bg-yellow-400"></span>
              Medium Priority
            </span>
            <span>{{ stats.priorityCounts[TASK_PRIORITY.MEDIUM] }}</span>
          </div>
          <div class="h-4 bg-slate-100 rounded-full overflow-hidden">
            <div
              class="h-full bg-yellow-400 transition-all duration-1000 ease-out"
              :style="{ width: getBarWidth(stats.priorityCounts[TASK_PRIORITY.MEDIUM]) }"
            ></div>
          </div>
        </div>

        <div>
          <div class="flex justify-between text-sm font-bold text-slate-600 mb-2">
            <span class="flex items-center gap-2">
              <span class="w-3 h-3 rounded-full bg-green-500"></span>
              Low Priority
            </span>
            <span>{{ stats.priorityCounts[TASK_PRIORITY.LOW] }}</span>
          </div>
          <div class="h-4 bg-slate-100 rounded-full overflow-hidden">
            <div
              class="h-full bg-green-500 transition-all duration-1000 ease-out"
              :style="{ width: getBarWidth(stats.priorityCounts[TASK_PRIORITY.LOW]) }"
            ></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
