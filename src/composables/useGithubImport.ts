import { ref } from 'vue'
import { useTaskStore, TASK_PRIORITY, TASK_STATUS } from '@/stores/taskStore'
import { useGithubIssues } from './useGithubIssues'

const STORAGE_KEY = 'devtracker-last-repo'

export function useGithubImport() {
  const taskStore = useTaskStore()

  // Restore last used repo from localStorage on first call
  const repoPath = ref(localStorage.getItem(STORAGE_KEY) ?? '')
  const lastImportedRepo = ref(localStorage.getItem(STORAGE_KEY) ?? '')

  const { isFetching, isError, error, data: issues, refetch } = useGithubIssues(repoPath)

  const handleImport = () => {
    if (!issues.value) return

    issues.value
      .filter((issue) => !issue.pull_request)
      .forEach((issue) => {
        taskStore.addTask({
          title: issue.title,
          description: issue.body || 'No description provided on GitHub.',
          status: TASK_STATUS.TODO,
          priority: TASK_PRIORITY.MEDIUM,
          tags: ['github', ...issue.labels.map((l) => l.name)]
        })
      })

    lastImportedRepo.value = repoPath.value
    localStorage.setItem(STORAGE_KEY, repoPath.value)
  }

  return { repoPath, lastImportedRepo, isFetching, isError, error, issues, refetch, handleImport }
}
