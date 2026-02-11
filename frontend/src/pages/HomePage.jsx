import { Link } from 'react-router-dom';
import Button from '../components/common/Button';
import Card from '../components/common/Card';

/**
 * Home page - Landing page with overview and quick actions
 */
const HomePage = () => {
  return (
    <div className="space-y-8">
      {/* Hero Section */}
      <div className="text-center py-12">
        <h1 className="text-4xl font-bold text-gray-900 mb-4">
          Welcome to Books Online
        </h1>
        <p className="text-xl text-gray-600 mb-8">
          Your comprehensive book management system
        </p>
        <div className="flex justify-center gap-4">
          <Link to="/books">
            <Button>Browse Books</Button>
          </Link>
          <Link to="/books/add">
            <Button variant="secondary">Add New Book</Button>
          </Link>
        </div>
      </div>

      {/* Features Grid */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card title="Browse Books">
          <p className="text-gray-600 mb-4">
            Explore our collection of books with advanced search and filtering
            capabilities.
          </p>
          <Link to="/books">
            <Button variant="secondary" className="w-full">
              View All Books
            </Button>
          </Link>
        </Card>

        <Card title="Add Books">
          <p className="text-gray-600 mb-4">
            Add new books to your collection with detailed information and
            metadata.
          </p>
          <Link to="/books/add">
            <Button variant="secondary" className="w-full">
              Add New Book
            </Button>
          </Link>
        </Card>

        <Card title="Advanced Search">
          <p className="text-gray-600 mb-4">
            Search books by title, author, genre, year, price, and more.
          </p>
          <Link to="/search">
            <Button variant="secondary" className="w-full">
              Search Books
            </Button>
          </Link>
        </Card>
      </div>

      {/* Stats Section */}
      <div className="bg-blue-50 rounded-lg p-8">
        <h2 className="text-2xl font-bold text-gray-900 mb-6 text-center">
          System Features
        </h2>
        <div className="grid grid-cols-2 md:grid-cols-4 gap-6 text-center">
          <div>
            <div className="text-3xl font-bold text-blue-600 mb-2">CRUD</div>
            <div className="text-sm text-gray-600">Full Operations</div>
          </div>
          <div>
            <div className="text-3xl font-bold text-blue-600 mb-2">17+</div>
            <div className="text-sm text-gray-600">Search Filters</div>
          </div>
          <div>
            <div className="text-3xl font-bold text-blue-600 mb-2">8</div>
            <div className="text-sm text-gray-600">API Endpoints</div>
          </div>
          <div>
            <div className="text-3xl font-bold text-blue-600 mb-2">REST</div>
            <div className="text-sm text-gray-600">API Architecture</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomePage;
