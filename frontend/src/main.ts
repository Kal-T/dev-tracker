import './assets/main.css'
import { createApp, type DirectiveBinding } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import { VueQueryPlugin, QueryClient } from '@tanstack/vue-query'
import directives from './directives'

const app = createApp(App)
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 1000 * 60 * 5,
      retry: 2
    }
  }
})

app.use(createPinia())
app.use(router)
app.use(directives)
app.use(VueQueryPlugin, { queryClient })

app.mount('#app')
