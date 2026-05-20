<script setup lang="ts">
import { ref } from 'vue'
import AppInput from '@/components/vmodel/AppInput.vue'

const taskTitle = ref('')
</script>

<template>
  <div class="max-w-2xl mx-auto px-4 py-10 space-y-8">
    <section class="bg-white border border-slate-200 rounded-2xl shadow-sm p-6 space-y-5">
      <h2 class="text-base font-bold text-slate-800 flex items-center gap-2">
        <span
          class="w-6 h-6 rounded-full bg-blue-600 text-white text-xs font-black flex items-center justify-center"
          >▶</span
        >
        Demo
      </h2>

      <AppInput v-model="taskTitle" label="Task Title" placeholder="e.g. Fix login page bug" />

      <div class="flex items-center gap-2 text-sm pt-1">
        <span class="text-slate-400">Live value:</span>
        <code
          class="px-3 py-1 bg-blue-50 text-blue-700 rounded-lg font-mono border border-blue-100"
        >
          "{{ taskTitle }}"
        </code>
      </div>
    </section>
  </div>
</template>
