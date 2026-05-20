import { computed } from 'vue'
import { useTasksQuery } from './useTasks'

export function useTaskStats() {
  const { data: response } = useTasksQuery(undefined, undefined)

  const stats = computed(() => {
    const tasks = response.value?.data || []
    const total = tasks.length
    const done = tasks.filter((t) => t.status === 'done').length
    const completionRate = total > 0 ? Math.round((done / total) * 100) : 0

    const priorityCounts = {
      low: tasks.filter((t) => t.priority === 'low').length,
      medium: tasks.filter((t) => t.priority === 'medium').length,
      high: tasks.filter((t) => t.priority === 'high').length
    }

    const typeCounts = {
      user: tasks.filter((t) => t.type === 'user').length,
      github: tasks.filter((t) => t.type === 'github').length
    }

    return { total, done, completionRate, priorityCounts, typeCounts }
  })

  const getBarWidth = (count: number): string => {
    return stats.value.total > 0 ? `${(count / stats.value.total) * 100}%` : '0%'
  }

  return { stats, getBarWidth }
}
