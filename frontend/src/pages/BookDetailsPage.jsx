import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import booksService from '../services/booksService';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Loading from '../components/common/Loading';
import Modal from '../components/common/Modal';
import { formatCurrency, formatDate, formatNumber } from '../utils/formatters';

/**
 * Book details page - view single book information
 */
const BookDetailsPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(true);
  const [deleteModalOpen, setDeleteModalOpen] = useState(false);

  useEffect(() => {
    fetchBook();
  }, [id]);

  const fetchBook = async () => {
    try {
      const response = await booksService.getBookById(id);
      setBook(response.data);
    } catch (error) {
      console.error('Error fetching book:', error);
      toast.error('Failed to load book');
      navigate('/books');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    try {
      await booksService.deleteBook(id);
      toast.success('Book deleted successfully');
      navigate('/books');
    } catch (error) {
      console.error('Error deleting book:', error);
      toast.error('Failed to delete book');
    }
  };

  const handleAddToWishlist = async () => {
    try {
      const response = await booksService.addToWishlist(id);
      setBook(response.data);
      toast.success('Added to wishlist');
    } catch (error) {
      console.error('Error adding to wishlist:', error);
      toast.error('Failed to add to wishlist');
    }
  };

  const handleDuplicate = async () => {
    try {
      const response = await booksService.duplicateBook(id);
      toast.success('Book duplicated successfully');
      navigate(`/books/${response.data.id}/edit`);
    } catch (error) {
      console.error('Error duplicating book:', error);
      toast.error('Failed to duplicate book');
    }
  };

  if (loading) {
    return <Loading message="Loading book details..." />;
  }

  if (!book) {
    return null;
  }

  return (
    <div className="max-w-6xl mx-auto">
      {/* Header */}
      <div className="mb-6">
        <Link to="/books" className="text-blue-600 hover:text-blue-700 mb-2 inline-block">
          ← Back to Books
        </Link>
        <div className="flex justify-between items-start">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">{book.title}</h1>
            <p className="text-xl text-gray-600 mt-1">{book.author}</p>
          </div>
          <div className="flex gap-2">
            <Link to={`/books/${id}/edit`}>
              <Button variant="secondary">Edit</Button>
            </Link>
            <Button variant="secondary" onClick={handleDuplicate}>
              Duplicate
            </Button>
            <Button variant="danger" onClick={() => setDeleteModalOpen(true)}>
              Delete
            </Button>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {/* Book Cover */}
        <div className="md:col-span-1">
          <Card>
            <div className="aspect-[3/4] bg-gray-200 rounded overflow-hidden">
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
                  No Cover Available
                </div>
              )}
            </div>
          </Card>
        </div>

        {/* Book Information */}
        <div className="md:col-span-2 space-y-6">
          {/* Basic Info */}
          <Card title="Basic Information">
            <dl className="grid grid-cols-2 gap-4">
              <div>
                <dt className="text-sm font-medium text-gray-500">ISBN</dt>
                <dd className="mt-1 text-sm text-gray-900">{book.isbn}</dd>
              </div>
              <div>
                <dt className="text-sm font-medium text-gray-500">Genre</dt>
                <dd className="mt-1 text-sm text-gray-900">{book.genre}</dd>
              </div>
              <div>
                <dt className="text-sm font-medium text-gray-500">Language</dt>
                <dd className="mt-1 text-sm text-gray-900">{book.language}</dd>
              </div>
              <div>
                <dt className="text-sm font-medium text-gray-500">Publication Year</dt>
                <dd className="mt-1 text-sm text-gray-900">{book.publicationYear}</dd>
              </div>
              {book.publisher && (
                <div>
                  <dt className="text-sm font-medium text-gray-500">Publisher</dt>
                  <dd className="mt-1 text-sm text-gray-900">{book.publisher}</dd>
                </div>
              )}
              {book.pages && (
                <div>
                  <dt className="text-sm font-medium text-gray-500">Pages</dt>
                  <dd className="mt-1 text-sm text-gray-900">{book.pages}</dd>
                </div>
              )}
            </dl>
          </Card>

          {/* Pricing & Availability */}
          <Card title="Pricing & Availability">
            <dl className="grid grid-cols-2 gap-4">
              {book.price && (
                <div>
                  <dt className="text-sm font-medium text-gray-500">Price</dt>
                  <dd className="mt-1 text-lg font-semibold text-blue-600">
                    {formatCurrency(book.price)}
                  </dd>
                </div>
              )}
              <div>
                <dt className="text-sm font-medium text-gray-500">Stock Quantity</dt>
                <dd className="mt-1 text-sm text-gray-900">{book.stockQuantity}</dd>
              </div>
              <div>
                <dt className="text-sm font-medium text-gray-500">Availability</dt>
                <dd className="mt-1">
                  <span className={`px-3 py-1 rounded text-sm font-medium ${
                    book.isAvailable ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                  }`}>
                    {book.isAvailable ? 'Available' : 'Unavailable'}
                  </span>
                </dd>
              </div>
            </dl>
          </Card>

          {/* Ratings & Engagement */}
          <Card title="Ratings & Engagement">
            <dl className="grid grid-cols-4 gap-4">
              <div>
                <dt className="text-sm font-medium text-gray-500">Rating</dt>
                <dd className="mt-1 flex items-center">
                  <span className="text-yellow-500 text-xl mr-1">★</span>
                  <span className="text-lg font-semibold text-gray-900">
                    {book.rating || 'N/A'}
                  </span>
                </dd>
              </div>
              <div>
                <dt className="text-sm font-medium text-gray-500">Reviews</dt>
                <dd className="mt-1 text-sm text-gray-900">
                  {formatNumber(book.reviewCount)}
                </dd>
              </div>
              <div>
                <dt className="text-sm font-medium text-gray-500">Views</dt>
                <dd className="mt-1 text-sm text-gray-900">
                  {formatNumber(book.viewCount)}
                </dd>
              </div>
              <div>
                <dt className="text-sm font-medium text-gray-500">Wishlist</dt>
                <dd className="mt-1 flex items-center">
                  <span className="text-red-500 text-lg mr-1" aria-label="wishlist">♥</span>
                  <span className="text-sm text-gray-900">
                    {formatNumber(book.wishlistCount ?? 0)}
                  </span>
                  <button
                    type="button"
                    onClick={handleAddToWishlist}
                    className="ml-3 text-xs text-blue-600 hover:text-blue-700"
                  >
                    Add
                  </button>
                </dd>
              </div>
            </dl>
          </Card>

          {/* Description */}
          {book.description && (
            <Card title="Description">
              <p className="text-gray-700 whitespace-pre-wrap">{book.description}</p>
            </Card>
          )}

          {/* Metadata */}
          <Card title="Metadata">
            <dl className="grid grid-cols-2 gap-4 text-sm">
              <div>
                <dt className="font-medium text-gray-500">Created At</dt>
                <dd className="mt-1 text-gray-900">{formatDate(book.createdAt)}</dd>
              </div>
              <div>
                <dt className="font-medium text-gray-500">Last Updated</dt>
                <dd className="mt-1 text-gray-900">{formatDate(book.updatedAt)}</dd>
              </div>
            </dl>
          </Card>
        </div>
      </div>

      {/* Delete Confirmation Modal */}
      <Modal
        isOpen={deleteModalOpen}
        onClose={() => setDeleteModalOpen(false)}
        onConfirm={handleDelete}
        title="Delete Book"
        confirmText="Delete"
        confirmVariant="danger"
      >
        <p className="text-gray-600">
          Are you sure you want to delete <strong>{book.title}</strong>?
          This action cannot be undone.
        </p>
      </Modal>
    </div>
  );
};

export default BookDetailsPage;
