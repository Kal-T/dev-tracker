import { ref, computed } from 'vue'
import { useTaskStore, TASK_STATUS, type TaskStatus } from '@/stores/taskStore'

export function useKanbanBoard() {
  const taskStore = useTaskStore()
  const searchQuery = ref('')

  const todoTasks = computed({
    get: () =>
      taskStore.filteredTasks(searchQuery.value).filter((t) => t.status === TASK_STATUS.TODO),
    set: () => {}
  })

  const inProgressTasks = computed({
    get: () =>
      taskStore
        .filteredTasks(searchQuery.value)
        .filter((t) => t.status === TASK_STATUS.IN_PROGRESS),
    set: () => {}
  })

  const doneTasks = computed({
    get: () =>
      taskStore.filteredTasks(searchQuery.value).filter((t) => t.status === TASK_STATUS.DONE),
    set: () => {}
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

  return {
    taskStore,
    searchQuery,
    todoTasks,
    inProgressTasks,
    doneTasks,
    onDragChange,
    handleMove,
    handleDelete
  }
}
