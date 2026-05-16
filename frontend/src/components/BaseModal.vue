<script setup lang="ts">
defineEmits<{
  (e: 'close'): void
}>()
</script>

<template>
  <Teleport to="body">
    <div
      class="fixed inset-0 z-[100] flex items-center justify-center p-4 sm:p-6"
      role="dialog"
      aria-modal="true"
    >
      <!-- Backdrop -->
      <div
        class="absolute inset-0 bg-slate-900/60 backdrop-blur-sm transition-opacity"
        @click="$emit('close')"
      ></div>

      <!-- Modal Content -->
      <div
        class="relative bg-white rounded-2xl shadow-2xl w-full max-w-lg transform transition-all overflow-hidden border border-slate-200"
      >
        <div
          class="px-6 py-4 border-b border-slate-100 flex justify-between items-center bg-slate-50/50"
        >
          <h3 class="text-lg font-bold text-slate-900">
            <slot name="title">Modal Title</slot>
          </h3>
          <button
            class="text-slate-400 hover:text-slate-600 p-1 rounded-full hover:bg-slate-100 transition-colors"
            @click="$emit('close')"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="h-6 w-6"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>
        </div>

        <div class="p-6">
          <slot />
        </div>

        <div v-if="$slots.footer" class="px-6 py-4 border-t border-slate-100 bg-slate-50/50">
          <slot name="footer" />
        </div>
      </div>
    </div>
  </Teleport>
</template>

<style scoped>
/* Add transitions if needed */
</style>
