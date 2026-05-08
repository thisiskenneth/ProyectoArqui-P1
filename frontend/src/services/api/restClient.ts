import axios from 'axios';

const restClient = axios.create({
  baseURL: import.meta.env.VITE_API_REST_URL || 'http://localhost:3000',
  headers: {
    'Content-Type': 'application/json',
  },
});

restClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default restClient;
