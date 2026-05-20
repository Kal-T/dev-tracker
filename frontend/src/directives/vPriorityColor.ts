import type { Directive, DirectiveBinding } from 'vue'
import { TASK_PRIORITY } from '@/composables/useTasks'

const applyPriorityStyle = (el: HTMLElement, priority: string) => {
  const colors: Record<string, string> = {
    [TASK_PRIORITY.HIGH]: '#ef4444',
    [TASK_PRIORITY.MEDIUM]: '#f59e0b',
    [TASK_PRIORITY.LOW]: '#10b981'
  }
  el.style.borderLeft = `6px solid ${colors[priority] || '#cbd5e1'}`
}

export const vPriorityColor: Directive = {
  mounted: (el: HTMLElement, binding: DirectiveBinding<string>) => {
    applyPriorityStyle(el, binding.value)
  },
  updated: (el: HTMLElement, binding: DirectiveBinding<string>) => {
    applyPriorityStyle(el, binding.value)
  },
  unmounted: (el: HTMLElement) => {
    el.style.borderLeft = ''
  }
}
