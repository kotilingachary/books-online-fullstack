import Button from './Button';

/**
 * Pagination component for navigating through pages
 */
const Pagination = ({
  currentPage,
  totalPages,
  onPageChange,
  pageSize,
  onPageSizeChange,
  totalElements,
  pageSizeOptions = [4, 10, 20, 50]
}) => {
  const handlePrevious = () => {
    if (currentPage > 0) {
      onPageChange(currentPage - 1);
    }
  };

  const handleNext = () => {
    if (currentPage < totalPages - 1) {
      onPageChange(currentPage + 1);
    }
  };

  const handlePageClick = (page) => {
    onPageChange(page);
  };

  // Generate page numbers to display
  const getPageNumbers = () => {
    const pages = [];
    const maxPagesToShow = 5;

    if (totalPages <= maxPagesToShow) {
      for (let i = 0; i < totalPages; i++) {
        pages.push(i);
      }
    } else {
      // Always show first page
      pages.push(0);

      let startPage = Math.max(1, currentPage - 1);
      let endPage = Math.min(totalPages - 2, currentPage + 1);

      if (currentPage <= 2) {
        endPage = 3;
      }
      if (currentPage >= totalPages - 3) {
        startPage = totalPages - 4;
      }

      if (startPage > 1) {
        pages.push('...');
      }

      for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
      }

      if (endPage < totalPages - 2) {
        pages.push('...');
      }

      // Always show last page
      pages.push(totalPages - 1);
    }

    return pages;
  };

  if (totalPages <= 1) return null;

  return (
    <div className="flex flex-col sm:flex-row justify-between items-center gap-4 mt-6">
      {/* Page size selector */}
      <div className="flex items-center gap-2">
        <span className="text-sm text-gray-600">Show:</span>
        <select
          value={pageSize}
          onChange={(e) => onPageSizeChange(Number(e.target.value))}
          className="px-3 py-1 border border-gray-300 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          {pageSizeOptions.map((size) => (
            <option key={size} value={size}>
              {size}
            </option>
          ))}
        </select>
        <span className="text-sm text-gray-600">
          of {totalElements} results
        </span>
      </div>

      {/* Page navigation */}
      <div className="flex items-center gap-2">
        <Button
          variant="secondary"
          onClick={handlePrevious}
          disabled={currentPage === 0}
          className="text-sm"
        >
          Previous
        </Button>

        <div className="flex gap-1">
          {getPageNumbers().map((page, index) => (
            <button
              key={index}
              onClick={() => typeof page === 'number' && handlePageClick(page)}
              disabled={page === '...'}
              className={`px-3 py-1 rounded-md text-sm transition-colors ${
                page === currentPage
                  ? 'bg-blue-600 text-white'
                  : page === '...'
                  ? 'cursor-default'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
            >
              {typeof page === 'number' ? page + 1 : page}
            </button>
          ))}
        </div>

        <Button
          variant="secondary"
          onClick={handleNext}
          disabled={currentPage === totalPages - 1}
          className="text-sm"
        >
          Next
        </Button>
      </div>
    </div>
  );
};

export default Pagination;
