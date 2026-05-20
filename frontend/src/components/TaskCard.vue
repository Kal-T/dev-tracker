<script setup lang="ts">
import { type DirectiveBinding } from 'vue'
import { TASK_STATUS, TASK_PRIORITY, type Task, type TaskStatus } from '@/composables/useTasks'
import DeleteButton from './DeleteButton.vue'

const props = defineProps<{
  task: Task
}>()


const emit = defineEmits<{
  (e: 'move', id: string, status: TaskStatus): void
  (e: 'select', id: string): void
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
  <div
    v-priority-color="task.priority"
    class="bg-white p-4 rounded-xl shadow-sm border border-slate-200 hover:shadow-md transition-shadow group cursor-grab active:cursor-grabbing select-none"
  >
    <div class="flex justify-between items-start mb-3">
      <div class="flex items-center gap-1.5">
        <span
          class="text-[10px] uppercase font-bold px-2 py-0.5 rounded-full border"
          :class="priorityColors[task.priority]"
        >
          {{ task.priority }}
        </span>

        <!-- Source Type Indicator Badge -->
        <span
          v-if="task.type === 'github'"
          class="text-[10px] font-extrabold px-2 py-0.5 rounded-full border bg-slate-800 text-white border-slate-900 flex items-center gap-1 shadow-sm"
        >
          <svg class="h-3 w-3 fill-current" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg">
            <path fill-rule="evenodd" d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0016 8c0-4.42-3.58-8-8-8z"/>
          </svg>
          GitHub
        </span>
        <span
          v-else
          class="text-[10px] font-bold px-2 py-0.5 rounded-full border bg-blue-50 text-blue-700 border-blue-100 flex items-center gap-1"
        >
          <svg class="h-3 w-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
          </svg>
          User
        </span>
      </div>
      <DeleteButton :taskId="task.id" />
    </div>

    <div class="block cursor-pointer group/title" @click="emit('select', task.id)">
      <h3
        class="font-semibold text-slate-800 mb-1 leading-tight group-hover/title:text-blue-600 transition-colors"
      >
        {{ task.title }}
      </h3>
    </div>
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
        <div
          class="w-6 h-6 rounded-full bg-blue-500 border-2 border-white flex items-center justify-center text-[10px] text-white font-bold"
        >
          JD
        </div>
      </div>

      <select
        :value="task.status"
        class="text-xs bg-slate-50 border-none rounded px-2 py-1 text-slate-600 focus:ring-1 focus:ring-blue-500 outline-none cursor-pointer"
        @change="(e) => emit('move', task.id, (e.target as HTMLSelectElement).value as TaskStatus)"
      >
        <option v-for="opt in statusOptions" :key="opt.value" :value="opt.value">
          {{ opt.label }}
        </option>
      </select>
    </div>
  </div>
</template>
