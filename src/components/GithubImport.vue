<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useTaskStore, TASK_PRIORITY, TASK_STATUS } from '@/stores/taskStore'

const taskStore = useTaskStore()
const repoPath = ref('') // owner/repo
const isLoading = ref(false)
const errorMessage = ref('')
const lastImportedRepo = ref('')

onMounted(() => {
  const saved = localStorage.getItem('devtracker-last-repo')
  if (saved) {
    lastImportedRepo.value = saved
  }
})

const importIssues = async () => {
  if (!repoPath.value.includes('/')) {
    errorMessage.value = 'Please use owner/repo format'
    return
  }

  isLoading.value = true
  errorMessage.value = ''

  try {
    const response = await axios.get(`https://api.github.com/repos/${repoPath.value}/issues`, {
      params: {
        state: 'open',
        per_page: 10
      }
    })

    const issues = response.data

    if (issues.length === 0) {
      errorMessage.value = 'No open issues found in this repository.'
    } else {
      issues.forEach((issue: any) => {
        // Skip pull requests (GitHub API returns them as issues)
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
      repoPath.value = ''
    }
  } catch (err: any) {
    errorMessage.value =
      err.response?.status === 404
        ? 'Repository not found. Check the owner/repo path.'
        : 'Failed to fetch issues. You might be rate limited or the repo is private.'
  } finally {
    isLoading.value = false
  }
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
            @keyup.enter="importIssues"
          />
        </div>
      </div>
      <button
        :disabled="isLoading || !repoPath"
        class="bg-slate-900 text-white px-6 py-2.5 rounded-xl font-bold hover:bg-slate-800 disabled:opacity-50 transition-all flex items-center gap-2 whitespace-nowrap h-[46px]"
        @click="importIssues"
      >
        <svg
          v-if="isLoading"
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
        {{ isLoading ? 'Fetching...' : 'Import Issues' }}
      </button>
    </div>

    <p v-if="errorMessage" class="mt-3 text-sm font-semibold text-red-500 flex items-center gap-1">
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
      {{ errorMessage }}
    </p>

    <p v-if="lastImportedRepo" class="mt-3 text-xs text-slate-400 italic">
      Last imported from: <span class="font-bold">{{ lastImportedRepo }}</span>
    </p>
  </div>
</template>
