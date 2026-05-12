import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  define: {
    global: 'window',
  },
  server: {
    proxy: {
      '/api': 'http://localhost',
      '/graphql': 'http://localhost',
      '/ws-tracking': {
        target: 'http://localhost',
        ws: true
      }
    }
  }
})
