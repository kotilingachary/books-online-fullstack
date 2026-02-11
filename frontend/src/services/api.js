import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1';

/**
 * Axios instance configured for Books Online API
 *
 * Features:
 * - Base URL configuration
 * - Request/response interceptors
 * - Error handling
 * - Timeout configuration
 */
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

/**
 * Request interceptor
 * Add authentication token to requests (for V2)
 */
api.interceptors.request.use(
  (config) => {
    // For V2: Add JWT token from localStorage
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

/**
 * Response interceptor
 * Handle common error scenarios
 */
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      // Server responded with error status
      const { status, data } = error.response;

      switch (status) {
        case 401:
          // Unauthorized - redirect to login (V2)
          console.error('Unauthorized access');
          break;
        case 403:
          // Forbidden
          console.error('Access forbidden');
          break;
        case 404:
          // Not found
          console.error('Resource not found:', data.message);
          break;
        case 409:
          // Conflict (e.g., duplicate ISBN)
          console.error('Conflict:', data.message);
          break;
        case 500:
          // Server error
          console.error('Server error:', data.message);
          break;
        default:
          console.error('API error:', data.message || 'Unknown error');
      }
    } else if (error.request) {
      // Request made but no response
      console.error('No response from server');
    } else {
      // Error setting up request
      console.error('Request error:', error.message);
    }

    return Promise.reject(error);
  }
);

export default api;
