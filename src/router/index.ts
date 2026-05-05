import { createRouter, createWebHistory } from 'vue-router'
import { defineAsyncComponent } from 'vue'
import BoardView from '../views/BoardView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/board',
    },
    {
      path: '/board',
      name: 'board',
      component: BoardView,
    },
    {
      path: '/task/:id',
      name: 'task-detail',
      component: () => import('../views/TaskDetailView.vue'),
    },
    {
      path: '/stats',
      name: 'stats',
      component: defineAsyncComponent(() => import('../views/StatsView.vue')),
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
      meta: { layout: 'auth' },
    },
  ],
})

export default router
