import { defineStore } from 'pinia'
import { ref, watch, computed } from 'vue'

export const TASK_STATUS = {
  TODO: 'todo',
  IN_PROGRESS: 'in-progress',
  DONE: 'done'
} as const

export const TASK_PRIORITY = {
  LOW: 'low',
  MEDIUM: 'medium',
  HIGH: 'high'
} as const

export type TaskStatus = (typeof TASK_STATUS)[keyof typeof TASK_STATUS]
export type TaskPriority = (typeof TASK_PRIORITY)[keyof typeof TASK_PRIORITY]

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
  const tasks = ref<Task[]>([])

  const savedTasks = localStorage.getItem('devtracker-tasks')
  if (savedTasks) {
    try {
      tasks.value = JSON.parse(savedTasks)
    } catch (e) {
      console.error('Failed to parse tasks from localStorage', e)
      tasks.value = []
    }
  }

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
      [TASK_STATUS.TODO]: tasks.value.filter((t) => t.status === TASK_STATUS.TODO),
      [TASK_STATUS.IN_PROGRESS]: tasks.value.filter((t) => t.status === TASK_STATUS.IN_PROGRESS),
      [TASK_STATUS.DONE]: tasks.value.filter((t) => t.status === TASK_STATUS.DONE)
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
      createdAt: Date.now()
    }
    tasks.value.push(newTask)
  }


  function updateTask(id: string, updatedTask: Partial<Task>) {
    const index = tasks.value.findIndex((t) => t.id === id)
    if (index !== -1) {
      tasks.value[index] = { ...tasks.value[index], ...updatedTask } as Task
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
    moveTask
  }
})
