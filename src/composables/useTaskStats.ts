import { computed } from 'vue'
import { useTaskStore, TASK_STATUS, TASK_PRIORITY } from '@/stores/taskStore'

export function useTaskStats() {
  const taskStore = useTaskStore()

  const stats = computed(() => {
    const total = taskStore.taskCount
    const done = taskStore.tasks.filter((t) => t.status === TASK_STATUS.DONE).length
    const completionRate = total > 0 ? Math.round((done / total) * 100) : 0

    const priorityCounts = {
      [TASK_PRIORITY.LOW]: taskStore.tasks.filter((t) => t.priority === TASK_PRIORITY.LOW).length,
      [TASK_PRIORITY.MEDIUM]: taskStore.tasks.filter((t) => t.priority === TASK_PRIORITY.MEDIUM)
        .length,
      [TASK_PRIORITY.HIGH]: taskStore.tasks.filter((t) => t.priority === TASK_PRIORITY.HIGH).length
    }

    return { total, done, completionRate, priorityCounts }
  })

  const getBarWidth = (count: number): string => {
    return stats.value.total > 0 ? `${(count / stats.value.total) * 100}%` : '0%'
  }

  return { stats, getBarWidth }
}
