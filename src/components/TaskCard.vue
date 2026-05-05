<script setup lang="ts">
import { RouterLink } from 'vue-router'
import { TASK_STATUS, TASK_PRIORITY, type Task, type TaskStatus } from '@/stores/taskStore'

const props = defineProps<{
  task: Task
}>()

const emit = defineEmits<{
  (e: 'move', id: string, status: TaskStatus): void
  (e: 'delete', id: string): void
}>()

const priorityColors = {
  [TASK_PRIORITY.LOW]: 'bg-green-100 text-green-700 border-green-200',
  [TASK_PRIORITY.MEDIUM]: 'bg-yellow-100 text-yellow-700 border-yellow-200',
  [TASK_PRIORITY.HIGH]: 'bg-red-100 text-red-700 border-red-200'
}

const statusOptions: { label: string; value: TaskStatus }[] = [
  { label: 'To Do', value: TASK_STATUS.TODO },
  { label: 'In Progress', value: TASK_STATUS.IN_PROGRESS },
  { label: 'Done', value: TASK_STATUS.DONE }
]
</script>

<template>
  <div class="bg-white p-4 rounded-xl shadow-sm border border-slate-200 hover:shadow-md transition-shadow group">
    <div class="flex justify-between items-start mb-3">
      <span 
        class="text-[10px] uppercase font-bold px-2 py-0.5 rounded-full border"
        :class="priorityColors[task.priority]"
      >
        {{ task.priority }}
      </span>
      <button 
        @click="emit('delete', task.id)"
        class="text-slate-300 hover:text-red-500 transition-colors opacity-0 group-hover:opacity-100"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
        </svg>
      </button>
    </div>

    <RouterLink :to="`/task/${task.id}`" class="block group/title">
      <h3 class="font-semibold text-slate-800 mb-1 leading-tight group-hover/title:text-blue-600 transition-colors">
        {{ task.title }}
      </h3>
    </RouterLink>
    <p class="text-sm text-slate-500 line-clamp-2 mb-4">{{ task.description }}</p>

    <div class="flex flex-wrap gap-1 mb-4">
      <span 
        v-for="tag in task.tags" 
        :key="tag"
        class="text-[10px] bg-slate-100 text-slate-600 px-2 py-0.5 rounded"
      >
        #{{ tag }}
      </span>
    </div>

    <div class="pt-3 border-t border-slate-50 flex justify-between items-center">
      <div class="flex -space-x-2">
        <div class="w-6 h-6 rounded-full bg-blue-500 border-2 border-white flex items-center justify-center text-[10px] text-white font-bold">JD</div>
      </div>
      
      <select 
        :value="task.status" 
        @change="(e) => emit('move', task.id, (e.target as HTMLSelectElement).value as TaskStatus)"
        class="text-xs bg-slate-50 border-none rounded px-2 py-1 text-slate-600 focus:ring-1 focus:ring-blue-500 outline-none cursor-pointer"
      >
        <option v-for="opt in statusOptions" :key="opt.value" :value="opt.value">
          {{ opt.label }}
        </option>
      </select>
    </div>
  </div>
</template>
