import { ref, watch } from 'vue'
import { useCreateTaskMutation, TASK_PRIORITY, TASK_STATUS } from './useTasks'
import { useGithubIssues } from './useGithubIssues'

const STORAGE_KEY = 'devtracker-last-repo'

const repoPath = ref(localStorage.getItem(STORAGE_KEY) ?? '')
const lastImportedRepo = ref(localStorage.getItem(STORAGE_KEY) ?? '')

watch(lastImportedRepo, (newVal) => {
  localStorage.setItem(STORAGE_KEY, newVal)
})

export function useGithubImport() {
  const createTaskMutation = useCreateTaskMutation()

  const { isFetching, isError, error, data: issues, refetch } = useGithubIssues(repoPath)

  const handleImport = () => {
    if (!issues.value) return

    issues.value
      .filter((issue) => !issue.pull_request)
      .forEach((issue) => {
        let titleText = (issue.title || '').trim()
        if (titleText.length < 3) {
          titleText = titleText ? `${titleText} Issue` : 'GitHub Issue'
        }
        if (titleText.length > 100) {
          titleText = titleText.substring(0, 97) + '...'
        }

        const bodyText = issue.body || ''
        const truncatedDescription =
          bodyText.length > 500
            ? bodyText.substring(0, 497) + '...'
            : bodyText || 'No description provided on GitHub.'

        createTaskMutation.mutate({
          title: titleText,
          description: truncatedDescription,
          status: TASK_STATUS.TODO,
          priority: TASK_PRIORITY.MEDIUM,
          type: 'github',
          tags: ['github', ...issue.labels.map((l) => l.name)]
        })
      })

    lastImportedRepo.value = repoPath.value
  }

  return { repoPath, lastImportedRepo, isFetching, isError, error, issues, refetch, handleImport }
}
