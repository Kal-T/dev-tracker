import type { App } from 'vue'
import { vFocus } from './vFocus'
import { vPriorityColor } from './vPriorityColor'

export default {
  install(app: App) {
    app.directive('focus', vFocus)
    app.directive('priority-color', vPriorityColor)
  }
}
