import { useState } from 'react';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import booksService from '../services/booksService';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Input from '../components/common/Input';
import Select from '../components/common/Select';
import Loading from '../components/common/Loading';
import { GENRES, LANGUAGES } from '../utils/constants';
import { formatCurrency } from '../utils/formatters';

/**
 * Search page - advanced book search with filters
 */
const SearchPage = () => {
  const [searchParams, setSearchParams] = useState({
    q: '',
    genre: '',
    language: '',
    minYear: '',
    maxYear: '',
    minPrice: '',
    maxPrice: ''
  });
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searched, setSearched] = useState(false);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setSearchParams(prev => ({ ...prev, [name]: value }));
  };

  const handleSearch = async () => {
    try {
      setLoading(true);
      setSearched(true);
      const response = await booksService.searchBooks({
        ...searchParams,
        size: 20
      });
      setResults(response.data.content);
      toast.success(`Found ${response.data.totalElements} books`);
    } catch (error) {
      console.error('Error searching books:', error);
      toast.error('Search failed');
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setSearchParams({
      q: '',
      genre: '',
      language: '',
      minYear: '',
      maxYear: '',
      minPrice: '',
      maxPrice: ''
    });
    setResults([]);
    setSearched(false);
  };

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Search Books</h1>
        <p className="text-gray-600 mt-1">
          Use advanced filters to find specific books
        </p>
      </div>

      {/* Search Form */}
      <Card title="Search Filters">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {/* Quick Search */}
          <div className="md:col-span-2">
            <Input
              label="Quick Search"
              name="q"
              value={searchParams.q}
              onChange={handleInputChange}
              placeholder="Search by title, author, or ISBN"
            />
          </div>

          {/* Genre */}
          <Select
            label="Genre"
            name="genre"
            value={searchParams.genre}
            onChange={handleInputChange}
            options={GENRES}
            placeholder="All genres"
          />

          {/* Language */}
          <Select
            label="Language"
            name="language"
            value={searchParams.language}
            onChange={handleInputChange}
            options={LANGUAGES}
            placeholder="All languages"
          />

          {/* Year Range */}
          <Input
            label="Min Year"
            type="number"
            name="minYear"
            value={searchParams.minYear}
            onChange={handleInputChange}
            placeholder="e.g., 2000"
          />

          <Input
            label="Max Year"
            type="number"
            name="maxYear"
            value={searchParams.maxYear}
            onChange={handleInputChange}
            placeholder="e.g., 2024"
          />

          {/* Price Range */}
          <Input
            label="Min Price"
            type="number"
            step="0.01"
            name="minPrice"
            value={searchParams.minPrice}
            onChange={handleInputChange}
            placeholder="0.00"
          />

          <Input
            label="Max Price"
            type="number"
            step="0.01"
            name="maxPrice"
            value={searchParams.maxPrice}
            onChange={handleInputChange}
            placeholder="999.99"
          />
        </div>

        <div className="flex gap-3 mt-6">
          <Button onClick={handleSearch} disabled={loading}>
            {loading ? 'Searching...' : 'Search'}
          </Button>
          <Button variant="secondary" onClick={handleReset}>
            Reset
          </Button>
        </div>
      </Card>

      {/* Results */}
      {loading && <Loading message="Searching books..." />}

      {searched && !loading && (
        <div>
          <h2 className="text-2xl font-bold text-gray-900 mb-4">
            Search Results ({results.length})
          </h2>

          {results.length === 0 ? (
            <Card>
              <div className="text-center py-8">
                <p className="text-gray-600">
                  No books found matching your criteria. Try adjusting your filters.
                </p>
              </div>
            </Card>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {results.map((book) => (
                <Card key={book.id}>
                  <h3 className="font-semibold text-lg mb-1">{book.title}</h3>
                  <p className="text-sm text-gray-600 mb-2">{book.author}</p>
                  <div className="text-sm text-gray-500 mb-3">
                    <p>{book.genre} • {book.publicationYear}</p>
                    {book.price && (
                      <p className="font-semibold text-blue-600 mt-1">
                        {formatCurrency(book.price)}
                      </p>
                    )}
                  </div>
                  <Link to={`/books/${book.id}`}>
                    <Button variant="secondary" className="w-full text-sm">
                      View Details
                    </Button>
                  </Link>
                </Card>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default SearchPage;
