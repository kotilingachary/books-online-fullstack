/**
 * Footer component
 */
const Footer = () => {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="bg-gray-800 text-white mt-auto">
      <div className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {/* About section */}
          <div>
            <h3 className="text-lg font-semibold mb-4">Books Online</h3>
            <p className="text-gray-400 text-sm">
              A comprehensive book management system for cataloging, searching,
              and managing your book collection.
            </p>
          </div>

          {/* Quick links */}
          <div>
            <h3 className="text-lg font-semibold mb-4">Quick Links</h3>
            <ul className="space-y-2 text-sm text-gray-400">
              <li>
                <a href="/" className="hover:text-white transition-colors">
                  Home
                </a>
              </li>
              <li>
                <a href="/books" className="hover:text-white transition-colors">
                  Browse Books
                </a>
              </li>
              <li>
                <a href="/search" className="hover:text-white transition-colors">
                  Search
                </a>
              </li>
              <li>
                <a href="/books/add" className="hover:text-white transition-colors">
                  Add Book
                </a>
              </li>
            </ul>
          </div>

          {/* API Documentation */}
          <div>
            <h3 className="text-lg font-semibold mb-4">API Documentation</h3>
            <ul className="space-y-2 text-sm text-gray-400">
              <li>
                <a
                  href="http://localhost:8080/swagger-ui.html"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="hover:text-white transition-colors"
                >
                  Swagger UI
                </a>
              </li>
              <li>
                <a
                  href="http://localhost:8080/v3/api-docs"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="hover:text-white transition-colors"
                >
                  OpenAPI Spec
                </a>
              </li>
            </ul>
          </div>
        </div>

        <div className="border-t border-gray-700 mt-8 pt-8 text-center text-sm text-gray-400">
          <p>&copy; {currentYear} Books Online. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
