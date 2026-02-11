import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import booksService from '../services/booksService';
import BookForm from '../components/features/books/BookForm';
import Card from '../components/common/Card';
import Loading from '../components/common/Loading';

/**
 * Edit book page - update existing book
 */
const EditBookPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(true);

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

  const handleSubmit = async (data) => {
    try {
      await booksService.updateBook(id, data);
      toast.success('Book updated successfully');
      navigate(`/books/${id}`);
    } catch (error) {
      console.error('Error updating book:', error);
      if (error.response?.data?.message) {
        toast.error(error.response.data.message);
      } else {
        toast.error('Failed to update book');
      }
    }
  };

  const handleCancel = () => {
    navigate(`/books/${id}`);
  };

  if (loading) {
    return <Loading message="Loading book..." />;
  }

  return (
    <div className="max-w-4xl mx-auto">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Edit Book</h1>
        <p className="text-gray-600 mt-1">Update book information</p>
      </div>

      <Card>
        <BookForm
          initialData={book}
          onSubmit={handleSubmit}
          onCancel={handleCancel}
        />
      </Card>
    </div>
  );
};

export default EditBookPage;
