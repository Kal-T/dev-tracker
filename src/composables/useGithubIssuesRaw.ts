import { shallowRef, type Ref } from 'vue'
import axios from 'axios'
import type { GithubIssue } from './useGithubIssues'

export function useGithubIssuesRaw(repo: Ref<string>) {
  const issues = shallowRef<GithubIssue[]>([])
  const isLoading = shallowRef(false)
  const error = shallowRef<string | null>(null)

  const fetchIssues = async () => {
    if (!repo.value.includes('/')) return
    
    isLoading.value = true
    error.value = null
    
    try {
      const response = await axios.get<GithubIssue[]>(
        `https://api.github.com/repos/${repo.value}/issues`
      )
      // shallowRef is great here because we don't need Vue to track 
      // every single property of every issue deeply. We only care 
      // if the whole 'issues' array is replaced.
      issues.value = response.data
    } catch (err) {
      error.value = 'Failed to fetch'
    } finally {
      isLoading.value = false
    }
  }

  return { issues, isLoading, error, fetchIssues }
}
