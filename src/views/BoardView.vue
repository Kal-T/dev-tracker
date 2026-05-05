<script setup lang="ts">
import { ref, computed } from 'vue'
import { useTaskStore, type TaskStatus } from '@/stores/taskStore'
import TaskCard from '@/components/TaskCard.vue'

const taskStore = useTaskStore()
const searchQuery = ref('')

// Filtered tasks based on search query
const filteredTasks = computed(() => {
  return taskStore.filteredTasks(searchQuery.value)
})

// Group filtered tasks by status
const columns = computed(() => {
  return {
    todo: {
      title: 'To Do',
      tasks: filteredTasks.value.filter((t) => t.status === 'todo'),
      color: 'bg-slate-100',
    },
    'in-progress': {
      title: 'In Progress',
      tasks: filteredTasks.value.filter((t) => t.status === 'in-progress'),
      color: 'bg-blue-50/50',
    },
    done: {
      title: 'Done',
      tasks: filteredTasks.value.filter((t) => t.status === 'done'),
      color: 'bg-green-50/50',
    },
  }
})

const handleMove = (id: string, newStatus: TaskStatus) => {
  taskStore.moveTask(id, newStatus)
}

const handleDelete = (id: string) => {
  if (confirm('Are you sure you want to delete this task?')) {
    taskStore.deleteTask(id)
  }
}

// Helper to add a mock task if the board is empty
const addSampleTask = () => {
  taskStore.addTask({
    title: 'New Feature Request',
    description: 'Implement dark mode toggle in the settings panel.',
    status: 'todo',
    priority: 'medium',
    tags: ['ui', 'ux'],
  })
}
</script>

<template>
  <div>
    <div class="flex flex-col md:flex-row md:items-center justify-between mb-8 gap-4">
      <h1 class="text-3xl font-black text-slate-900 tracking-tight">Project Board</h1>
      
      <div class="flex items-center gap-3">
        <div class="relative">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
          </svg>
          <input 
            v-model="searchQuery"
            type="text" 
            placeholder="Search tasks..." 
            class="pl-10 pr-4 py-2 bg-white border border-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none w-full md:w-64 transition-all"
          >
        </div>
        <button 
          @click="addSampleTask"
          class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg font-semibold shadow-md hover:shadow-blue-200 transition-all flex items-center gap-2 whitespace-nowrap"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clip-rule="evenodd" />
          </svg>
          Add Task
        </button>
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 items-start">
      <div 
        v-for="(col, key) in columns" 
        :key="key"
        class="rounded-2xl p-4 min-h-[500px]"
        :class="col.color"
      >
        <div class="flex items-center justify-between mb-4 px-2">
          <h2 class="font-bold text-slate-700 uppercase text-xs tracking-widest flex items-center gap-2">
            {{ col.title }}
            <span class="bg-white/50 px-2 py-0.5 rounded text-[10px]">{{ col.tasks.length }}</span>
          </h2>
        </div>

        <div class="space-y-4">
          <TaskCard 
            v-for="task in col.tasks" 
            :key="task.id" 
            :task="task"
            @move="handleMove"
            @delete="handleDelete"
          />
          
          <div 
            v-if="col.tasks.length === 0" 
            class="border-2 border-dashed border-slate-200 rounded-xl py-12 flex flex-col items-center justify-center text-slate-400"
          >
            <p class="text-xs font-medium">No tasks here</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Custom column scrolling if content overflows */
.grid > div {
  max-height: calc(100vh - 200px);
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: #cbd5e1 transparent;
}
</style>
