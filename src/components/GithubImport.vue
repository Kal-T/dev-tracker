<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useTaskStore, TASK_PRIORITY, TASK_STATUS } from '@/stores/taskStore'
import { useGithubIssues } from '@/composables/useGithubIssues'

const taskStore = useTaskStore()
const repoPath = ref('') // owner/repo
const lastImportedRepo = ref('')

// Vue Query hook
const { 
  isFetching, 
  isError, 
  error, 
  data: issues, 
  refetch 
} = useGithubIssues(repoPath)

onMounted(() => {
  const saved = localStorage.getItem('devtracker-last-repo')
  if (saved) {
    lastImportedRepo.value = saved
  }
})

const handleImport = () => {
  if (!issues.value) return

  issues.value.forEach((issue: any) => {
    if (issue.pull_request) return

    taskStore.addTask({
      title: issue.title,
      description: issue.body || 'No description provided on GitHub.',
      status: TASK_STATUS.TODO,
      priority: TASK_PRIORITY.MEDIUM,
      tags: ['github', ...issue.labels.map((l: any) => l.name)]
    })
  })

  lastImportedRepo.value = repoPath.value
  localStorage.setItem('devtracker-last-repo', repoPath.value)
}
</script>

<template>
  <div class="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm mb-8">
    <div class="flex flex-col md:flex-row md:items-end gap-4">
      <div class="flex-grow">
        <label class="block text-xs font-black text-slate-400 uppercase tracking-widest mb-2"
          >Import from GitHub</label
        >
        <div class="relative">
          <span class="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 font-medium"
            >github.com/</span
          >
          <input
            v-model="repoPath"
            type="text"
            placeholder="owner/repo"
            class="w-full pl-[104px] pr-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none transition-all font-medium"
          />
        </div>
      </div>
      <div class="flex gap-2">
        <button
          @click="handleImport"
          :disabled="isFetching || !issues || issues.length === 0"
          class="bg-slate-900 text-white px-6 py-2.5 rounded-xl font-bold hover:bg-slate-800 disabled:opacity-50 transition-all flex items-center gap-2 whitespace-nowrap h-[46px]"
        >
          <svg
            v-if="isFetching"
            class="animate-spin h-4 w-4 text-white"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
          >
            <circle
              class="opacity-25"
              cx="12"
              cy="12"
              r="10"
              stroke="currentColor"
              stroke-width="4"
            ></circle>
            <path
              class="opacity-75"
              fill="currentColor"
              d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
            ></path>
          </svg>
          {{ isFetching ? 'Fetching...' : 'Import Issues' }}
        </button>

        <button
          @click="() => refetch()"
          :disabled="isFetching || !repoPath.includes('/')"
          class="bg-blue-50 text-blue-600 px-4 py-2.5 rounded-xl font-bold hover:bg-blue-100 transition-all h-[46px]"
          title="Force Refresh"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-5 w-5"
            viewBox="0 0 20 20"
            fill="currentColor"
          >
            <path
              fill-rule="evenodd"
              d="M4 2a1 1 0 011 1v2.101a7.002 7.002 0 0111.601 2.566 1 1 0 11-1.885.666A5.002 5.002 0 005.999 7H9a1 1 0 110 2H4a1 1 0 01-1-1V3a1 1 0 011-1zm.008 9.057a1 1 0 011.276.61A5.002 5.002 0 0014.001 13H11a1 1 0 110-2h5a1 1 0 011 1v5a1 1 0 11-2 0v-2.101a7.002 7.002 0 01-11.601-2.566 1 1 0 01.61-1.276z"
              clip-rule="evenodd"
            />
          </svg>
        </button>
      </div>
    </div>

    <p v-if="isError" class="mt-3 text-sm font-semibold text-red-500 flex items-center gap-1">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="h-4 w-4"
        viewBox="0 0 20 20"
        fill="currentColor"
      >
        <path
          fill-rule="evenodd"
          d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z"
          clip-rule="evenodd"
        />
      </svg>
      {{ (error as any)?.response?.status === 404 ? 'Repo not found' : 'Fetch failed' }}
    </p>

    <p v-if="lastImportedRepo" class="mt-3 text-xs text-slate-400 italic">
      Last imported from: <span class="font-bold">{{ lastImportedRepo }}</span>
    </p>
  </div>
</template>
