import { defineStore } from 'pinia'
import { ref, watch, computed } from 'vue'

export type TaskStatus = 'todo' | 'in-progress' | 'done'
export type TaskPriority = 'low' | 'medium' | 'high'

export interface Task {
  id: string
  title: string
  description: string
  status: TaskStatus
  priority: TaskPriority
  tags: string[]
  createdAt: number
}

export const useTaskStore = defineStore('task', () => {
  // State
  const tasks = ref<Task[]>([])

  // Load from localStorage on initialization
  const savedTasks = localStorage.getItem('devtracker-tasks')
  if (savedTasks) {
    try {
      tasks.value = JSON.parse(savedTasks)
    } catch (e) {
      console.error('Failed to parse tasks from localStorage', e)
      tasks.value = []
    }
  }

  // Persistence Watcher
  watch(
    tasks,
    (newTasks) => {
      localStorage.setItem('devtracker-tasks', JSON.stringify(newTasks))
    },
    { deep: true }
  )

  // Getters
  const taskCount = computed(() => tasks.value.length)

  const tasksByStatus = computed(() => {
    return {
      todo: tasks.value.filter((t) => t.status === 'todo'),
      'in-progress': tasks.value.filter((t) => t.status === 'in-progress'),
      done: tasks.value.filter((t) => t.status === 'done'),
    }
  })

  const filteredTasks = (query: string) => {
    if (!query) return tasks.value
    const q = query.toLowerCase()
    return tasks.value.filter(
      (t) => t.title.toLowerCase().includes(q) || t.description.toLowerCase().includes(q)
    )
  }

  // Actions
  function addTask(task: Omit<Task, 'id' | 'createdAt'>) {
    const newTask: Task = {
      ...task,
      id: crypto.randomUUID(),
      createdAt: Date.now(),
    }
    tasks.value.push(newTask)
  }

  function updateTask(id: string, updatedTask: Partial<Task>) {
    const index = tasks.value.findIndex((t) => t.id === id)
    if (index !== -1) {
      tasks.value[index] = { ...tasks.value[index], ...updatedTask }
    }
  }

  function deleteTask(id: string) {
    tasks.value = tasks.value.filter((t) => t.id !== id)
  }

  function moveTask(id: string, newStatus: TaskStatus) {
    updateTask(id, { status: newStatus })
  }

  return {
    tasks,
    taskCount,
    tasksByStatus,
    filteredTasks,
    addTask,
    updateTask,
    deleteTask,
    moveTask,
  }
})
