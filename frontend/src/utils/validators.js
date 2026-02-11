import { z } from 'zod';

/**
 * Zod validation schemas for forms
 */

/**
 * Book form validation schema
 * Matches backend BookRequest validation
 */
export const bookFormSchema = z.object({
  title: z.string()
    .min(1, 'Title is required')
    .max(200, 'Title must not exceed 200 characters'),

  isbn: z.string()
    .min(10, 'ISBN must be at least 10 characters')
    .max(20, 'ISBN must not exceed 20 characters')
    .regex(/^[0-9-]+$/, 'ISBN must contain only digits and hyphens'),

  author: z.string()
    .min(1, 'Author is required')
    .max(100, 'Author must not exceed 100 characters'),

  genre: z.string()
    .min(1, 'Genre is required')
    .max(50, 'Genre must not exceed 50 characters'),

  publicationYear: z.coerce.number()
    .int('Publication year must be an integer')
    .min(1000, 'Publication year must be at least 1000')
    .max(2100, 'Publication year cannot exceed 2100'),

  language: z.string()
    .min(1, 'Language is required')
    .max(30, 'Language must not exceed 30 characters'),

  isAvailable: z.boolean(),

  // Optional fields
  publisher: z.string()
    .max(100, 'Publisher must not exceed 100 characters')
    .optional()
    .or(z.literal('')),

  pages: z.coerce.number()
    .int('Pages must be an integer')
    .min(1, 'Pages must be at least 1')
    .optional()
    .nullable(),

  description: z.string()
    .max(2000, 'Description must not exceed 2000 characters')
    .optional()
    .or(z.literal('')),

  coverImageUrl: z.string()
    .url('Must be a valid URL')
    .max(500, 'Cover image URL must not exceed 500 characters')
    .optional()
    .or(z.literal('')),

  price: z.coerce.number()
    .min(0, 'Price must be non-negative')
    .max(9999.99, 'Price cannot exceed 9999.99')
    .optional()
    .nullable(),

  stockQuantity: z.coerce.number()
    .int('Stock quantity must be an integer')
    .min(0, 'Stock quantity must be non-negative')
    .optional()
    .nullable(),

  rating: z.coerce.number()
    .min(0, 'Rating must be between 0 and 5')
    .max(5, 'Rating must be between 0 and 5')
    .optional()
    .nullable(),

  reviewCount: z.coerce.number()
    .int('Review count must be an integer')
    .min(0, 'Review count must be non-negative')
    .optional()
    .nullable()
});

/**
 * Search form validation schema
 */
export const searchFormSchema = z.object({
  q: z.string().optional(),
  title: z.string().optional(),
  author: z.string().optional(),
  isbn: z.string().optional(),
  publisher: z.string().optional(),
  genre: z.string().optional(),
  language: z.string().optional(),
  minYear: z.coerce.number().min(1000).max(2100).optional().nullable(),
  maxYear: z.coerce.number().min(1000).max(2100).optional().nullable(),
  minPrice: z.coerce.number().min(0).optional().nullable(),
  maxPrice: z.coerce.number().min(0).optional().nullable(),
  inStock: z.boolean().optional(),
  isAvailable: z.boolean().optional()
});
