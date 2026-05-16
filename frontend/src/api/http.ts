import axios from 'axios'

/**
 * Shared Axios instance for all DevTracker API calls.
 * The base URL is injected at build time via Vite env variables:
 *   - Development: http://localhost:8080  (from .env.development)
 *   - Production:  https://api.devtracker.com  (from .env.production)
 */
const http = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

export default http
