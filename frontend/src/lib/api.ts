import axios from 'axios';

// Usamos el proxy de Vite para evitar CORS
const API_URL = '/api';

export const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});
