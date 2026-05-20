<script setup lang="ts">
import { useForm, useField } from 'vee-validate'
import * as yup from 'yup'
import { useCreateTaskMutation } from '@/composables/useTasks'
import BaseModal from './BaseModal.vue'

const emit = defineEmits<{
  (e: 'close'): void
}>()

const createTaskMutation = useCreateTaskMutation()

const TASK_PRIORITY = {
  LOW: 'low',
  MEDIUM: 'medium',
  HIGH: 'high'
} as const

// Validation Schema
const schema = yup.object({
  title: yup.string().required('Title is required').min(3, 'Title must be at least 3 characters'),
  description: yup.string().optional(),
  priority: yup.string().oneOf(Object.values(TASK_PRIORITY)).required('Priority is required'),
  tags: yup.string().optional()
})

const { handleSubmit, errors, isSubmitting } = useForm({
  validationSchema: schema,
  initialValues: {
    title: '',
    description: '',
    priority: TASK_PRIORITY.MEDIUM,
    tags: ''
  }
})

const { value: title } = useField<string>('title')
const { value: description } = useField<string>('description')
const { value: priority } = useField<string>('priority')
const { value: tagsInput } = useField<string>('tags')

const onSubmit = handleSubmit((values) => {
  const tagsArray = values.tags
    ? String(values.tags)
        .split(',')
        .map((tag) => tag.trim())
        .filter((tag) => tag !== '')
    : []

  createTaskMutation.mutate(
    {
      title: values.title as string,
      description: (values.description as string) || '',
      priority: (values.priority as string).toUpperCase(),
      status: 'TODO',
      type: 'user',
      tags: tagsArray
    },
    {
      onSuccess: () => {
        emit('close')
      }
    }
  )
})
</script>

<template>
  <BaseModal @close="$emit('close')">
    <template #title>Add New Task</template>

    <form class="space-y-5" @submit="onSubmit">
      <!-- Title Field -->
      <div>
        <label class="block text-sm font-semibold text-slate-700 mb-1">Title</label>
        <input
          v-model="title"
          v-focus
          type="text"
          placeholder="e.g. Design System Audit"
          class="w-full px-4 py-2 rounded-lg border focus:ring-2 focus:ring-blue-500 outline-none transition-all"
          :class="errors.title ? 'border-red-500 bg-red-50' : 'border-slate-200'"
        />
        <span v-if="errors.title" class="text-xs text-red-500 mt-1 block">{{ errors.title }}</span>
      </div>

      <!-- Description Field -->
      <div>
        <label class="block text-sm font-semibold text-slate-700 mb-1">Description</label>
        <textarea
          v-model="description"
          rows="3"
          placeholder="What needs to be done?"
          class="w-full px-4 py-2 rounded-lg border border-slate-200 focus:ring-2 focus:ring-blue-500 outline-none transition-all"
        ></textarea>
      </div>

      <div class="grid grid-cols-2 gap-4">
        <!-- Priority Field -->
        <div>
          <label class="block text-sm font-semibold text-slate-700 mb-1">Priority</label>
          <select
            v-model="priority"
            class="w-full px-4 py-2 rounded-lg border border-slate-200 focus:ring-2 focus:ring-blue-500 outline-none transition-all bg-white"
          >
            <option :value="TASK_PRIORITY.LOW">Low</option>
            <option :value="TASK_PRIORITY.MEDIUM">Medium</option>
            <option :value="TASK_PRIORITY.HIGH">High</option>
          </select>
        </div>

        <!-- Tags Field -->
        <div>
          <label class="block text-sm font-semibold text-slate-700 mb-1"
            >Tags (comma-separated)</label
          >
          <input
            v-model="tagsInput"
            type="text"
            placeholder="ui, fix, critical"
            class="w-full px-4 py-2 rounded-lg border border-slate-200 focus:ring-2 focus:ring-blue-500 outline-none transition-all"
          />
        </div>
      </div>

      <div class="pt-4 flex gap-3">
        <button
          type="button"
          class="flex-1 px-4 py-2.5 rounded-lg border border-slate-200 text-slate-600 font-semibold hover:bg-slate-50 transition-colors"
          @click="$emit('close')"
        >
          Cancel
        </button>
        <button
          type="submit"
          :disabled="isSubmitting"
          class="flex-1 bg-blue-600 text-white px-4 py-2.5 rounded-lg font-bold shadow-lg hover:bg-blue-700 disabled:opacity-50 transition-all"
        >
          {{ isSubmitting ? 'Adding...' : 'Create Task' }}
        </button>
      </div>
    </form>
  </BaseModal>
</template>
