/**
 * Utility functions for formatting data
 */

/**
 * Format currency value
 * @param {number} value - Numeric value
 * @returns {string} Formatted currency string
 */
export const formatCurrency = (value) => {
  if (value === null || value === undefined) return 'N/A';
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD'
  }).format(value);
};

/**
 * Format date to readable string
 * @param {string} dateString - ISO date string
 * @returns {string} Formatted date string
 */
export const formatDate = (dateString) => {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  });
};

/**
 * Format date to relative time (e.g., "2 days ago")
 * @param {string} dateString - ISO date string
 * @returns {string} Relative time string
 */
export const formatRelativeTime = (dateString) => {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  const now = new Date();
  const diffInSeconds = Math.floor((now - date) / 1000);

  if (diffInSeconds < 60) return 'just now';
  if (diffInSeconds < 3600) return `${Math.floor(diffInSeconds / 60)} minutes ago`;
  if (diffInSeconds < 86400) return `${Math.floor(diffInSeconds / 3600)} hours ago`;
  if (diffInSeconds < 2592000) return `${Math.floor(diffInSeconds / 86400)} days ago`;
  return formatDate(dateString);
};

/**
 * Format number with thousands separator
 * @param {number} value - Numeric value
 * @returns {string} Formatted number string
 */
export const formatNumber = (value) => {
  if (value === null || value === undefined) return '0';
  return new Intl.NumberFormat('en-US').format(value);
};

/**
 * Truncate text to specified length
 * @param {string} text - Text to truncate
 * @param {number} maxLength - Maximum length
 * @returns {string} Truncated text
 */
export const truncateText = (text, maxLength = 100) => {
  if (!text) return '';
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + '...';
};

/**
 * Format ISBN with hyphens for readability
 * @param {string} isbn - ISBN string
 * @returns {string} Formatted ISBN
 */
export const formatISBN = (isbn) => {
  if (!isbn) return 'N/A';
  // Simple formatting - add hyphens if not present
  if (isbn.includes('-')) return isbn;

  if (isbn.length === 10) {
    return `${isbn.substring(0, 1)}-${isbn.substring(1, 5)}-${isbn.substring(5, 9)}-${isbn.substring(9)}`;
  } else if (isbn.length === 13) {
    return `${isbn.substring(0, 3)}-${isbn.substring(3, 4)}-${isbn.substring(4, 9)}-${isbn.substring(9, 12)}-${isbn.substring(12)}`;
  }
  return isbn;
};
