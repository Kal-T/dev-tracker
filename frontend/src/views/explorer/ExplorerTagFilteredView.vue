<script setup lang="ts">
import { ref, watch, computed } from 'vue'
import { useRoute, RouterLink } from 'vue-router'
import { useTasksQuery } from '@/composables/useTasks'

const route = useRoute()
const currentTag = ref<string>(route.params.tag as string)

watch(
  () => route.params.tag,
  (newTag) => {
    if (newTag) {
      currentTag.value = newTag as string
    }
  }
)

const status = ref<string | undefined>(undefined)
const search = ref<string | undefined>(undefined)

const { data: tasksResponse, isLoading } = useTasksQuery(status, search)

const filteredTasks = computed(() => {
  const allTasks = tasksResponse.value?.data || []
  return allTasks.filter(task => task.tags && task.tags.includes(currentTag.value))
})

const allTags = computed(() => {
  const tagsSet = new Set<string>()
  const allTasks = tasksResponse.value?.data || []
  allTasks.forEach(t => {
    if (t.tags) t.tags.forEach(tag => tagsSet.add(tag))
  })
  if (tagsSet.size === 0) {
    return ['feature', 'bug', 'enhancement']
  }
  return Array.from(tagsSet)
})

const priorityColors = {
  low: 'bg-green-50 text-green-700 border-green-200',
  medium: 'bg-yellow-50 text-yellow-700 border-yellow-200',
  high: 'bg-red-50 text-red-700 border-red-200'
}
</script>

<template>
  <div class="space-y-4 animate-in fade-in duration-150">
    <div class="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm space-y-4">
      <h2 class="text-lg font-bold text-slate-900 flex items-center gap-2">
        Tag: 
        <span class="bg-indigo-50 border border-indigo-200 text-indigo-700 px-2 py-0.5 rounded-lg text-xs font-bold">
          #{{ currentTag }}
        </span>
      </h2>

      <!-- Switch tag list -->
      <div class="flex flex-wrap gap-1.5 pt-2 border-t border-slate-100">
        <RouterLink
          v-for="tag in allTags"
          :key="tag"
          :to="'/explorer/tags/' + tag"
          class="text-xs px-2.5 py-1 rounded-lg border font-medium transition-all"
          :class="tag === currentTag 
            ? 'bg-indigo-600 text-white border-indigo-600 shadow-sm'
            : 'bg-slate-50 text-slate-600 border-slate-200 hover:bg-slate-100'
          "
        >
          #{{ tag }}
        </RouterLink>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="isLoading" class="space-y-2">
      <div v-for="i in 2" :key="i" class="bg-white h-12 rounded-xl animate-pulse"></div>
    </div>

    <!-- Empty -->
    <div v-else-if="filteredTasks.length === 0" class="bg-white p-8 rounded-2xl text-center border border-slate-200">
      <p class="text-sm text-slate-500">No tasks containing tag #{{ currentTag }}.</p>
    </div>

    <!-- Results List -->
    <div v-else class="space-y-2">
      <div
        v-for="task in filteredTasks"
        :key="task.id"
        class="bg-white border border-slate-200 rounded-xl p-4 flex items-center justify-between hover:border-slate-300 shadow-sm transition-all"
      >
        <div class="flex items-center gap-3">
          <span class="text-[10px] font-bold px-2 py-0.5 rounded-full border uppercase" :class="priorityColors[task.priority]">
            {{ task.priority }}
          </span>
          <span class="font-bold text-slate-800 text-sm">{{ task.title }}</span>
        </div>

        <RouterLink
          :to="'/explorer/tasks/' + task.id"
          class="text-xs font-bold text-indigo-600 hover:text-indigo-800"
        >
          View details &rarr;
        </RouterLink>
      </div>
    </div>
  </div>
</template>
