import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import Layout from './components/layout/Layout';
import HomePage from './pages/HomePage';
import BooksListPage from './pages/BooksListPage';
import AddBookPage from './pages/AddBookPage';
import EditBookPage from './pages/EditBookPage';
import BookDetailsPage from './pages/BookDetailsPage';
import SearchPage from './pages/SearchPage';
import NotFoundPage from './pages/NotFoundPage';

/**
 * Main App component with routing
 */
function App() {
  return (
    <BrowserRouter>
      <Layout>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/books" element={<BooksListPage />} />
          <Route path="/books/add" element={<AddBookPage />} />
          <Route path="/books/:id" element={<BookDetailsPage />} />
          <Route path="/books/:id/edit" element={<EditBookPage />} />
          <Route path="/search" element={<SearchPage />} />
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </Layout>
      <ToastContainer
        position="top-right"
        autoClose={3000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
      />
    </BrowserRouter>
  );
}

export default App;
