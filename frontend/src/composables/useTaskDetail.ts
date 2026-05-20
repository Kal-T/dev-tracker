import { ref, watch, type Ref } from 'vue'
import { useTaskDetailQuery, useUpdateTaskMutation } from './useTasks'

export function useTaskDetail(taskId: Ref<string>) {
  const isEditing = ref(false)

  const { data: task } = useTaskDetailQuery(taskId)
  const updateMutation = useUpdateTaskMutation()

  const editedTask = ref({
    title: '',
    description: '',
    status: 'todo',
    priority: 'medium'
  })

  watch(
    task,
    (newTask) => {
      if (newTask) {
        editedTask.value = {
          title: newTask.title,
          description: newTask.description,
          status: newTask.status,
          priority: newTask.priority
        }
      }
    },
    { immediate: true }
  )

  const toggleEdit = () => {
    isEditing.value = !isEditing.value
  }

  const saveChanges = () => {
    if (task.value) {
      updateMutation.mutate(
        {
          id: task.value.id,
          task: editedTask.value
        },
        {
          onSuccess: () => {
            isEditing.value = false
          }
        }
      )
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
