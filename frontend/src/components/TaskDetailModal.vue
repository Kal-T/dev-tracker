<script setup lang="ts">
import { computed } from 'vue'
import { useTaskDetail } from '@/composables/useTaskDetail'
import BaseModal from './BaseModal.vue'

const props = defineProps<{
  taskId: string
}>()

const emit = defineEmits<{
  (e: 'close'): void
}>()

const taskIdRef = computed(() => props.taskId)
const { task, isEditing, editedTask, toggleEdit, saveChanges, cancelEdit } = useTaskDetail(taskIdRef)

const priorityColors: Record<string, string> = {
  low: 'bg-green-100 text-green-700 border-green-200',
  medium: 'bg-yellow-100 text-yellow-700 border-yellow-200',
  high: 'bg-red-100 text-red-700 border-red-200'
}

const formatDate = (dateStr?: string) => {
  if (!dateStr) return 'N/A'
  try {
    return new Date(dateStr).toLocaleDateString()
  } catch (e) {
    return dateStr
  }
}

const handleSave = () => {
  saveChanges()
}

const handleCancel = () => {
  cancelEdit()
}
</script>

<template>
  <BaseModal @close="emit('close')">
    <template #title>
      <div class="flex items-center gap-3">
        <span v-if="!isEditing" class="font-black text-slate-900 tracking-tight leading-tight">
          {{ task?.title || 'Loading details...' }}
        </span>
        <span v-else class="font-black text-slate-900 tracking-tight leading-tight">
          Edit Task
        </span>
      </div>
    </template>

    <div v-if="task" class="space-y-6">
      <!-- Title Edit Field -->
      <div v-if="isEditing" class="space-y-1">
        <label class="block text-xs font-black text-slate-400 uppercase tracking-widest">Title</label>
        <input
          v-model="editedTask.title"
          type="text"
          class="w-full px-4 py-2.5 rounded-xl border border-slate-200 focus:ring-2 focus:ring-blue-500 outline-none transition-all font-bold text-slate-800"
        />
      </div>

      <!-- Description Section -->
      <div>
        <label class="block text-xs font-black text-slate-400 uppercase tracking-widest mb-2">
          Description
        </label>
        <p v-if="!isEditing" class="text-slate-600 leading-relaxed text-sm whitespace-pre-wrap bg-slate-50/50 p-4 rounded-xl border border-slate-100">
          {{ task.description || 'No description provided.' }}
        </p>
        <textarea
          v-else
          v-model="editedTask.description"
          rows="4"
          class="w-full p-4 bg-slate-50 rounded-xl border border-slate-200 focus:ring-2 focus:ring-blue-500 outline-none transition-all text-sm text-slate-700"
          placeholder="Add more details about this task..."
        ></textarea>
      </div>

      <!-- Meta Grid -->
      <div class="grid grid-cols-2 gap-6 pt-4 border-t border-slate-100">
        <div>
          <label class="block text-xs font-black text-slate-400 uppercase tracking-widest mb-2">Status</label>
          <div
            v-if="!isEditing"
            class="inline-flex items-center px-3 py-1 rounded-full text-xs font-black bg-slate-100 text-slate-600 uppercase tracking-wider border border-slate-200/50"
          >
            {{ task.status }}
          </div>
          <select
            v-else
            v-model="editedTask.status"
            class="w-full px-3 py-2 bg-white rounded-xl border border-slate-200 focus:ring-2 focus:ring-blue-500 outline-none transition-all font-semibold text-xs text-slate-700 cursor-pointer"
          >
            <option value="todo">To Do</option>
            <option value="in-progress">In Progress</option>
            <option value="done">Done</option>
          </select>
        </div>

        <div>
          <label class="block text-xs font-black text-slate-400 uppercase tracking-widest mb-2">
            Priority
          </label>
          <div
            v-if="!isEditing"
            class="inline-flex items-center px-3 py-1 rounded-full text-xs font-black uppercase tracking-wider border"
            :class="priorityColors[task.priority]"
          >
            {{ task.priority }}
          </div>
          <select
            v-else
            v-model="editedTask.priority"
            class="w-full px-3 py-2 bg-white rounded-xl border border-slate-200 focus:ring-2 focus:ring-blue-500 outline-none transition-all font-semibold text-xs text-slate-700 cursor-pointer"
          >
            <option value="low">Low</option>
            <option value="medium">Medium</option>
            <option value="high">High</option>
          </select>
        </div>
      </div>

      <div class="grid grid-cols-2 gap-6 pt-4 border-t border-slate-100">
        <div>
          <label class="block text-xs font-black text-slate-400 uppercase tracking-widest mb-1">
            Task Source
          </label>
          <span
            v-if="task.type === 'github'"
            class="inline-flex items-center px-3 py-1 rounded-full text-xs font-extrabold bg-slate-800 text-white border border-slate-900 gap-1 shadow-sm"
          >
            <svg class="h-3 w-3 fill-current" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg">
              <path fill-rule="evenodd" d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0016 8c0-4.42-3.58-8-8-8z"/>
            </svg>
            GitHub
          </span>
          <span
            v-else
            class="inline-flex items-center px-3 py-1 rounded-full text-xs font-bold bg-blue-50 text-blue-700 border border-blue-100 gap-1"
          >
            <svg class="h-3 w-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
            User
          </span>
        </div>

        <div>
          <label class="block text-xs font-black text-slate-400 uppercase tracking-widest mb-1">
            Created At
          </label>
          <div class="text-slate-500 text-xs font-semibold py-1">
            {{ formatDate(task.createdAt) }}
          </div>
        </div>
      </div>

      <!-- Action Footer -->
      <div class="pt-6 border-t border-slate-100 flex gap-3">
        <template v-if="!isEditing">
          <button
            class="flex-1 bg-blue-600 text-white py-2.5 rounded-xl font-bold shadow-md hover:bg-blue-700 transition-all text-sm"
            @click="toggleEdit"
          >
            Edit Task
          </button>
          <button
            class="flex-1 bg-white text-slate-600 border border-slate-200 py-2.5 rounded-xl font-bold hover:bg-slate-50 transition-all text-sm"
            @click="emit('close')"
          >
            Close
          </button>
        </template>
        <template v-else>
          <button
            class="flex-1 bg-blue-600 text-white py-2.5 rounded-xl font-bold shadow-md hover:bg-blue-700 transition-all text-sm animate-in fade-in zoom-in-95 duration-200"
            @click="handleSave"
          >
            Save Changes
          </button>
          <button
            class="flex-1 bg-white text-slate-600 border border-slate-200 py-2.5 rounded-xl font-bold hover:bg-slate-50 transition-all text-sm"
            @click="handleCancel"
          >
            Cancel
          </button>
        </template>
      </div>
    </div>
    <div v-else class="py-12 flex flex-col items-center justify-center text-center">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500 mb-3"></div>
      <p class="text-sm text-slate-500 font-semibold">Loading task details...</p>
    </div>
  </BaseModal>
</template>
