<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()
const taskId = route.params.id
</script>

<template>
  <div class="p-6 max-w-4xl mx-auto">
    <div class="bg-white rounded-xl shadow-lg p-8 border border-slate-200">
      <router-link to="/board" class="text-blue-600 hover:text-blue-800 text-sm font-medium mb-4 inline-block">&larr; Back to Board</router-link>
      <h1 class="text-3xl font-bold text-slate-900 mb-4">Task Details #{{ taskId }}</h1>
      <div class="space-y-6">
        <div>
          <h3 class="text-sm font-semibold text-slate-500 uppercase tracking-wider">Description</h3>
          <p class="mt-2 text-slate-700 leading-relaxed">
            This is a placeholder for the task description. In a real app, this data would be fetched from a store or API using the task ID from the route.
          </p>
        </div>
        <div class="grid grid-cols-2 gap-8">
          <div>
            <h3 class="text-sm font-semibold text-slate-500 uppercase tracking-wider">Status</h3>
            <span class="mt-2 inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-blue-100 text-blue-800">
              In Progress
            </span>
          </div>
          <div>
            <h3 class="text-sm font-semibold text-slate-500 uppercase tracking-wider">Assignee</h3>
            <p class="mt-2 text-slate-700">John Doe</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
