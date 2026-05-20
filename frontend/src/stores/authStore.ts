import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import http from '@/api/http'

export interface UserSession {
  email: string
  displayName?: string
}

function parseJwt(token: string): any {
  try {
    const parts = token.split('.')
    if (parts.length < 2) {
      throw new Error('Invalid JWT token structure')
    }
    const base64Url = parts[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    )
    return JSON.parse(jsonPayload)
  } catch (e) {
    console.warn('Failed to parse JWT token:', e)
    return null
  }
}

export const useAuthStore = defineStore('auth', () => {
  const user = ref<UserSession | null>(null)
  const accessToken = ref<string | null>(null)

  const isAuthenticated = computed(() => !!accessToken.value)

  const savedUser = localStorage.getItem('devtracker-user')
  if (savedUser) {
    try {
      user.value = JSON.parse(savedUser)
    } catch (e) {
      user.value = null
    }
  }

  async function register(displayName: string, email: string, password: string) {
    const response = await http.post('/api/auth/register', {
      displayName,
      email,
      password
    })
    const { accessToken: access } = response.data
    accessToken.value = access

    const decoded = parseJwt(access)
    const sessionUser: UserSession = {
      email: decoded?.sub || email,
      displayName: decoded?.displayName || displayName
    }
    user.value = sessionUser
    localStorage.setItem('devtracker-user', JSON.stringify(sessionUser))
  }

  async function login(email: string, password: string) {
    const response = await http.post('/api/auth/login', {
      email,
      password
    })
    const { accessToken: access } = response.data
    accessToken.value = access

    const decoded = parseJwt(access)
    const sessionUser: UserSession = {
      email: decoded?.sub || email,
      displayName: decoded?.displayName || email.split('@')[0]
    }
    user.value = sessionUser
    localStorage.setItem('devtracker-user', JSON.stringify(sessionUser))
  }

  async function logout() {
    try {
      await http.post('/api/auth/logout', null)
    } catch (e) {
      console.warn('Server-side token invalidation skipped:', e)
    } finally {
      accessToken.value = null
      user.value = null
      localStorage.removeItem('devtracker-user')
    }
  }

  async function refreshSession() {
    const response = await http.post('/api/auth/refresh', null)
    const { accessToken: newAccess } = response.data
    accessToken.value = newAccess

    const decoded = parseJwt(newAccess)
    if (decoded) {
      const sessionUser: UserSession = {
        email: decoded.sub,
        displayName: decoded.displayName
      }
      user.value = sessionUser
      localStorage.setItem('devtracker-user', JSON.stringify(sessionUser))
    }

    return newAccess
  }

  return {
    user,
    accessToken,
    isAuthenticated,
    register,
    login,
    logout,
    refreshSession
  }
})
