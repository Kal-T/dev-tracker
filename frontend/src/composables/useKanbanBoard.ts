import { ref, watch, computed } from 'vue'
import { useTasksQuery, useUpdateTaskStatusMutation, useDeleteTaskMutation } from './useTasks'

export const TASK_STATUS = {
  TODO: 'todo',
  IN_PROGRESS: 'in-progress',
  DONE: 'done'
} as const

export type TaskStatus = (typeof TASK_STATUS)[keyof typeof TASK_STATUS]

export function useKanbanBoard() {
  const searchQuery = ref('')
  const debouncedSearchQuery = ref('')
  const activeTypeFilter = ref<'all' | 'user' | 'github'>('all')

  let timeoutId: any
  watch(searchQuery, (newVal) => {
    clearTimeout(timeoutId)
    timeoutId = setTimeout(() => {
      debouncedSearchQuery.value = newVal
    }, 300)
  })

  const { data: response, isLoading } = useTasksQuery(undefined, debouncedSearchQuery)

  const updateStatusMutation = useUpdateTaskStatusMutation(undefined, debouncedSearchQuery)
  const deleteTaskMutation = useDeleteTaskMutation()

  const filteredTasks = computed(() => {
    const tasks = response.value?.data || []
    if (activeTypeFilter.value === 'all') return tasks
    return tasks.filter((t) => t.type === activeTypeFilter.value)
  })

  const todoTasks = computed({
    get: () => filteredTasks.value.filter((t) => t.status === 'todo'),
    set: () => {}
  })

  const inProgressTasks = computed({
    get: () => filteredTasks.value.filter((t) => t.status === 'in-progress'),
    set: () => {}
  })

  const doneTasks = computed({
    get: () => filteredTasks.value.filter((t) => t.status === 'done'),
    set: () => {}
  })

  const onDragChange = (event: any, newStatus: string) => {
    if (event.added) {
      const task = event.added.element
      updateStatusMutation.mutate({ id: task.id, status: newStatus })
    }
  }

  const handleMove = (id: string, newStatus: string) => {
    updateStatusMutation.mutate({ id, status: newStatus })
  }

  const handleDelete = (id: string) => {
    if (confirm('Are you sure you want to delete this task?')) {
      deleteTaskMutation.mutate(id)
    }
  }

  return {
    isLoading,
    searchQuery,
    activeTypeFilter,
    todoTasks,
    inProgressTasks,
    doneTasks,
    onDragChange,
    handleMove,
    handleDelete
  }
}
