import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import booksService from '../services/booksService';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Loading from '../components/common/Loading';
import Pagination from '../components/common/Pagination';
import Modal from '../components/common/Modal';
import { formatCurrency, formatDate } from '../utils/formatters';
import { SORT_OPTIONS } from '../utils/constants';

/**
 * Books list page - displays all books with pagination
 */
const BooksListPage = () => {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(4);
  const [sort, setSort] = useState('id,asc');
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [deleteModalOpen, setDeleteModalOpen] = useState(false);
  const [bookToDelete, setBookToDelete] = useState(null);

  // Fetch books
  const fetchBooks = async () => {
    try {
      setLoading(true);
      const response = await booksService.getAllBooks({ page, size, sort });
      setBooks(response.data.content);
      setTotalPages(response.data.totalPages);
      setTotalElements(response.data.totalElements);
    } catch (error) {
      console.error('Error fetching books:', error);
      toast.error('Failed to load books');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBooks();
  }, [page, size, sort]);

  // Handle delete
  const handleDeleteClick = (book) => {
    setBookToDelete(book);
    setDeleteModalOpen(true);
  };

  const confirmDelete = async () => {
    try {
      await booksService.deleteBook(bookToDelete.id);
      toast.success('Book deleted successfully');
      setDeleteModalOpen(false);
      setBookToDelete(null);
      fetchBooks();
    } catch (error) {
      console.error('Error deleting book:', error);
      toast.error('Failed to delete book');
    }
  };

  // Handle duplicate
  const handleDuplicate = async (id) => {
    try {
      const response = await booksService.duplicateBook(id);
      toast.success('Book duplicated successfully');
      fetchBooks();
    } catch (error) {
      console.error('Error duplicating book:', error);
      toast.error('Failed to duplicate book');
    }
  };

  if (loading) {
    return <Loading message="Loading books..." />;
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Books</h1>
          <p className="text-gray-600 mt-1">
            {totalElements} book{totalElements !== 1 ? 's' : ''} in collection
          </p>
        </div>
        <Link to="/books/add">
          <Button>Add New Book</Button>
        </Link>
      </div>

      {/* Sort options */}
      <div className="flex items-center gap-3">
        <label className="text-sm font-medium text-gray-700">Sort by:</label>
        <select
          value={sort}
          onChange={(e) => {
            setSort(e.target.value);
            setPage(0);
          }}
          className="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          {SORT_OPTIONS.map((option) => (
            <option key={option.value} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>
      </div>

      {/* Books grid */}
      {books.length === 0 ? (
        <Card>
          <div className="text-center py-12">
            <p className="text-gray-600 text-lg mb-4">No books found</p>
            <Link to="/books/add">
              <Button>Add Your First Book</Button>
            </Link>
          </div>
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {books.map((book) => (
            <Card key={book.id} className="flex flex-col">
              {/* Book Cover */}
              <div className="mb-4 aspect-[3/4] bg-gray-200 rounded overflow-hidden">
                {book.coverImageUrl ? (
                  <img
                    src={book.coverImageUrl}
                    alt={book.title}
                    className="w-full h-full object-cover"
                    onError={(e) => {
                      e.target.src = 'https://via.placeholder.com/300x400?text=No+Cover';
                    }}
                  />
                ) : (
                  <div className="w-full h-full flex items-center justify-center text-gray-400">
                    No Cover
                  </div>
                )}
              </div>

              {/* Book Info */}
              <div className="flex-grow">
                <h3 className="font-semibold text-lg text-gray-900 mb-1 line-clamp-2">
                  {book.title}
                </h3>
                <p className="text-sm text-gray-600 mb-1">{book.author}</p>
                <p className="text-sm text-gray-500 mb-2">{book.genre} • {book.publicationYear}</p>

                {book.price && (
                  <p className="text-lg font-bold text-blue-600 mb-2">
                    {formatCurrency(book.price)}
                  </p>
                )}

                {book.rating && (
                  <div className="flex items-center gap-1 text-sm mb-2">
                    <span className="text-yellow-500">★</span>
                    <span className="font-medium">{book.rating}</span>
                    <span className="text-gray-500">({book.reviewCount} reviews)</span>
                  </div>
                )}

                <div className="flex items-center gap-2 text-xs text-gray-500 mb-3">
                  <span className={`px-2 py-1 rounded ${
                    book.isAvailable ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                  }`}>
                    {book.isAvailable ? 'Available' : 'Unavailable'}
                  </span>
                  <span>Stock: {book.stockQuantity}</span>
                </div>
              </div>

              {/* Actions */}
              <div className="grid grid-cols-2 gap-2 pt-3 border-t border-gray-200">
                <Link to={`/books/${book.id}`} className="col-span-2">
                  <Button variant="primary" className="w-full text-sm">
                    View Details
                  </Button>
                </Link>
                <Link to={`/books/${book.id}/edit`}>
                  <Button variant="secondary" className="w-full text-sm">
                    Edit
                  </Button>
                </Link>
                <Button
                  variant="secondary"
                  className="text-sm"
                  onClick={() => handleDuplicate(book.id)}
                >
                  Duplicate
                </Button>
                <Button
                  variant="danger"
                  className="text-sm col-span-2"
                  onClick={() => handleDeleteClick(book)}
                >
                  Delete
                </Button>
              </div>
            </Card>
          ))}
        </div>
      )}

      {/* Pagination */}
      <Pagination
        currentPage={page}
        totalPages={totalPages}
        onPageChange={setPage}
        pageSize={size}
        onPageSizeChange={(newSize) => {
          setSize(newSize);
          setPage(0);
        }}
        totalElements={totalElements}
      />

      {/* Delete Confirmation Modal */}
      <Modal
        isOpen={deleteModalOpen}
        onClose={() => setDeleteModalOpen(false)}
        onConfirm={confirmDelete}
        title="Delete Book"
        confirmText="Delete"
        confirmVariant="danger"
      >
        <p className="text-gray-600">
          Are you sure you want to delete <strong>{bookToDelete?.title}</strong>?
          This action cannot be undone.
        </p>
      </Modal>
    </div>
  );
};

export default BooksListPage;
