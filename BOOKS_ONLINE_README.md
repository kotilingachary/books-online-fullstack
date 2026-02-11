# Books Online - Book Management System

A full-stack book management application built with React and Spring Boot.

## 🚀 Quick Start

### Prerequisites
- **Java 17** (Required - you currently have Java 25)
- **Node.js 18+**
- **Maven 3.9+**

### Install Java 17 (Required!)

```bash
# Mac - Install Java 17
brew install openjdk@17

# Set JAVA_HOME for current session
export JAVA_HOME=$(/usr/libexec/java_home -v 17)

# Verify
java -version  # Should show version 17
```

### Start the Application

#### Option 1: Using Start Script (Easiest)

**Mac/Linux:**
```bash
./start.sh
```

**Windows:**
```bash
start.bat
```

The script will automatically:
1. Check Java 17
2. Build and start backend (port 8080)
3. Install deps and start frontend (port 5173)
4. Display access URLs

#### Option 2: Manual Start

**Terminal 1 - Backend:**
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd frontend
npm install
npm run dev
```

## 📱 Access the Application

Once running:
- **Frontend**: http://localhost:5173
- **Backend API**: http://localhost:8080/api/v1
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console
  - URL: `jdbc:h2:file:./data/booksdb`
  - User: `sa`
  - Password: (blank)

## ✨ Features

- ✅ **Full CRUD** - Create, read, update, delete books
- ✅ **Advanced Search** - 17+ filter parameters
- ✅ **Pagination** - Configurable page sizes
- ✅ **Sorting** - Sort by any field
- ✅ **Duplicate** - Clone existing books
- ✅ **Export** - JSON/CSV/PDF formats
- ✅ **Validation** - Client and server-side
- ✅ **Responsive** - Mobile-friendly design
- ✅ **Sample Data** - 20 pre-loaded books

## 🎯 What to Test

1. **Browse Books**
   - Go to /books
   - View grid of 20 sample books
   - Change page size and sorting

2. **Add New Book**
   - Click "Add New Book"
   - Fill form (ISBN must be unique!)
   - Submit and see in list

3. **Search Books**
   - Go to /search
   - Try filters: genre, year range, price
   - See filtered results

4. **Edit Book**
   - Click Edit on any book
   - Modify fields
   - Save changes

5. **View Details**
   - Click on any book card
   - See full information
   - Notice view count increments

6. **Delete Book**
   - Click Delete
   - Confirm in modal
   - Book removed from list

7. **Duplicate Book**
   - Click Duplicate
   - Book copied with "(Copy)" suffix
   - ISBN cleared (must enter new one)

## 📚 Sample Data

The system comes with 20 books across 7 genres:
- **Programming** (6): Clean Code, Design Patterns, etc.
- **Science Fiction** (3): Dune, The Martian, Neuromancer
- **Fiction** (2): To Kill a Mockingbird, 1984
- **Business** (2): Good to Great, The Lean Startup
- **Self-Help** (2): Atomic Habits, 7 Habits
- **History** (2): Sapiens, Guns of August
- **Biography** (1): Steve Jobs

## 🔧 Tech Stack

### Frontend
- React 18.2 + Vite 5
- React Router 6 (routing)
- Tailwind CSS 3 (styling)
- React Hook Form + Zod (forms/validation)
- Axios (HTTP client)
- React Toastify (notifications)

### Backend
- Spring Boot 3.2.2
- Java 17
- Spring Data JPA
- H2 Database
- Spring Security (open access in V1)
- Springdoc OpenAPI (Swagger)
- Maven

## 📂 Project Structure

```
books-online/
├── backend/           # Spring Boot API
│   ├── src/main/java/com/booksonline/
│   │   ├── controller/     # REST endpoints
│   │   ├── service/        # Business logic
│   │   ├── repository/     # Data access
│   │   ├── model/          # Entities & DTOs
│   │   ├── exception/      # Error handling
│   │   ├── mapper/         # Conversions
│   │   └── config/         # Configuration
│   └── src/main/resources/
│       ├── application.properties
│       ├── schema.sql      # DB schema
│       └── data.sql        # Sample data
│
├── frontend/          # React SPA
│   ├── src/
│   │   ├── components/     # UI components
│   │   ├── pages/          # Route pages
│   │   ├── services/       # API client
│   │   └── utils/          # Helpers
│   └── package.json
│
├── artifacts/         # Design docs
│   ├── 01-wireframe-analysis/
│   ├── 02-prd/
│   ├── 03-user-stories/
│   ├── 04-architecture/
│   ├── 05-api-spec/
│   └── 06-database-design/
│
├── start.sh           # Start script (Mac/Linux)
└── start.bat          # Start script (Windows)
```

## 🔌 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/books | List all books |
| GET | /api/v1/books/{id} | Get book by ID |
| POST | /api/v1/books | Create book |
| PUT | /api/v1/books/{id} | Update book |
| DELETE | /api/v1/books/{id} | Delete book |
| GET | /api/v1/books/search | Advanced search |
| POST | /api/v1/books/{id}/duplicate | Duplicate book |
| GET | /api/v1/books/{id}/export | Export book |

Full API docs: http://localhost:8080/swagger-ui.html

## 🗃️ Database Schema

**Books Table (18 columns):**
- **PK**: id (auto-increment)
- **Required**: title, isbn (unique), author, genre, publicationYear, language, isAvailable
- **Optional**: publisher, pages, description, coverImageUrl, price, stockQuantity, rating, reviewCount
- **System**: viewCount, createdAt, updatedAt

**Indexes:** 8 performance indexes on common query fields

## ⚠️ Common Issues

### Java Version Error
```bash
# You have Java 25, need Java 17!
brew install openjdk@17
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
java -version  # Verify
```

### Port Already in Use
```bash
# Backend (8080)
lsof -ti:8080 | xargs kill -9

# Frontend (5173)
lsof -ti:5173 | xargs kill -9
```

### Dependencies Not Installed
```bash
cd frontend
npm install
```

### Build Fails
```bash
cd backend
mvn clean install -U
```

## 📖 Documentation

- **API Spec**: `artifacts/05-api-spec/api-documentation.md`
- **OpenAPI**: `artifacts/05-api-spec/openapi-spec.yaml`
- **Database**: `artifacts/06-database-design/er-diagram.md`
- **Architecture**: `artifacts/04-architecture/architecture.md`
- **Backend README**: `backend/README.md`

## 🎓 Learning Resources

### Swagger UI
- Interactive API documentation
- Test all endpoints
- See request/response schemas
- http://localhost:8080/swagger-ui.html

### H2 Console
- View database tables
- Run SQL queries
- See sample data
- http://localhost:8080/h2-console

## 🚦 Testing Checklist

- [ ] Backend starts on port 8080
- [ ] Frontend starts on port 5173
- [ ] Can view list of 20 books
- [ ] Can add new book
- [ ] Can edit existing book
- [ ] Can delete book
- [ ] Can search by genre
- [ ] Can filter by year range
- [ ] Pagination works
- [ ] Sorting works
- [ ] Form validation works
- [ ] Duplicate book works
- [ ] View count increments
- [ ] Swagger UI accessible

## 🎯 Next Steps (V2)

Future enhancements:
- JWT authentication
- User roles (Admin, Librarian, Viewer)
- CSV/PDF export implementation
- Bulk import
- User reviews and ratings
- Analytics dashboard
- PostgreSQL migration
- Redis caching

## 📝 License

MIT

---

**Built with Spring Boot + React + Vite + Tailwind CSS**

*Generated by Multi-Agent System - From Figma to Production*
