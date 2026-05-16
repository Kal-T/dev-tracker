import { ref, computed, watchEffect } from 'vue'
import { useRouter } from 'vue-router'
import {
  useTaskStore,
  TASK_STATUS,
  TASK_PRIORITY,
  type TaskStatus,
  type TaskPriority
} from '@/stores/taskStore'
import type { Ref } from 'vue'

export function useTaskDetail(taskId: Ref<string>) {
  const taskStore = useTaskStore()
  const router = useRouter()

  const isEditing = ref(false)

  const task = computed(() => taskStore.tasks.find((t) => t.id === taskId.value))

  const editedTask = ref({
    title: '',
    description: '',
    status: TASK_STATUS.TODO as TaskStatus,
    priority: TASK_PRIORITY.MEDIUM as TaskPriority
  })

  watchEffect(() => {
    if (task.value) {
      editedTask.value = {
        title: task.value.title,
        description: task.value.description,
        status: task.value.status,
        priority: task.value.priority
      }
    } else {
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

  return { task, isEditing, editedTask, toggleEdit, saveChanges, cancelEdit }
}
