import './assets/main.css'
import { createApp, type DirectiveBinding } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import directives from './directives'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(directives)

app.mount('#app')
