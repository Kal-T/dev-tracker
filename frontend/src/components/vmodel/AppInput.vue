<script setup lang="ts">
defineProps<{
  modelValue: string
  placeholder?: string
  label?: string
}>()

defineEmits<{
  (e: 'update:modelValue', value: string): void
}>()
</script>

<template>
  <div class="flex flex-col gap-1">
    <label v-if="label" class="text-xs font-semibold text-slate-500 uppercase tracking-wider">
      {{ label }}
    </label>

    <input
      :value="modelValue"
      :placeholder="placeholder"
      type="text"
      class="w-full px-4 py-2.5 rounded-xl border transition-all outline-none text-sm font-medium text-slate-800 placeholder:text-slate-400 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
      @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
    />

  </div>
</template>
