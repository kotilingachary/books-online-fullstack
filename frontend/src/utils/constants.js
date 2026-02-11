/**
 * Application constants
 */

export const GENRES = [
  'Programming',
  'Science Fiction',
  'Fiction',
  'Business',
  'Self-Help',
  'History',
  'Biography',
  'Mystery',
  'Romance',
  'Fantasy',
  'Non-Fiction',
  'Poetry',
  'Drama',
  'Horror',
  'Thriller',
  'Children',
  'Young Adult',
  'Science',
  'Philosophy',
  'Religion'
];

export const LANGUAGES = [
  'English',
  'Spanish',
  'French',
  'German',
  'Chinese',
  'Japanese',
  'Korean',
  'Hindi',
  'Arabic',
  'Portuguese',
  'Russian',
  'Italian',
  'Dutch',
  'Swedish',
  'Polish'
];

export const PAGE_SIZES = [4, 10, 20, 50];

export const SORT_OPTIONS = [
  { value: 'id,asc', label: 'ID (Ascending)' },
  { value: 'id,desc', label: 'ID (Descending)' },
  { value: 'title,asc', label: 'Title (A-Z)' },
  { value: 'title,desc', label: 'Title (Z-A)' },
  { value: 'author,asc', label: 'Author (A-Z)' },
  { value: 'author,desc', label: 'Author (Z-A)' },
  { value: 'publicationYear,desc', label: 'Year (Newest First)' },
  { value: 'publicationYear,asc', label: 'Year (Oldest First)' },
  { value: 'rating,desc', label: 'Rating (Highest First)' },
  { value: 'price,asc', label: 'Price (Low to High)' },
  { value: 'price,desc', label: 'Price (High to Low)' }
];

export const CURRENT_YEAR = new Date().getFullYear();
export const MIN_YEAR = 1000;
export const MAX_YEAR = 2100;
