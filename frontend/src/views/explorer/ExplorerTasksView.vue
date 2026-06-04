<script setup lang="ts">
import { ref, computed } from 'vue'
import { RouterLink } from 'vue-router'
import { useTasksQuery } from '@/composables/useTasks'

const status = ref<string | undefined>(undefined)
const search = ref<string | undefined>(undefined)

const { data: tasksResponse, isLoading } = useTasksQuery(status, search)
const tasks = computed(() => tasksResponse.value?.data || [])

const allTags = computed(() => {
  const tagsSet = new Set<string>()
  tasks.value.forEach(t => {
    if (t.tags) t.tags.forEach(tag => tagsSet.add(tag))
  })
  if (tagsSet.size === 0) {
    return ['feature', 'bug', 'enhancement']
  }
  return Array.from(tagsSet)
})

const priorityColors = {
  low: 'bg-green-50 text-green-700 border-green-200',
  medium: 'bg-yellow-50 text-yellow-700 border-yellow-200',
  high: 'bg-red-50 text-red-700 border-red-200'
}
</script>

<template>
  <div class="space-y-6">
    <div class="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm flex flex-col md:flex-row md:items-center justify-between gap-4">
      <h2 class="font-bold text-slate-800">Tasks</h2>

      <div class="flex flex-wrap gap-1.5 items-center">
        <span class="text-xs text-slate-400 font-semibold mr-1">Tags:</span>
        <RouterLink
          v-for="tag in allTags"
          :key="tag"
          :to="'/explorer/tags/' + tag"
          class="text-xs bg-slate-50 text-slate-600 px-2.5 py-1 rounded-lg border border-slate-200 hover:bg-indigo-50 hover:text-indigo-600 hover:border-indigo-200 transition-colors font-medium"
        >
          #{{ tag }}
        </RouterLink>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="isLoading" class="space-y-2">
      <div v-for="i in 3" :key="i" class="bg-white h-12 rounded-xl animate-pulse"></div>
    </div>

    <!-- Empty -->
    <div v-else-if="tasks.length === 0" class="bg-white p-8 rounded-2xl text-center border border-slate-200">
      <p class="text-sm text-slate-500">No tasks found. Create a task in the Board view first.</p>
    </div>

    <!-- List -->
    <div v-else class="space-y-2">
      <div
        v-for="task in tasks"
        :key="task.id"
        class="bg-white border border-slate-200 rounded-xl p-4 flex items-center justify-between hover:border-slate-300 shadow-sm transition-all"
      >
        <div class="flex items-center gap-3">
          <span class="text-[10px] font-bold px-2 py-0.5 rounded-full border uppercase" :class="priorityColors[task.priority]">
            {{ task.priority }}
          </span>
          <span class="font-bold text-slate-800 text-sm">{{ task.title }}</span>
        </div>

        <RouterLink
          :to="'/explorer/tasks/' + task.id"
          class="text-xs font-bold text-indigo-600 hover:text-indigo-800"
        >
          View details &rarr;
        </RouterLink>
      </div>
    </div>
  </div>
</template>
