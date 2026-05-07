import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import { createRouter, createMemoryHistory } from 'vue-router'
import { createPinia } from 'pinia'
import App from '../App.vue'

const router = createRouter({
  history: createMemoryHistory(),
  routes: [{ path: '/', component: { template: '<div />' } }]
})

describe('App', () => {
  it('mounts without errors', async () => {
    const wrapper = mount(App, {
      global: {
        plugins: [router, createPinia()]
      }
    })

    // Wait for the router's initial navigation to settle
    await router.isReady()

    // App.vue renders a dynamic layout component — confirm the root element exists
    expect(wrapper.element).toBeTruthy()
  })
})
