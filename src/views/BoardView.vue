<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import draggable from 'vuedraggable'
import { useTaskStore, TASK_STATUS, type TaskStatus } from '@/stores/taskStore'
import TaskCard from '@/components/TaskCard.vue'
import AddTaskModal from '@/components/AddTaskModal.vue'
import GithubImport from '@/components/GithubImport.vue'

const taskStore = useTaskStore()
const searchQuery = ref('')
const isModalOpen = ref(false)
const isLoading = ref(true) // Initial loading state

// Simulate initial data fetch
onMounted(() => {
  setTimeout(() => {
    isLoading.value = false
  }, 1000)
})

// Group filtered tasks by status for draggable
const todoTasks = computed({
  get: () =>
    taskStore.filteredTasks(searchQuery.value).filter((t) => t.status === TASK_STATUS.TODO),
  set: (val) => {}
})

const inProgressTasks = computed({
  get: () =>
    taskStore.filteredTasks(searchQuery.value).filter((t) => t.status === TASK_STATUS.IN_PROGRESS),
  set: (val) => {}
})

const doneTasks = computed({
  get: () =>
    taskStore.filteredTasks(searchQuery.value).filter((t) => t.status === TASK_STATUS.DONE),
  set: (val) => {}
})

const onDragChange = (event: any, newStatus: TaskStatus) => {
  if (event.added) {
    const task = event.added.element
    taskStore.moveTask(task.id, newStatus)
  }
}

const handleMove = (id: string, newStatus: TaskStatus) => {
  taskStore.moveTask(id, newStatus)
}

const handleDelete = (id: string) => {
  if (confirm('Are you sure you want to delete this task?')) {
    taskStore.deleteTask(id)
  }
}

const toggleModal = () => {
  isModalOpen.value = !isModalOpen.value
}
</script>

<template>
  <div>
    <div class="flex flex-col md:flex-row md:items-center justify-between mb-8 gap-4">
      <h1 class="text-3xl font-black text-slate-900 tracking-tight">Project Board</h1>

      <div class="flex items-center gap-3">
        <div class="relative">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-5 w-5 absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
            />
          </svg>
          <input
            v-model="searchQuery"
            type="text"
            placeholder="Search tasks..."
            class="pl-10 pr-4 py-2 bg-white border border-slate-200 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none w-full md:w-64 transition-all"
          />
        </div>
        <button
          class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg font-semibold shadow-md hover:shadow-blue-200 transition-all flex items-center gap-2 whitespace-nowrap"
          @click="toggleModal"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-5 w-5"
            viewBox="0 0 20 20"
            fill="currentColor"
          >
            <path
              fill-rule="evenodd"
              d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z"
              clip-rule="evenodd"
            />
          </svg>
          Add Task
        </button>
      </div>
    </div>

    <GithubImport />

    <!-- Skeleton Loading Overlay (v-show) -->
    <div v-show="isLoading" class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8 animate-pulse">
      <div v-for="i in 3" :key="i" class="bg-slate-200 h-64 rounded-2xl"></div>
    </div>

    <!-- Empty State Message (v-if) -->
    <div
      v-if="!isLoading && taskStore.taskCount === 0"
      class="bg-white border-2 border-dashed border-slate-200 rounded-3xl py-20 flex flex-col items-center justify-center text-center mb-8"
    >
      <div class="bg-slate-50 p-4 rounded-full mb-4">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="h-12 w-12 text-slate-300"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"
          />
        </svg>
      </div>
      <h3 class="text-xl font-bold text-slate-900 mb-1">No tasks yet</h3>
      <p class="text-slate-500 max-w-xs mx-auto">
        Get started by creating a new task or importing issues from GitHub.
      </p>
    </div>

    <!-- Kanban Grid -->
    <div v-show="!isLoading" class="grid grid-cols-1 md:grid-cols-3 gap-6 items-start">
      <!-- To Do Column -->
      <div class="rounded-2xl p-4 min-h-[500px] bg-slate-100">
        <h2
          class="font-bold text-slate-700 uppercase text-xs tracking-widest mb-4 px-2 flex items-center justify-between"
        >
          To Do <span class="bg-white/50 px-2 py-0.5 rounded">{{ todoTasks.length }}</span>
        </h2>
        <draggable
          v-model="todoTasks"
          group="tasks"
          item-key="id"
          class="space-y-4 min-h-[400px]"
          @change="(e: any) => onDragChange(e, TASK_STATUS.TODO)"
        >
          <template #item="{ element }">
            <TaskCard 
              v-memo="[element.status, element.priority, element.title]"
              :task="element" 
              @move="handleMove" 
              @delete="handleDelete" 
            />
          </template>
        </draggable>
      </div>

      <!-- In Progress Column -->
      <div class="rounded-2xl p-4 min-h-[500px] bg-blue-50/50">
        <h2
          class="font-bold text-slate-700 uppercase text-xs tracking-widest mb-4 px-2 flex items-center justify-between"
        >
          In Progress
          <span class="bg-white/50 px-2 py-0.5 rounded">{{ inProgressTasks.length }}</span>
        </h2>
        <draggable
          v-model="inProgressTasks"
          group="tasks"
          item-key="id"
          class="space-y-4 min-h-[400px]"
          @change="(e: any) => onDragChange(e, TASK_STATUS.IN_PROGRESS)"
        >
          <template #item="{ element }">
            <TaskCard 
              v-memo="[element.status, element.priority, element.title]"
              :task="element" 
              @move="handleMove" 
              @delete="handleDelete" 
            />
          </template>
        </draggable>
      </div>

      <!-- Done Column -->
      <div class="rounded-2xl p-4 min-h-[500px] bg-green-50/50">
        <h2
          class="font-bold text-slate-700 uppercase text-xs tracking-widest mb-4 px-2 flex items-center justify-between"
        >
          Done <span class="bg-white/50 px-2 py-0.5 rounded">{{ doneTasks.length }}</span>
        </h2>
        <draggable
          v-model="doneTasks"
          group="tasks"
          item-key="id"
          class="space-y-4 min-h-[400px]"
          @change="(e: any) => onDragChange(e, TASK_STATUS.DONE)"
        >
          <template #item="{ element }">
            <TaskCard 
              v-memo="[element.status, element.priority, element.title]"
              :task="element" 
              @move="handleMove" 
              @delete="handleDelete" 
            />
          </template>
        </draggable>
      </div>
    </div>

    <!-- Modals -->
    <AddTaskModal v-if="isModalOpen" @close="toggleModal" />
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
