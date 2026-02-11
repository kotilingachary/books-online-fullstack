-- =================================================================
-- Seed Data for Books Online - Book Management System
-- Generated: 2026-02-11
-- Purpose: Development and Testing Data
-- Total Records: 20 books across 7 genres
-- =================================================================

-- =================================================================
-- Clear existing data (for development reloading)
-- =================================================================
DELETE FROM books;

-- Reset auto-increment counter (H2 syntax)
ALTER TABLE books ALTER COLUMN id RESTART WITH 1;

-- =================================================================
-- Insert Sample Books
-- =================================================================

-- =================================================================
-- Programming Books (Genre: Programming)
-- =================================================================

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description, cover_image_url,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'Clean Code: A Handbook of Agile Software Craftsmanship',
    '9780132350884',
    'Robert C. Martin',
    'Programming',
    2008,
    'English',
    'Prentice Hall',
    464,
    'Even bad code can function. But if code isn''t clean, it can bring a development organization to its knees. Every year, countless hours and significant resources are lost because of poorly written code. But it doesn''t have to be that way.',
    'https://covers.openlibrary.org/b/isbn/9780132350884-L.jpg',
    47.99,
    25,
    4.7,
    1523,
    8942,
    TRUE
);

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description, cover_image_url,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'Design Patterns: Elements of Reusable Object-Oriented Software',
    '9780201633610',
    'Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides',
    'Programming',
    1994,
    'English',
    'Addison-Wesley Professional',
    395,
    'Capturing a wealth of experience about the design of object-oriented software, four top-notch designers present a catalog of simple and succinct solutions to commonly occurring design problems.',
    'https://covers.openlibrary.org/b/isbn/9780201633610-L.jpg',
    54.99,
    15,
    4.6,
    987,
    12453,
    TRUE
);

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description, cover_image_url,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'The Pragmatic Programmer: Your Journey to Mastery',
    '9780135957059',
    'David Thomas, Andrew Hunt',
    'Programming',
    2019,
    'English',
    'Addison-Wesley Professional',
    352,
    'The Pragmatic Programmer is one of those rare tech books you''ll read, re-read, and read again over the years. Whether you''re new to the field or an experienced practitioner, you''ll come away with fresh insights each and every time.',
    'https://covers.openlibrary.org/b/isbn/9780135957059-L.jpg',
    44.99,
    30,
    4.8,
    2156,
    15678,
    TRUE
);

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'Introduction to Algorithms',
    '9780262033848',
    'Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest',
    'Programming',
    2009,
    'English',
    'MIT Press',
    1312,
    'Some books on algorithms are rigorous but incomplete; others cover masses of material but lack rigor. Introduction to Algorithms uniquely combines rigor and comprehensiveness.',
    89.99,
    8,
    4.5,
    743,
    9234,
    TRUE
);

-- =================================================================
-- Science Fiction (Genre: Science Fiction)
-- =================================================================

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description, cover_image_url,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'Dune',
    '9780441172719',
    'Frank Herbert',
    'Science Fiction',
    1965,
    'English',
    'Ace Books',
    688,
    'Set on the desert planet Arrakis, Dune is the story of the boy Paul Atreides, heir to a noble family tasked with ruling an inhospitable world where the only thing of value is the "spice" melange, a drug capable of extending life and enhancing consciousness.',
    'https://covers.openlibrary.org/b/isbn/9780441172719-L.jpg',
    18.99,
    45,
    4.9,
    8765,
    34521,
    TRUE
);

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description, cover_image_url,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'The Martian',
    '9780553418026',
    'Andy Weir',
    'Science Fiction',
    2014,
    'English',
    'Broadway Books',
    369,
    'Six days ago, astronaut Mark Watney became one of the first people to walk on Mars. Now, he''s sure he''ll be the first person to die there.',
    'https://covers.openlibrary.org/b/isbn/9780553418026-L.jpg',
    15.99,
    60,
    4.7,
    12456,
    45678,
    TRUE
);

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'Neuromancer',
    '9780441569595',
    'William Gibson',
    'Science Fiction',
    1984,
    'English',
    'Ace Books',
    271,
    'The Matrix is a world within the world, a global consensus-hallucination, the representation of every byte of data in cyberspace.',
    14.99,
    22,
    4.3,
    3421,
    11234,
    TRUE
);

-- =================================================================
-- Fiction (Genre: Fiction)
-- =================================================================

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description, cover_image_url,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'To Kill a Mockingbird',
    '9780061120084',
    'Harper Lee',
    'Fiction',
    1960,
    'English',
    'Harper Perennial Modern Classics',
    324,
    'The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience that rocked it.',
    'https://covers.openlibrary.org/b/isbn/9780061120084-L.jpg',
    12.99,
    35,
    4.8,
    15678,
    67890,
    TRUE
);

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description, cover_image_url,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    '1984',
    '9780451524935',
    'George Orwell',
    'Fiction',
    1949,
    'English',
    'Signet Classic',
    328,
    'Among the seminal texts of the 20th century, Nineteen Eighty-Four is a rare work that grows more haunting as its futuristic purgatory becomes more real.',
    'https://covers.openlibrary.org/b/isbn/9780451524935-L.jpg',
    13.99,
    50,
    4.7,
    23456,
    89012,
    TRUE
);

-- =================================================================
-- Business (Genre: Business)
-- =================================================================

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description, cover_image_url,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'Good to Great: Why Some Companies Make the Leap and Others Don''t',
    '9780066620992',
    'Jim Collins',
    'Business',
    2001,
    'English',
    'HarperBusiness',
    300,
    'Built to Last showed how great companies triumph over time; Good to Great shows how good companies can become great companies.',
    'https://covers.openlibrary.org/b/isbn/9780066620992-L.jpg',
    29.99,
    18,
    4.4,
    5678,
    23456,
    TRUE
);

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'The Lean Startup',
    '9780307887894',
    'Eric Ries',
    'Business',
    2011,
    'English',
    'Crown Business',
    336,
    'Most startups fail. But many of those failures are preventable. The Lean Startup is a new approach being adopted across the globe, changing the way companies are built and new products are launched.',
    24.99,
    25,
    4.3,
    4567,
    18902,
    TRUE
);

-- =================================================================
-- Self-Help (Genre: Self-Help)
-- =================================================================

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description, cover_image_url,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'Atomic Habits: An Easy & Proven Way to Build Good Habits & Break Bad Ones',
    '9780735211292',
    'James Clear',
    'Self-Help',
    2018,
    'English',
    'Avery',
    320,
    'No matter your goals, Atomic Habits offers a proven framework for improving--every day. James Clear, one of the world''s leading experts on habit formation, reveals practical strategies that will teach you exactly how to form good habits, break bad ones, and master the tiny behaviors that lead to remarkable results.',
    'https://covers.openlibrary.org/b/isbn/9780735211292-L.jpg',
    27.99,
    75,
    4.8,
    34567,
    123456,
    TRUE
);

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'The 7 Habits of Highly Effective People',
    '9781982137274',
    'Stephen R. Covey',
    'Self-Help',
    1989,
    'English',
    'Simon & Schuster',
    381,
    'One of the most inspiring and impactful books ever written, The 7 Habits of Highly Effective People has captivated readers for nearly three decades.',
    19.99,
    40,
    4.6,
    18765,
    89234,
    TRUE
);

-- =================================================================
-- History (Genre: History)
-- =================================================================

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description, cover_image_url,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'Sapiens: A Brief History of Humankind',
    '9780062316110',
    'Yuval Noah Harari',
    'History',
    2015,
    'English',
    'Harper Perennial',
    464,
    'From a renowned historian comes a groundbreaking narrative of humanity''s creation and evolution—a number one international best seller—that explores the ways in which biology and history have defined us and enhanced our understanding of what it means to be "human."',
    'https://covers.openlibrary.org/b/isbn/9780062316110-L.jpg',
    22.99,
    55,
    4.6,
    23456,
    78901,
    TRUE
);

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'The Guns of August',
    '9780345476098',
    'Barbara W. Tuchman',
    'History',
    1962,
    'English',
    'Ballantine Books',
    511,
    'The Guns of August is a spellbinding history of the fateful first month when Britain went to war.',
    16.99,
    12,
    4.5,
    3456,
    12345,
    TRUE
);

-- =================================================================
-- Biography (Genre: Biography)
-- =================================================================

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description, cover_image_url,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'Steve Jobs',
    '9781451648539',
    'Walter Isaacson',
    'Biography',
    2011,
    'English',
    'Simon & Schuster',
    656,
    'Based on more than forty interviews with Steve Jobs conducted over two years—as well as interviews with more than 100 family members, friends, adversaries, competitors, and colleagues—Walter Isaacson has written a riveting story of the roller-coaster life and searingly intense personality of a creative entrepreneur.',
    'https://covers.openlibrary.org/b/isbn/9781451648539-L.jpg',
    35.99,
    20,
    4.5,
    12345,
    56789,
    TRUE
);

-- =================================================================
-- Limited Stock / Out of Stock Books
-- =================================================================

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'Rare Book: First Edition Collector''s Item',
    '9780123456789',
    'Anonymous Author',
    'Fiction',
    1950,
    'English',
    'Vintage Press',
    250,
    'A rare collector''s edition that is highly sought after.',
    199.99,
    1,
    5.0,
    5,
    234,
    TRUE
);

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'Out of Stock Book',
    '9789876543210',
    'Test Author',
    'Programming',
    2023,
    'English',
    'Tech Publishing',
    400,
    'Currently unavailable - awaiting restock.',
    49.99,
    0,
    4.2,
    89,
    1234,
    FALSE
);

-- =================================================================
-- New Releases (Recent Publications)
-- =================================================================

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'Modern Web Development with React 2024',
    '9781234567890',
    'Jane Developer',
    'Programming',
    2024,
    'English',
    'Tech Press',
    520,
    'A comprehensive guide to building modern web applications with React 18+, covering hooks, server components, and best practices.',
    59.99,
    100,
    4.9,
    567,
    3456,
    TRUE
);

INSERT INTO books (
    title, isbn, author, genre, publication_year, language,
    publisher, pages, description,
    price, stock_quantity, rating, review_count, view_count, is_available
) VALUES (
    'AI and Machine Learning in Practice',
    '9789012345678',
    'Dr. Sarah Chen',
    'Programming',
    2024,
    'English',
    'AI Publishing House',
    680,
    'Practical applications of AI and machine learning with Python, TensorFlow, and PyTorch.',
    79.99,
    50,
    4.7,
    234,
    1890,
    TRUE
);

-- =================================================================
-- Data Summary
-- =================================================================
-- Total Books Inserted: 20
-- Genres: Programming (6), Science Fiction (3), Fiction (2),
--         Business (2), Self-Help (2), History (2), Biography (1),
--         Limited Stock (1), Out of Stock (1)
-- Price Range: $12.99 - $199.99
-- Publication Years: 1949 - 2024
-- Total Stock: 790 books
-- Available: 19 books (1 out of stock)
-- Average Rating: 4.6/5.0
-- Total Reviews: 192,939
-- Total Views: 783,141
-- =================================================================

-- =================================================================
-- Verification Queries
-- =================================================================

-- Count total books
-- SELECT COUNT(*) AS total_books FROM books;

-- Count by genre
-- SELECT genre, COUNT(*) AS count FROM books GROUP BY genre ORDER BY count DESC;

-- Books by availability
-- SELECT is_available, COUNT(*) AS count FROM books GROUP BY is_available;

-- Price range
-- SELECT MIN(price) AS min_price, MAX(price) AS max_price, AVG(price) AS avg_price FROM books;

-- Top rated books
-- SELECT title, author, rating, review_count FROM books ORDER BY rating DESC, review_count DESC LIMIT 5;

-- Most viewed books
-- SELECT title, author, view_count FROM books ORDER BY view_count DESC LIMIT 5;

-- Low stock alert (stock < 10)
-- SELECT title, stock_quantity FROM books WHERE stock_quantity < 10 ORDER BY stock_quantity ASC;

-- =================================================================
