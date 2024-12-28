import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/users': { // Proxy all requests starting with "/users"
        target: 'http://localhost:8080', // Backend server
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/users/, '/users') // Optional: rewrites if needed
      }
    }
  }
}); 
