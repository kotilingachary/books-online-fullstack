import { Link } from 'react-router-dom';
import Button from '../components/common/Button';

/**
 * 404 Not Found page
 */
const NotFoundPage = () => {
  return (
    <div className="flex flex-col items-center justify-center min-h-[60vh] text-center">
      <div className="mb-8">
        <h1 className="text-9xl font-bold text-gray-300">404</h1>
        <h2 className="text-3xl font-bold text-gray-900 mt-4">Page Not Found</h2>
        <p className="text-gray-600 mt-2 max-w-md">
          The page you're looking for doesn't exist or has been moved.
        </p>
      </div>

      <div className="flex gap-4">
        <Link to="/">
          <Button>Go Home</Button>
        </Link>
        <Link to="/books">
          <Button variant="secondary">Browse Books</Button>
        </Link>
      </div>
    </div>
  );
};

export default NotFoundPage;
