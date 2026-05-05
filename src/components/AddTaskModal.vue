<script setup lang="ts">
import { useForm, useField } from 'vee-validate'
import * as yup from 'yup'
import { useTaskStore } from '@/stores/taskStore'
import BaseModal from './BaseModal.vue'

const emit = defineEmits<{
  (e: 'close'): void
}>()

const taskStore = useTaskStore()

// Validation Schema
const schema = yup.object({
  title: yup.string().required('Title is required').min(3, 'Title must be at least 3 characters'),
  description: yup.string().optional(),
  priority: yup.string().oneOf(['low', 'medium', 'high']).required('Priority is required'),
  tags: yup.string().optional()
})

const { handleSubmit, errors, isSubmitting } = useForm({
  validationSchema: schema,
  initialValues: {
    priority: 'medium',
    tags: ''
  }
})

const { value: title } = useField<string>('title')
const { value: description } = useField<string>('description')
const { value: priority } = useField<string>('priority')
const { value: tagsInput } = useField<string>('tags')

const onSubmit = handleSubmit((values) => {
  // Process tags from comma-separated string to array
  const tagsArray = values.tags 
    ? values.tags.split(',').map(tag => tag.trim()).filter(tag => tag !== '')
    : []

  taskStore.addTask({
    title: values.title,
    description: values.description || '',
    priority: values.priority as 'low' | 'medium' | 'high',
    status: 'todo',
    tags: tagsArray
  })

  emit('close')
})
</script>

<template>
  <BaseModal @close="$emit('close')">
    <template #title>Add New Task</template>

    <form @submit="onSubmit" class="space-y-5">
      <!-- Title Field -->
      <div>
        <label class="block text-sm font-semibold text-slate-700 mb-1">Title</label>
        <input 
          v-model="title"
          type="text" 
          placeholder="e.g. Design System Audit"
          class="w-full px-4 py-2 rounded-lg border focus:ring-2 focus:ring-blue-500 outline-none transition-all"
          :class="errors.title ? 'border-red-500 bg-red-50' : 'border-slate-200'"
        >
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
            <option value="low">Low</option>
            <option value="medium">Medium</option>
            <option value="high">High</option>
          </select>
        </div>

        <!-- Tags Field -->
        <div>
          <label class="block text-sm font-semibold text-slate-700 mb-1">Tags (comma-separated)</label>
          <input 
            v-model="tagsInput"
            type="text" 
            placeholder="ui, fix, critical"
            class="w-full px-4 py-2 rounded-lg border border-slate-200 focus:ring-2 focus:ring-blue-500 outline-none transition-all"
          >
        </div>
      </div>

      <div class="pt-4 flex gap-3">
        <button 
          type="button"
          @click="$emit('close')"
          class="flex-1 px-4 py-2.5 rounded-lg border border-slate-200 text-slate-600 font-semibold hover:bg-slate-50 transition-colors"
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
