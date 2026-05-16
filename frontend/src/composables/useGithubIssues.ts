import { useQuery } from '@tanstack/vue-query'
import axios from 'axios'
import type { Ref } from 'vue'

export interface GithubIssue {
  id: number
  title: string
  body: string
  pull_request?: any
  labels: { name: string }[]
}

export function useGithubIssues(repo: Ref<string>) {
  return useQuery({
    queryKey: ['github-issues', repo],
    queryFn: async () => {
      if (!repo.value || !repo.value.includes('/')) return []

      console.log(`[Vue Query] Fetching issues for: ${repo.value}...`)
      const response = await axios.get<GithubIssue[]>(
        `https://api.github.com/repos/${repo.value}/issues`,
        {
          params: { state: 'open', per_page: 10 }
        }
      )
      return response.data
    },
    enabled: () => repo.value.includes('/')
  })
}
