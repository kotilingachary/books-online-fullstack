import api from './api';

/**
 * Books API Service
 * Implements all endpoints from OpenAPI specification
 */
const booksService = {
  /**
   * Get all books with pagination and sorting
   * @param {Object} params - Query parameters { page, size, sort }
   * @returns {Promise} Page response with books
   */
  getAllBooks: (params = {}) => {
    const queryParams = {
      page: params.page || 0,
      size: params.size || 4,
      sort: params.sort || 'id,asc'
    };
    return api.get('/books', { params: queryParams });
  },

  /**
   * Get a single book by ID (increments view count)
   * @param {number} id - Book ID
   * @returns {Promise} Book response
   */
  getBookById: (id) => {
    return api.get(`/books/${id}`);
  },

  /**
   * Create a new book
   * @param {Object} bookData - Book request data
   * @returns {Promise} Created book response
   */
  createBook: (bookData) => {
    return api.post('/books', bookData);
  },

  /**
   * Update an existing book
   * @param {number} id - Book ID
   * @param {Object} bookData - Updated book request data
   * @returns {Promise} Updated book response
   */
  updateBook: (id, bookData) => {
    return api.put(`/books/${id}`, bookData);
  },

  /**
   * Delete a book
   * @param {number} id - Book ID
   * @returns {Promise} Empty response (204 No Content)
   */
  deleteBook: (id) => {
    return api.delete(`/books/${id}`);
  },

  /**
   * Advanced search with multiple filters
   * @param {Object} searchParams - Search filter parameters
   * @returns {Promise} Page response with matching books
   */
  searchBooks: (searchParams = {}) => {
    const params = {
      page: searchParams.page || 0,
      size: searchParams.size || 10,
      sort: searchParams.sort || 'id,asc',
      ...(searchParams.q && { q: searchParams.q }),
      ...(searchParams.title && { title: searchParams.title }),
      ...(searchParams.author && { author: searchParams.author }),
      ...(searchParams.isbn && { isbn: searchParams.isbn }),
      ...(searchParams.publisher && { publisher: searchParams.publisher }),
      ...(searchParams.genre && { genre: searchParams.genre }),
      ...(searchParams.language && { language: searchParams.language }),
      ...(searchParams.minYear && { minYear: searchParams.minYear }),
      ...(searchParams.maxYear && { maxYear: searchParams.maxYear }),
      ...(searchParams.minPrice && { minPrice: searchParams.minPrice }),
      ...(searchParams.maxPrice && { maxPrice: searchParams.maxPrice }),
      ...(searchParams.inStock !== undefined && { inStock: searchParams.inStock }),
      ...(searchParams.isAvailable !== undefined && { isAvailable: searchParams.isAvailable })
    };
    return api.get('/books/search', { params });
  },

  /**
   * Duplicate an existing book
   * @param {number} id - Book ID to duplicate
   * @returns {Promise} Duplicated book response
   */
  duplicateBook: (id) => {
    return api.post(`/books/${id}/duplicate`);
  },

  /**
   * Increment the wishlist count for a book
   * @param {number} id - Book ID
   * @returns {Promise} Updated book response
   */
  addToWishlist: (id) => {
    return api.post(`/books/${id}/wishlist`);
  },

  /**
   * Export book data
   * @param {number} id - Book ID
   * @param {string} format - Export format (json, csv, pdf)
   * @returns {Promise} Book data in specified format
   */
  exportBook: (id, format = 'json') => {
    return api.get(`/books/${id}/export`, {
      params: { format },
      responseType: format === 'json' ? 'json' : 'blob'
    });
  }
};

export default booksService;
