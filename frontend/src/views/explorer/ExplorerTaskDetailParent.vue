<script setup lang="ts">
import { computed } from 'vue'
import { RouterLink, RouterView } from 'vue-router'
import { useTaskDetailQuery } from '@/composables/useTasks'

const props = defineProps<{
  id: string
}>()

const idRef = computed(() => props.id)
const { data: task, isLoading, error } = useTaskDetailQuery(idRef)
</script>

<template>
  <div class="space-y-4">
    <div class="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <RouterLink to="/explorer/tasks" class="text-xs text-slate-500 hover:text-indigo-600 font-semibold block mb-1">
          &larr; Back to Tasks
        </RouterLink>
        
        <div v-if="isLoading" class="h-6 w-32 bg-slate-200 animate-pulse rounded"></div>
        <h2 v-else-if="task" class="text-lg font-bold text-slate-900 leading-tight">
          {{ task.title }}
        </h2>
      </div>

      <!-- Tab Buttons -->
      <div v-if="task" class="flex bg-slate-100 p-1 rounded-xl">
        <RouterLink
          :to="'/explorer/tasks/' + id + '/info'"
          class="px-3 py-1.5 rounded-lg text-xs font-bold text-slate-600"
          active-class="bg-white text-slate-900 shadow-sm"
        >
          Info Tab
        </RouterLink>
        <RouterLink
          :to="'/explorer/tasks/' + id + '/json'"
          class="px-3 py-1.5 rounded-lg text-xs font-bold text-slate-600"
          active-class="bg-white text-slate-900 shadow-sm"
        >
          JSON Tab
        </RouterLink>
      </div>
    </div>

    <!-- Nested Component View -->
    <div v-if="task">
      <RouterView :task="task" />
    </div>
    <div v-else-if="error" class="bg-white p-6 rounded-2xl text-center text-red-500 border border-slate-200">
      Error loading task. Make sure ID "{{ id }}" is valid.
    </div>
  </div>
</template>
