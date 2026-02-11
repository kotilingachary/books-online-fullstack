import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import booksService from '../services/booksService';
import BookForm from '../components/features/books/BookForm';
import Card from '../components/common/Card';

/**
 * Add book page - create new book
 */
const AddBookPage = () => {
  const navigate = useNavigate();

  const handleSubmit = async (data) => {
    try {
      await booksService.createBook(data);
      toast.success('Book created successfully');
      navigate('/books');
    } catch (error) {
      console.error('Error creating book:', error);
      if (error.response?.status === 409) {
        toast.error('A book with this ISBN already exists');
      } else if (error.response?.data?.message) {
        toast.error(error.response.data.message);
      } else {
        toast.error('Failed to create book');
      }
    }
  };

  const handleCancel = () => {
    navigate('/books');
  };

  return (
    <div className="max-w-4xl mx-auto">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Add New Book</h1>
        <p className="text-gray-600 mt-1">
          Fill in the details to add a new book to your collection
        </p>
      </div>

      <Card>
        <BookForm onSubmit={handleSubmit} onCancel={handleCancel} />
      </Card>
    </div>
  );
};

export default AddBookPage;
