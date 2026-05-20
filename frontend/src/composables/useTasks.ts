import { useQuery, useMutation, useQueryClient } from '@tanstack/vue-query'
import { toValue, type Ref } from 'vue'
import http from '@/api/http'

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
export type TaskType = 'user' | 'github'

export interface Task {
  id: string
  title: string
  description: string
  status: TaskStatus
  priority: TaskPriority
  type: TaskType
  tags: string[]
  ownerEmail: string
  createdAt?: string
  updatedAt?: string
}

export interface PaginatedResponse<T> {
  data: T[]
  totalElements: number
  page: number
  totalPages: number
}

export function normalizeTask(task: any): Task {
  return {
    ...task,
    status: task.status ? task.status.toLowerCase().replace('_', '-') : 'todo',
    priority: task.priority ? task.priority.toLowerCase() : 'medium',
    type: task.type ? task.type.toLowerCase() : 'user'
  }
}

export function toBackendStatus(status?: string): string | undefined {
  if (!status) return undefined
  return status.toUpperCase().replace('-', '_')
}

export function useTasksQuery(
  status: Ref<string | undefined> | string | undefined,
  search: Ref<string | undefined> | string | undefined
) {
  return useQuery({
    queryKey: ['tasks', { status, search }],
    queryFn: async () => {
      const rawStatus = toValue(status)
      const rawSearch = toValue(search)

      const response = await http.get<PaginatedResponse<Task>>('/api/tasks', {
        params: {
          status: toBackendStatus(rawStatus),
          search: rawSearch,
          page: 0,
          size: 100
        }
      })

      return {
        ...response.data,
        data: response.data.data.map(normalizeTask)
      }
    }
  })
}

export function useTaskDetailQuery(id: string | Ref<string>) {
  return useQuery({
    queryKey: ['task', id],
    queryFn: async () => {
      const taskId = toValue(id)
      if (!taskId) return null
      const response = await http.get<Task>(`/api/tasks/${taskId}`)
      return normalizeTask(response.data)
    },
    enabled: () => !!toValue(id)
  })
}

export function useTaskStatsQuery() {
  return useQuery({
    queryKey: ['task-stats'],
    queryFn: async () => {
      const response = await http.get<Record<string, number>>('/api/tasks/stats')

      const raw = response.data
      const translated: Record<string, number> = {}
      for (const key in raw) {
        const localKey = key.toLowerCase().replace('_', '-')
        translated[localKey] = raw[key] ?? 0
      }
      return translated
    }
  })
}

export function useCreateTaskMutation() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: async (task: Omit<Task, 'id' | 'ownerEmail'>) => {
      const payload = {
        ...task,
        status: toBackendStatus(task.status) || 'TODO',
        priority: task.priority.toUpperCase(),
        type: task.type ? task.type.toUpperCase() : 'USER'
      }
      const response = await http.post<Task>('/api/tasks', payload)
      return normalizeTask(response.data)
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['tasks'] })
      queryClient.invalidateQueries({ queryKey: ['task-stats'] })
    }
  })
}

export function useUpdateTaskMutation() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: async ({ id, task }: { id: string; task: Partial<Task> }) => {
      const payload: any = { ...task }
      if (task.status) payload.status = toBackendStatus(task.status)
      if (task.priority) payload.priority = task.priority.toUpperCase()

      const response = await http.put<Task>(`/api/tasks/${id}`, payload)
      return normalizeTask(response.data)
    },
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ['tasks'] })
      queryClient.invalidateQueries({ queryKey: ['task-stats'] })
      queryClient.invalidateQueries({ queryKey: ['task', data.id] })
    }
  })
}

export function useUpdateTaskStatusMutation(
  statusRef?: Ref<string | undefined>,
  searchRef?: Ref<string | undefined>
) {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: async ({ id, status }: { id: string; status: string }) => {
      const response = await http.patch<Task>(`/api/tasks/${id}/status`, {
        status: toBackendStatus(status)
      })
      return normalizeTask(response.data)
    },
    onMutate: async ({ id, status }) => {
      const queryKey = ['tasks', { status: statusRef, search: searchRef }]

      await queryClient.cancelQueries({ queryKey })
      const previousTasks = queryClient.getQueryData<PaginatedResponse<Task>>(queryKey)
      if (previousTasks) {
        queryClient.setQueryData<PaginatedResponse<Task>>(queryKey, {
          ...previousTasks,
          data: previousTasks.data.map((task) =>
            task.id === id ? { ...task, status: status as TaskStatus } : task
          )
        })
      }

      return { previousTasks, queryKey }
    },
    onError: (err, newValues, context) => {
      if (context?.previousTasks) {
        queryClient.setQueryData(context.queryKey, context.previousTasks)
      }
    },
    onSettled: (data) => {
      queryClient.invalidateQueries({ queryKey: ['tasks'] })
      queryClient.invalidateQueries({ queryKey: ['task-stats'] })
      if (data) {
        queryClient.invalidateQueries({ queryKey: ['task', data.id] })
      }
    }
  })
}

export function useDeleteTaskMutation() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: async (id: string) => {
      await http.delete(`/api/tasks/${id}`)
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['tasks'] })
      queryClient.invalidateQueries({ queryKey: ['task-stats'] })
    }
  })
}
