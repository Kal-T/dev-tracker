<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const email = ref('')
const password = ref('')

const handleLogin = () => {
  // Mock login
  console.log('Logging in...', email.value)
  router.push('/board')
}
</script>

<template>
  <div class="bg-white p-10 rounded-2xl shadow-xl w-full border border-slate-100">
    <div class="text-center mb-10">
        <h1 class="text-3xl font-bold text-slate-900">Welcome Back</h1>
        <p class="text-slate-500 mt-2">Sign in to DevTracker</p>
      </div>
      
      <form @submit.prevent="handleLogin" class="space-y-6">
        <div>
          <label class="block text-sm font-semibold text-slate-700 mb-2">Email Address</label>
          <input 
            v-model="email"
            type="email" 
            class="w-full px-4 py-3 rounded-lg border border-slate-200 focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
            placeholder="name@company.com"
          >
        </div>
        <div>
          <label class="block text-sm font-semibold text-slate-700 mb-2">Password</label>
          <input 
            v-model="password"
            type="password" 
            class="w-full px-4 py-3 rounded-lg border border-slate-200 focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none transition-all"
            placeholder="••••••••"
          >
        </div>
        <button 
          type="submit"
          class="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 rounded-lg shadow-lg hover:shadow-blue-200 transition-all transform hover:-translate-y-0.5"
        >
          Sign In
        </button>
      </form>
      
      <div class="mt-8 text-center text-sm text-slate-500">
        Don't have an account? <a href="#" class="text-blue-600 font-semibold hover:underline">Create one</a>
      </div>
    </div>
</template>
