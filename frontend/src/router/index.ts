import { createRouter, createWebHistory } from 'vue-router'
import { defineAsyncComponent } from 'vue'
import BoardView from '../views/BoardView.vue'
// import TaskDetailView from '../views/TaskDetailView.vue'
// import StatsView from '../views/StatsView.vue'
// import LoginView from '../views/LoginView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/board'
    },
    {
      path: '/board',
      name: 'board',
      component: BoardView
    },
    {
      path: '/task/:id',
      name: 'task-detail',
      // component: TaskDetailView
      component: () => import('../views/TaskDetailView.vue')
    },
    {
      path: '/stats',
      name: 'stats',
      // component: StatsView
      component: defineAsyncComponent(() => import('../views/StatsView.vue'))
    },
    {
      path: '/login',
      name: 'login',
      // component: LoginView,
      component: () => import('../views/LoginView.vue'),
      meta: { layout: 'auth' }
    },

    // { path: '/examples', redirect: '/examples/vmodel' }
    // { path: '/examples', redirect: { name: 'vmodel-demo' } }

    // {
    //   path: '/examples',
    //   component: ExamplesLayout,        // contain <RouterView />
    //   children: [
    //     {
    //       path: 'vmodel',
    //       name: 'vmodel-demo',
    //       component: () => import('../views/VModelDemoView.vue')
    //     },
    //     {
    //       path: 'test'
    //       name: 'test-demo',
    //       component: () => import('../views/Test.vue')
    //     }
    //   ]
    // }

    // ── Active Route (used by the app) ───────────────────────────────────────
    {
      path: '/vmodel',
      name: 'vmodel-demo',
      component: () => import('../views/VModelDemoView.vue')
    }
  ]
})

export default router
