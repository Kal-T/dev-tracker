<script lang="ts">
import { useAuthStore } from '@/stores/authStore'

export default {
  beforeRouteEnter(to, from, next) {
    const authStore = useAuthStore()
    if (!authStore.isAuthenticated) {
      next('/login')
    } else {
      next()
    }
  }
}
</script>

<script setup lang="ts">
import { ref, computed, watchEffect } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useTaskStore, TASK_STATUS, TASK_PRIORITY, type TaskStatus, type TaskPriority } from '@/stores/taskStore'

const route = useRoute()
const router = useRouter()
const taskStore = useTaskStore()

const isEditing = ref(false)
const taskId = computed(() => route.params.id as string)

const task = computed(() => {
  return taskStore.tasks.find((t) => t.id === taskId.value)
})

// Local state for editing
const editedTask = ref({
  title: '',
  description: '',
  status: TASK_STATUS.TODO as TaskStatus,
  priority: TASK_PRIORITY.MEDIUM as TaskPriority
})

// Initialize local state when task is found or toggled to edit
watchEffect(() => {
  if (task.value) {
    editedTask.value = {
      title: task.value.title,
      description: task.value.description,
      status: task.value.status,
      priority: task.value.priority
    }
  } else {
    // Redirect if not found
    router.replace('/board')
  }
})

const toggleEdit = () => {
  isEditing.value = !isEditing.value
}

const saveChanges = () => {
  if (task.value) {
    taskStore.updateTask(task.value.id, editedTask.value)
    isEditing.value = false
  }
}

const cancelEdit = () => {
  if (task.value) {
    editedTask.value = {
      title: task.value.title,
      description: task.value.description,
      status: task.value.status,
      priority: task.value.priority
    }
    isEditing.value = false
  }
}

const priorityColors = {
  [TASK_PRIORITY.LOW]: 'bg-green-100 text-green-700',
  [TASK_PRIORITY.MEDIUM]: 'bg-yellow-100 text-yellow-700',
  [TASK_PRIORITY.HIGH]: 'bg-red-100 text-red-700'
}
</script>

<template>
  <div v-if="task" class="max-w-4xl mx-auto py-8">
    <div class="bg-white rounded-2xl shadow-xl border border-slate-100 overflow-hidden">
      <!-- Header -->
      <div class="px-8 py-6 border-b border-slate-50 flex justify-between items-center bg-slate-50/30">
        <div>
          <router-link to="/board" class="text-blue-600 hover:text-blue-800 text-sm font-bold flex items-center gap-1 mb-2 transition-colors">
            &larr; Back to Board
          </router-link>
          <h1 v-if="!isEditing" class="text-3xl font-black text-slate-900 tracking-tight">{{ task.title }}</h1>
          <input 
            v-else 
            v-model="editedTask.title"
            type="text" 
            class="text-3xl font-black text-slate-900 tracking-tight bg-white border-b-2 border-blue-500 outline-none w-full"
          >
        </div>
        <button 
          @click="toggleEdit"
          class="px-4 py-2 rounded-lg font-bold transition-all"
          :class="isEditing ? 'bg-slate-200 text-slate-700 hover:bg-slate-300' : 'bg-blue-50 text-blue-600 hover:bg-blue-100'"
        >
          {{ isEditing ? 'Editing...' : 'Edit Task' }}
        </button>
      </div>

      <!-- Details Body -->
      <div class="p-8 space-y-8">
        <!-- Description -->
        <div>
          <h3 class="text-xs font-black text-slate-400 uppercase tracking-widest mb-3">Description</h3>
          <p v-if="!isEditing" class="text-slate-600 leading-relaxed text-lg whitespace-pre-wrap">
            {{ task.description || 'No description provided.' }}
          </p>
          <textarea 
            v-else 
            v-model="editedTask.description"
            rows="4"
            class="w-full p-4 bg-slate-50 rounded-xl border border-slate-200 focus:ring-2 focus:ring-blue-500 outline-none transition-all text-slate-700"
            placeholder="Add more details about this task..."
          ></textarea>
        </div>

        <!-- Meta Grid -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8 pt-8 border-t border-slate-50">
          <div>
            <h3 class="text-xs font-black text-slate-400 uppercase tracking-widest mb-3">Status</h3>
            <div v-if="!isEditing" class="inline-flex items-center px-3 py-1 rounded-full text-sm font-bold bg-slate-100 text-slate-600">
              {{ task.status }}
            </div>
            <select 
              v-else 
              v-model="editedTask.status"
              class="w-full p-2 bg-white rounded-lg border border-slate-200 focus:ring-2 focus:ring-blue-500 outline-none transition-all font-medium text-sm"
            >
              <option :value="TASK_STATUS.TODO">To Do</option>
              <option :value="TASK_STATUS.IN_PROGRESS">In Progress</option>
              <option :value="TASK_STATUS.DONE">Done</option>
            </select>
          </div>

          <div>
            <h3 class="text-xs font-black text-slate-400 uppercase tracking-widest mb-3">Priority</h3>
            <div 
              v-if="!isEditing" 
              class="inline-flex items-center px-3 py-1 rounded-full text-sm font-bold uppercase tracking-wider"
              :class="priorityColors[task.priority]"
            >
              {{ task.priority }}
            </div>
            <select 
              v-else 
              v-model="editedTask.priority"
              class="w-full p-2 bg-white rounded-lg border border-slate-200 focus:ring-2 focus:ring-blue-500 outline-none transition-all font-medium text-sm"
            >
              <option :value="TASK_PRIORITY.LOW">Low</option>
              <option :value="TASK_PRIORITY.MEDIUM">Medium</option>
              <option :value="TASK_PRIORITY.HIGH">High</option>
            </select>
          </div>

          <div>
            <h3 class="text-xs font-black text-slate-400 uppercase tracking-widest mb-3">Created At</h3>
            <div class="text-slate-500 text-sm font-medium">
              {{ new Date(task.createdAt).toLocaleDateString() }}
            </div>
          </div>
        </div>

        <!-- Save/Cancel Footer for Edit Mode -->
        <div v-if="isEditing" class="pt-8 flex gap-4">
          <button 
            @click="saveChanges"
            class="flex-1 bg-blue-600 text-white py-3 rounded-xl font-bold shadow-lg shadow-blue-200 hover:bg-blue-700 transition-all transform hover:-translate-y-0.5"
          >
            Save Changes
          </button>
          <button 
            @click="cancelEdit"
            class="flex-1 bg-white text-slate-600 border border-slate-200 py-3 rounded-xl font-bold hover:bg-slate-50 transition-all"
          >
            Cancel
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
