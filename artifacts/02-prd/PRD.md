# Product Requirements Document

## Document Information
- **Product Name:** Books Online - Book Management System
- **Version:** 1.0
- **Date:** 2024-02-11
- **Author:** AI Generated from Wireframe Analysis
- **Status:** Draft
- **Document ID:** PRD-BOOKS-2024-001

---

## 1. Executive Summary

### 1.1 Product Vision

Books Online is a comprehensive book management system designed to streamline the cataloging, organization, and discovery of books in libraries, bookstores, and personal collections. The system provides an intuitive web-based interface that empowers administrators and staff to efficiently manage their book inventory while offering powerful search and filtering capabilities to quickly locate any book in the collection.

The vision is to eliminate the complexity of traditional book management systems by providing a modern, user-friendly solution that requires minimal training and delivers immediate value. By combining essential CRUD operations with advanced search functionality and data export capabilities, Books Online serves as the central hub for all book-related operations.

In an era where digital transformation is critical, Books Online bridges the gap between traditional library management practices and modern web applications, ensuring that book collections are accessible, well-organized, and easy to maintain regardless of collection size.

### 1.2 Product Description

Books Online is a full-stack web application built with React frontend and Java Spring Boot backend, utilizing H2 database for data persistence. The application provides five primary screens that guide users through the complete lifecycle of book management:

**Core Screens:**
1. **Books List View** - Main dashboard displaying all books in a sortable, paginated table with quick filters
2. **Add Book Form** - Comprehensive form for adding new books with cover image upload and complete metadata
3. **Search Books** - Advanced search interface with multiple criteria including title, author, ISBN, genre, language, publication year range, price range, and availability filters
4. **Edit Book Form** - Pre-populated form for updating existing book information with ISBN immutability
5. **Book Details** - Rich detail view showing all book information, ratings, availability, pricing, and action buttons for edit, duplicate, export, and delete operations

The application is designed for single-page application (SPA) architecture with client-side routing, providing seamless navigation and instant feedback for all user actions. All data operations are performed through RESTful APIs, ensuring clean separation of concerns and enabling future extensibility.

### 1.3 Target Audience

**Primary Users:**

**Persona 1: Library Administrator**
- **Role:** Senior staff member responsible for maintaining accurate book records
- **Goals:**
  - Maintain accurate, up-to-date book catalog
  - Quickly add new acquisitions to the system
  - Update book information when details change
  - Remove obsolete or damaged books from inventory
- **Pain Points:**
  - Time-consuming data entry processes
  - Difficulty finding specific books in large collections
  - Manual tracking of stock quantities and availability
  - Lack of data export capabilities for reporting
- **Technical Proficiency:** Intermediate - comfortable with web applications, forms, and basic search
- **Frequency:** Daily use, 2-4 hours per day, multiple CRUD operations

**Persona 2: Catalog Manager**
- **Role:** Staff member focused on organization and data quality
- **Goals:**
  - Browse and verify book records for accuracy
  - Apply filters to view specific subsets of the collection
  - Ensure consistent categorization (genre, language)
  - Monitor stock levels and availability
- **Pain Points:**
  - Inability to view collection by specific criteria
  - Difficulty identifying duplicate or inconsistent records
  - No overview of collection statistics
- **Technical Proficiency:** Intermediate - familiar with filtering, sorting, and data management concepts
- **Frequency:** Daily use, 3-5 hours per day, primarily browsing and filtering

**Secondary Users:**

**Persona 3: Data Entry Clerk**
- **Role:** Staff member dedicated to adding new books in bulk
- **Goals:**
  - Rapidly add multiple new books to the system
  - Use duplicate feature to speed up entry of similar books
  - Minimize errors through form validation
- **Pain Points:**
  - Repetitive data entry for similar books
  - Manual validation of ISBN formats
  - No bulk import capabilities
- **Technical Proficiency:** Beginner to Intermediate - focused on form completion
- **Frequency:** Weekly or periodic, 2-3 hours per session during acquisition periods

**Persona 4: Collection Analyst**
- **Role:** Staff or management reviewing collection composition
- **Goals:**
  - Export book data for analysis and reporting
  - Search by multiple criteria to identify gaps in collection
  - Review metadata for collection development decisions
- **Pain Points:**
  - No reporting capabilities
  - Data locked in system without export
- **Technical Proficiency:** Intermediate to Advanced
- **Frequency:** Weekly or monthly, ad-hoc basis

### 1.4 Success Metrics

**User Adoption:**
- 100% of library staff trained and using system within 2 weeks of deployment
- 90% of book additions completed through the system within 1 month
- User satisfaction score > 4.0/5.0 based on usability surveys

**Engagement:**
- Average session duration: 15-30 minutes per user per day
- Daily active users: 80% of total user base
- Feature utilization: All primary features (add, search, edit) used by >75% of users monthly

**Performance:**
- Page load time < 2 seconds for all screens
- API response time < 500ms for 95% of requests
- Search results returned < 1 second for collections up to 10,000 books
- Zero data loss incidents

**Business:**
- Cataloging time reduced by 40% compared to previous system
- Search/retrieval time reduced by 60%
- Data accuracy >99% (verified through periodic audits)
- Administrative overhead reduced by 30%
- System uptime >99.5%

---

## 2. Product Scope

### 2.1 In Scope

**Core Functionality:**
1. Complete CRUD operations for books (Create, Read, Update, Delete)
2. Real-time quick search from header
3. Advanced multi-criteria search with 12+ search parameters
4. Filtering by genre, author, publication year
5. Pagination for browsing large collections (configurable page size)
6. Book cover image upload and display
7. ISBN validation and uniqueness enforcement
8. Form validation for all required and optional fields
9. Delete confirmation with modal dialog
10. Book duplication for rapid data entry
11. Data export in JSON, CSV, and PDF formats
12. View count tracking for book details
13. Availability status computation based on stock quantity
14. Responsive design for desktop, tablet, and mobile devices
15. Comprehensive error handling and user feedback

**Data Management:**
1. Full Book entity with 18 fields (7 required, 11 optional, 4 system-managed)
2. Automatic timestamp management (createdAt, updatedAt)
3. ISBN immutability after creation
4. Stock quantity and availability tracking
5. Book ratings and review count display (static in V1)

**User Experience:**
1. Consistent navigation with back buttons and breadcrumbs
2. Loading states and skeleton loaders
3. Success/error toast notifications
4. Empty state messages with helpful guidance
5. Informational banners and help text
6. Keyboard navigation support
7. Mobile-responsive layouts

### 2.2 Out of Scope (V1)

**Authentication & Authorization:**
- User registration and login system
- Role-based access control (admin, viewer, editor roles)
- Multi-user support with user accounts
- Session management and JWT authentication
- Password reset and account recovery
- Audit logs showing who made changes

**Advanced Features:**
- User-submitted book reviews and ratings (only display static rating in V1)
- Book borrowing/checkout system
- Due date tracking and late fees
- Patron/member management
- Book reservations and holds
- Email notifications
- Barcode scanning for ISBN entry
- Bulk import via CSV/Excel
- Advanced reporting and analytics dashboards
- Collection statistics and visualizations
- Multi-author support (V1 stores single author name)
- Publisher management as separate entity
- Genre/category hierarchy and tagging
- Book recommendations
- Related books suggestions

**Integration:**
- Integration with external book databases (Google Books, OpenLibrary)
- ISBN lookup to auto-populate metadata
- Integration with payment systems for purchases
- Integration with external inventory management systems

**Infrastructure:**
- Production database (PostgreSQL/MySQL) - V1 uses H2 only
- Cloud deployment and hosting
- Automated backups and disaster recovery
- Monitoring and alerting systems
- Performance optimization for >100,000 books

### 2.3 Future Considerations

**Phase 2 (Q2 2024):**
- User authentication system with JWT
- Role-based access control (Admin, Librarian, Viewer)
- User-submitted reviews and ratings (CRUD operations)
- Bulk import from CSV files
- Advanced analytics dashboard
- Production database migration (PostgreSQL)

**Phase 3 (Q3 2024):**
- Book borrowing and checkout system
- Patron management
- Due date tracking and notifications
- Barcode scanner integration
- Mobile app (React Native)

**Phase 4 (Q4 2024):**
- External API integrations (Google Books, OpenLibrary)
- Multi-author support with Author entity
- Publisher management module
- Recommendation engine
- API for third-party integrations

---

## 3. Functional Requirements

### 3.1 Core Features

#### Feature 1: Book List Management (FR-001 to FR-010)

**FR-001: Display Books in Table**
**Description:** System shall display all books in a paginated table with columns for Cover, Title, Author, Genre, Year, and Actions.

**User Stories:**
- As a library administrator, I want to see all books in a table format so that I can quickly scan the collection
- As a catalog manager, I want to view book covers in the table so that I can visually identify books

**Acceptance Criteria:**
- [ ] Table displays 6 columns: Cover (thumbnail), Title, Author, Genre, Year, Actions
- [ ] Cover images display as thumbnails (50x75px approximately)
- [ ] Missing cover images show placeholder icon
- [ ] Table is sortable by Title, Author, Genre, and Year (ascending/descending)
- [ ] Actions column shows Edit (blue) and Delete (red) buttons for each row
- [ ] Table rows are clickable to navigate to Book Details
- [ ] Table handles empty state with "No books yet. Add your first book!" message

**Priority:** Must Have
**Requirement ID:** FR-001

---

**FR-002: Pagination**
**Description:** System shall paginate book listings with configurable page size and navigation controls.

**User Stories:**
- As a user, I want to navigate through multiple pages of books so that I can browse large collections efficiently

**Acceptance Criteria:**
- [ ] Default page size is 4 books per page (configurable)
- [ ] Pagination displays: "Showing X-Y of Z books"
- [ ] Previous (◀) and Next (▶) buttons for navigation
- [ ] Page number buttons for direct page access
- [ ] Current page highlighted with blue background
- [ ] Previous button disabled on first page
- [ ] Next button disabled on last page
- [ ] Pagination updates when filters are applied

**Priority:** Must Have
**Requirement ID:** FR-002

---

**FR-003: Quick Filter by Genre, Author, Year**
**Description:** System shall provide dropdown filters for Genre, Author, and Year in the Books List View.

**User Stories:**
- As a catalog manager, I want to filter books by genre so that I can view specific categories
- As a user, I want to filter by publication year so that I can find books from a specific era

**Acceptance Criteria:**
- [ ] Three dropdown filters displayed above table: Genre, Author, Year
- [ ] Genre dropdown populated with all unique genres in collection
- [ ] Author dropdown populated with all unique authors
- [ ] Year dropdown populated with all unique publication years (sorted descending)
- [ ] Filters apply immediately on selection (no "Apply" button needed)
- [ ] Multiple filters work together (AND logic)
- [ ] Filters can be cleared by deselecting
- [ ] Filtered results update table and pagination count
- [ ] Empty results show "No books match your filters" message

**Priority:** Should Have
**Requirement ID:** FR-003

---

**FR-004: Header Quick Search**
**Description:** System shall provide real-time search functionality in the header search bar.

**User Stories:**
- As a user, I want to quickly search for books from any screen so that I can find books without navigating to search page

**Acceptance Criteria:**
- [ ] Search bar present in header with placeholder "Search books..."
- [ ] Search executes as user types (debounced 300ms)
- [ ] Search matches against title, author, ISBN (partial, case-insensitive)
- [ ] Results filtered in real-time in Books List table
- [ ] Search works in combination with dropdown filters
- [ ] Clear "X" button appears when search has text
- [ ] Empty search results show "No books found" message with "Clear search" link
- [ ] Search input accessible via keyboard (Tab navigation)

**Priority:** Should Have
**Requirement ID:** FR-004

---

**FR-005: Navigate to Add Book**
**Description:** System shall provide "+ Add Book" button in header to navigate to Add Book Form.

**User Stories:**
- As a library administrator, I want quick access to add new books so that I can add books without searching through menus

**Acceptance Criteria:**
- [ ] "+ Add Book" button displayed in top-right of header
- [ ] Button styled as primary action (green #4CAF50)
- [ ] Button visible on all screens
- [ ] Clicking button navigates to Add Book Form screen
- [ ] Navigation preserves any active search/filter state for return

**Priority:** Must Have
**Requirement ID:** FR-005

---

**FR-006: Edit Book from List**
**Description:** System shall provide Edit button on each table row to navigate to Edit Book Form.

**User Stories:**
- As a user, I want to edit book details directly from the list so that I can quickly update information

**Acceptance Criteria:**
- [ ] Blue "Edit" button displayed in Actions column for each book
- [ ] Clicking Edit navigates to Edit Book Form for that book
- [ ] Edit form pre-populated with book's current data
- [ ] After successful edit, user returned to Books List view
- [ ] Edit button accessible via keyboard navigation

**Priority:** Must Have
**Requirement ID:** FR-006

---

**FR-007: Delete Book from List**
**Description:** System shall provide Delete button on each table row with confirmation modal.

**User Stories:**
- As a user, I want to delete books from the list with confirmation so that I can remove obsolete books safely

**Acceptance Criteria:**
- [ ] Red "Delete" button displayed in Actions column for each book
- [ ] Clicking Delete navigates to Book Details page with delete modal
- [ ] Modal overlay dims background
- [ ] Modal shows warning icon and "⚠ Confirm Delete" title
- [ ] Modal displays book title and ISBN for confirmation
- [ ] Modal shows "This action cannot be undone" warning
- [ ] Modal provides "Cancel" (gray) and "Delete Permanently" (red) buttons
- [ ] Cancel closes modal without deleting
- [ ] Delete Permanently removes book and redirects to Books List
- [ ] Success toast shows "Book deleted permanently"
- [ ] ESC key closes modal

**Priority:** Must Have
**Requirement ID:** FR-007

---

**FR-008: Navigate to Book Details**
**Description:** System shall allow clicking on book row to navigate to Book Details screen.

**User Stories:**
- As a user, I want to click on a book to see its full details so that I can view comprehensive information

**Acceptance Criteria:**
- [ ] Clicking anywhere on table row (except action buttons) navigates to Book Details
- [ ] Row shows hover state (background color change) to indicate clickability
- [ ] Cursor changes to pointer on row hover
- [ ] Navigation passes book ID to Book Details screen
- [ ] Back button on Book Details returns to Books List

**Priority:** Must Have
**Requirement ID:** FR-008

---

**FR-009: Global Header and Footer**
**Description:** System shall display consistent header and footer on all screens.

**User Stories:**
- As a user, I want consistent navigation across all screens so that I always know where I am and can navigate easily

**Acceptance Criteria:**
- [ ] Header displays "Books Online" logo/text on left
- [ ] Header displays search bar in center (on Books List view)
- [ ] Header displays context-appropriate action button on right
- [ ] Footer displays copyright: "© 2024 Books Online. All rights reserved."
- [ ] Footer has dark background (#333333) and light text
- [ ] Header and footer present on all 5 screens

**Priority:** Must Have
**Requirement ID:** FR-009

---

**FR-010: Empty State Handling**
**Description:** System shall display helpful messages when no books exist or search returns no results.

**User Stories:**
- As a new user, I want guidance when the system is empty so that I know how to get started

**Acceptance Criteria:**
- [ ] Empty collection shows: "No books yet. Add your first book!" with prominent + Add Book button
- [ ] Empty search results show: "No books found" with "Try different search terms" suggestion
- [ ] Empty filter results show: "No books match your filters" with "Clear filters" button
- [ ] Empty states include relevant icon or illustration
- [ ] Empty states provide actionable next steps

**Priority:** Should Have
**Requirement ID:** FR-010

---

#### Feature 2: Add New Book (FR-011 to FR-020)

**FR-011: Add Book Form Layout**
**Description:** System shall provide comprehensive form for adding new books with all required and optional fields.

**User Stories:**
- As a library administrator, I want to add new books with complete metadata so that the catalog is comprehensive and accurate

**Acceptance Criteria:**
- [ ] Form displays with 2-column layout (desktop)
- [ ] Left column: Cover image upload area (large)
- [ ] Right column: All input fields in 2-column grid
- [ ] Required fields marked with red asterisk (*)
- [ ] Form header shows screen title "Add New Book"
- [ ] Back button (← Back) in top-right corner
- [ ] Bottom action buttons: Cancel (left), Add Book (right, green)
- [ ] Form scales to single column on mobile (<768px)

**Priority:** Must Have
**Requirement ID:** FR-011

---

**FR-012: Book Cover Upload**
**Description:** System shall allow image upload for book cover with drag-drop and click-to-upload support.

**User Stories:**
- As a user, I want to upload book cover images so that books are visually identifiable in the catalog

**Acceptance Criteria:**
- [ ] Large upload area with dashed border and upload icon
- [ ] Text: "Click to upload or drag and drop"
- [ ] Supported formats: JPEG, PNG, GIF, WebP
- [ ] Maximum file size: 5MB
- [ ] Image preview shows immediately after upload
- [ ] "Remove" or "Change" button appears after upload
- [ ] File validation with error messages for unsupported types/sizes
- [ ] Optional field - form can be submitted without cover

**Priority:** Should Have
**Requirement ID:** FR-012

---

**FR-013: Required Fields Validation**
**Description:** System shall enforce validation for all required book fields: Title, ISBN, Author, Genre, Publication Year, Language.

**User Stories:**
- As a system, I want to ensure required fields are provided so that book records are complete and usable

**Acceptance Criteria:**
- [ ] Title: Required, 1-200 characters, text input
- [ ] ISBN: Required, valid ISBN-10 or ISBN-13 format, unique in database
- [ ] Author: Required, 1-100 characters, text input
- [ ] Genre: Required, dropdown selection from predefined list
- [ ] Publication Year: Required, integer, 1000 to current year, YYYY format
- [ ] Language: Required, dropdown selection from predefined list
- [ ] Red error message displays below field if validation fails
- [ ] Error messages appear on blur or on submit attempt
- [ ] Form cannot be submitted until all required fields valid
- [ ] Submit button remains enabled but shows errors on click

**Priority:** Must Have
**Requirement ID:** FR-013

---

**FR-014: Optional Fields**
**Description:** System shall accept optional metadata: Publisher, Pages, Price, Stock Quantity, Description.

**User Stories:**
- As a user, I want to provide additional book details when available so that the catalog is as complete as possible

**Acceptance Criteria:**
- [ ] Publisher: Optional, max 100 characters, text input
- [ ] Pages: Optional, positive integer, number input
- [ ] Price: Optional, decimal (10,2), min $0.00, currency format
- [ ] Stock Quantity: Optional, integer >= 0, number input, default 0
- [ ] Description: Optional, max 5000 characters, textarea (multi-line)
- [ ] All optional fields have placeholder text
- [ ] Optional fields validated only if provided (format, range)

**Priority:** Should Have
**Requirement ID:** FR-014

---

**FR-015: Genre Dropdown Options**
**Description:** System shall provide predefined list of genres: Fiction, Non-Fiction, Dystopian, Romance, Mystery, Science, Biography, Fantasy.

**User Stories:**
- As a user, I want to select from standard genres so that book categorization is consistent

**Acceptance Criteria:**
- [ ] Dropdown displays 8 genre options
- [ ] Options: Fiction, Non-Fiction, Dystopian, Romance, Mystery, Science, Biography, Fantasy
- [ ] Placeholder: "Select genre"
- [ ] Dropdown arrow (▼) indicates interactive element
- [ ] Required field validation applies

**Priority:** Must Have
**Requirement ID:** FR-015

---

**FR-016: Language Dropdown Options**
**Description:** System shall provide predefined list of languages: English, Spanish, French, German, Italian, Portuguese, Chinese, Japanese, Korean, Arabic.

**User Stories:**
- As a user, I want to select from supported languages so that language information is standardized

**Acceptance Criteria:**
- [ ] Dropdown displays 10 language options
- [ ] Options: English, Spanish, French, German, Italian, Portuguese, Chinese, Japanese, Korean, Arabic
- [ ] Placeholder: "Select language"
- [ ] Required field validation applies

**Priority:** Must Have
**Requirement ID:** FR-016

---

**FR-017: ISBN Validation and Uniqueness**
**Description:** System shall validate ISBN format and ensure uniqueness in database.

**User Stories:**
- As a system, I want to ensure ISBNs are valid and unique so that books are correctly identified and no duplicates exist

**Acceptance Criteria:**
- [ ] ISBN must match ISBN-10 (10 digits with optional hyphens) or ISBN-13 (13 digits with optional hyphens) format
- [ ] System validates format on blur
- [ ] System checks uniqueness against database on blur
- [ ] Error message: "ISBN is required and must be a valid ISBN-10 or ISBN-13 format"
- [ ] Error message for duplicate: "This ISBN already exists in the system"
- [ ] Placeholder shows example: "978-0-00-000000-0"

**Priority:** Must Have
**Requirement ID:** FR-017

---

**FR-018: Submit Add Book**
**Description:** System shall validate all fields, submit book data to backend via POST /api/v1/books, and handle success/error responses.

**User Stories:**
- As a user, I want to save new book data so that it is added to the catalog permanently

**Acceptance Criteria:**
- [ ] Clicking "Add Book" button triggers form validation
- [ ] If validation fails, display error messages and prevent submission
- [ ] If validation passes, send POST request to /api/v1/books
- [ ] Request body includes all form data as JSON
- [ ] Cover image uploaded separately if provided
- [ ] Loading state shown during submission (button disabled, "Saving..." text)
- [ ] On success (201 Created), show success toast: "Book added successfully!"
- [ ] On success, navigate to Books List View with new book visible
- [ ] On error (400/409/500), show error toast with message
- [ ] On error, remain on form with data intact for correction

**Priority:** Must Have
**Requirement ID:** FR-018

---

**FR-019: Cancel Add Book**
**Description:** System shall allow user to cancel adding book and return to Books List without saving.

**User Stories:**
- As a user, I want to cancel adding a book so that I can return to the list without saving incomplete data

**Acceptance Criteria:**
- [ ] "Cancel" button displayed at bottom-left of form (gray)
- [ ] Clicking Cancel navigates to Books List View
- [ ] No data is saved to database
- [ ] No confirmation dialog shown (data loss is expected)
- [ ] "← Back" button in header also cancels and returns to list

**Priority:** Must Have
**Requirement ID:** FR-019

---

**FR-020: System Auto-Generated Fields**
**Description:** System shall automatically set createdAt, updatedAt, isAvailable, viewCount, reviewCount, and rating on book creation.

**User Stories:**
- As a system, I want to automatically manage metadata fields so that data integrity is maintained

**Acceptance Criteria:**
- [ ] id: Auto-generated by database (primary key, auto-increment)
- [ ] createdAt: Set to current timestamp on creation
- [ ] updatedAt: Set to current timestamp on creation
- [ ] isAvailable: Computed as true if stockQuantity > 0, else false
- [ ] viewCount: Initialized to 0
- [ ] reviewCount: Initialized to 0
- [ ] rating: Initialized to NULL
- [ ] User cannot modify these fields via form

**Priority:** Must Have
**Requirement ID:** FR-020

---

#### Feature 3: Advanced Search (FR-021 to FR-030)

**FR-021: Search Books Screen Layout**
**Description:** System shall provide dedicated search screen with Quick Search and Advanced Search sections.

**User Stories:**
- As a user, I want a dedicated search page with advanced options so that I can find books using multiple criteria

**Acceptance Criteria:**
- [ ] Screen title: "Search Books"
- [ ] Quick Search section at top: Large search input with search icon and green "Search" button
- [ ] Advanced Search section below: Expandable/collapsible form with multiple fields
- [ ] Search Tips banner: "Tip: Use quotation marks for exact phrase matches, or leave fields empty to search all books."
- [ ] Back button (← Back) navigates to Books List
- [ ] Form layout: 2-column grid on desktop, single column on mobile

**Priority:** Should Have
**Requirement ID:** FR-021

---

**FR-022: Quick Search**
**Description:** System shall provide single large search input for quick searches across title, author, ISBN, and keywords.

**User Stories:**
- As a user, I want to search quickly with a single search box so that I can find books without filling multiple fields

**Acceptance Criteria:**
- [ ] Large search input with placeholder: "Search by title, author, ISBN, or keyword..."
- [ ] Search icon displayed in input
- [ ] Green "Search" button to execute search
- [ ] Search matches title, author, ISBN (partial, case-insensitive)
- [ ] Search executes on button click or Enter key
- [ ] Results displayed in Books List View
- [ ] Search term preserved in URL query parameter for bookmark/share

**Priority:** Should Have
**Requirement ID:** FR-022

---

**FR-023: Advanced Search - Basic Fields**
**Description:** System shall provide text inputs for Title, Author, ISBN, and Publisher in Advanced Search.

**User Stories:**
- As a user, I want to search by specific fields so that I can narrow results precisely

**Acceptance Criteria:**
- [ ] Title input: Placeholder "Search by title", partial match
- [ ] Author input: Placeholder "Search by author", partial match
- [ ] ISBN input: Placeholder "978-0-00-000000-0", exact match
- [ ] Publisher input: Placeholder "Search by publisher", partial match
- [ ] All searches case-insensitive
- [ ] Multiple filled fields use AND logic (all must match)

**Priority:** Should Have
**Requirement ID:** FR-023

---

**FR-024: Advanced Search - Filter Dropdowns**
**Description:** System shall provide dropdown filters for Genre, Language, and Availability.

**User Stories:**
- As a user, I want to filter search results by categorical fields so that I can find books in specific categories

**Acceptance Criteria:**
- [ ] Genre dropdown: Same options as Add Book form (8 genres)
- [ ] Language dropdown: Same options as Add Book form (10 languages)
- [ ] Availability dropdown: Options: "All", "Available (In Stock)", "Out of Stock"
- [ ] All dropdowns optional (can be left unselected)
- [ ] Filters use exact match

**Priority:** Should Have
**Requirement ID:** FR-024

---

**FR-025: Advanced Search - Year Range**
**Description:** System shall provide From and To year inputs for publication year range search.

**User Stories:**
- As a user, I want to search for books published within a date range so that I can find books from specific eras

**Acceptance Criteria:**
- [ ] From (YYYY) input: 4-digit year, placeholder "From (YYYY)"
- [ ] To (YYYY) input: 4-digit year, placeholder "To (YYYY)"
- [ ] Both fields optional
- [ ] If only From provided: publicationYear >= From
- [ ] If only To provided: publicationYear <= To
- [ ] If both provided: publicationYear BETWEEN From AND To
- [ ] Validation: From <= To if both provided

**Priority:** Should Have
**Requirement ID:** FR-025

---

**FR-026: Advanced Search - Price Range**
**Description:** System shall provide Min and Max price inputs for price range search.

**User Stories:**
- As a user, I want to search for books within a price range so that I can find books fitting my budget

**Acceptance Criteria:**
- [ ] Min ($) input: Decimal, placeholder "Min ($)"
- [ ] Max ($) input: Decimal, placeholder "Max ($)"
- [ ] Both fields optional
- [ ] If only Min provided: price >= Min
- [ ] If only Max provided: price <= Max
- [ ] If both provided: price BETWEEN Min AND Max
- [ ] Validation: Min <= Max if both provided

**Priority:** Could Have
**Requirement ID:** FR-026

---

**FR-027: Advanced Search - Sort Options**
**Description:** System shall provide Sort Results By dropdown with options: Relevance, Title (A-Z), Author (A-Z), Year (Newest First), Year (Oldest First), Price (Low to High), Price (High to Low), Rating (Highest First).

**User Stories:**
- As a user, I want to sort search results so that I can view books in my preferred order

**Acceptance Criteria:**
- [ ] Dropdown with 8 sort options
- [ ] Default: "Relevance" (for quick search) or "Title (A-Z)" (for advanced search)
- [ ] Sort applies to results before display
- [ ] Sort persists across pagination

**Priority:** Should Have
**Requirement ID:** FR-027

---

**FR-028: Advanced Search - Checkboxes**
**Description:** System shall provide three checkbox filters: In Stock Only, On Sale, New Releases.

**User Stories:**
- As a user, I want additional boolean filters so that I can find books meeting specific criteria

**Acceptance Criteria:**
- [ ] "In Stock Only" checkbox: Filters books where stockQuantity > 0
- [ ] "On Sale" checkbox: Filters books with discount (future: price < originalPrice)
- [ ] "New Releases" checkbox: Filters books where publicationYear >= currentYear - 1
- [ ] All checkboxes unchecked by default
- [ ] Multiple checkboxes can be selected (AND logic)

**Priority:** Could Have
**Requirement ID:** FR-028

---

**FR-029: Execute Advanced Search**
**Description:** System shall submit advanced search criteria to backend via GET /api/v1/books/search and display results.

**User Stories:**
- As a user, I want to execute my advanced search so that I can see matching books

**Acceptance Criteria:**
- [ ] Clicking "Search" button sends GET request with all filled criteria as query parameters
- [ ] Empty/unfilled fields omitted from request
- [ ] Loading state shown during search
- [ ] Results displayed in Books List View (replaces current list)
- [ ] Results include pagination if >4 books
- [ ] Results show count: "Found X books matching your criteria"
- [ ] No results shows: "No books found. Try different criteria."
- [ ] Search criteria preserved for back navigation

**Priority:** Should Have
**Requirement ID:** FR-029

---

**FR-030: Clear Filters**
**Description:** System shall provide "Clear Filters" button to reset all advanced search fields.

**User Stories:**
- As a user, I want to clear all search criteria so that I can start a new search without manually clearing each field

**Acceptance Criteria:**
- [ ] "Clear Filters" button displayed in Advanced Search form (gray)
- [ ] Clicking button resets all text inputs to empty
- [ ] Clicking button resets all dropdowns to default/placeholder
- [ ] Clicking button unchecks all checkboxes
- [ ] No API call made on clear (local state reset only)
- [ ] User can then enter new criteria or search to show all books

**Priority:** Should Have
**Requirement ID:** FR-030

---

#### Feature 4: Edit/Update Book (FR-031 to FR-040)

**FR-031: Edit Book Form Layout**
**Description:** System shall provide pre-populated form for editing existing book with same layout as Add Book form.

**User Stories:**
- As a user, I want to edit book information so that I can correct errors or update details

**Acceptance Criteria:**
- [ ] Form identical layout to Add Book form (2-column, cover + fields)
- [ ] Screen title: "Edit Book"
- [ ] All fields pre-filled with current book data
- [ ] Book ID and Last Updated date displayed in info banner at top
- [ ] ISBN field is read-only (styled with lock icon 🔒)
- [ ] Bottom action buttons: Cancel (left), Update Book (right, orange #FF9800)

**Priority:** Must Have
**Requirement ID:** FR-031

---

**FR-032: Load Existing Book Data**
**Description:** System shall fetch book data via GET /api/v1/books/{id} and pre-populate all form fields.

**User Stories:**
- As a system, I want to load current book data so that the user can see what they're editing

**Acceptance Criteria:**
- [ ] On screen load, fetch book via GET /api/v1/books/{id}
- [ ] Display loading state while fetching
- [ ] On success, populate all form fields with current values
- [ ] Cover image preview shows current cover if exists
- [ ] Info banner shows: "Book ID: {id} | Last Updated: {date}"
- [ ] On fetch error (404/500), show error and redirect to Books List

**Priority:** Must Have
**Requirement ID:** FR-032

---

**FR-033: Book ID and Metadata Display**
**Description:** System shall display book ID, last updated timestamp, and other metadata in prominent info banner.

**User Stories:**
- As a user, I want to see book ID and update history so that I know which book I'm editing and when it was last changed

**Acceptance Criteria:**
- [ ] Yellow/warning background info banner at top of form
- [ ] Text: "Book ID: BK-2024-001234 | Last Updated: 2024-01-15"
- [ ] Book ID format: BK-{YEAR}-{6-digit-padded-id}
- [ ] Last Updated shows date in YYYY-MM-DD format

**Priority:** Should Have
**Requirement ID:** FR-033

---

**FR-034: ISBN Immutability**
**Description:** System shall display ISBN as read-only field that cannot be modified.

**User Stories:**
- As a system, I want to prevent ISBN changes so that book identity remains consistent (ISBN is unique identifier)

**Acceptance Criteria:**
- [ ] ISBN input field styled as read-only (grayed out, cursor not-allowed)
- [ ] Lock icon (🔒) displayed next to ISBN label
- [ ] Tooltip on hover: "ISBN cannot be changed after creation"
- [ ] ISBN value visible but not editable
- [ ] ISBN not included in update request payload

**Priority:** Must Have
**Requirement ID:** FR-034

---

**FR-035: Cover Image Change**
**Description:** System shall allow changing book cover with "Change Image" button replacing upload area.

**User Stories:**
- As a user, I want to update the book cover image so that I can replace incorrect or low-quality images

**Acceptance Criteria:**
- [ ] Current cover image displayed as preview (larger than table thumbnail)
- [ ] "Change Image" button overlaid or adjacent to image
- [ ] Clicking "Change Image" opens file picker
- [ ] Selected image replaces preview immediately
- [ ] Image upload same validation as Add Book (format, size)
- [ ] User can upload new image or keep existing

**Priority:** Should Have
**Requirement ID:** FR-035

---

**FR-036: Modify Editable Fields**
**Description:** System shall allow modifying all fields except ISBN, id, createdAt.

**User Stories:**
- As a user, I want to change any book detail except ISBN so that I can keep information up-to-date

**Acceptance Criteria:**
- [ ] All fields editable except: ISBN (read-only), id (not displayed), createdAt (not displayed)
- [ ] Editable fields: title, author, publisher, genre, publicationYear, pages, language, price, stockQuantity, description, coverImage
- [ ] Same validation rules as Add Book form
- [ ] User can modify one field or multiple fields
- [ ] Changes not saved until "Update Book" clicked

**Priority:** Must Have
**Requirement ID:** FR-036

---

**FR-037: Submit Update Book**
**Description:** System shall validate changes, submit via PUT /api/v1/books/{id}, and handle success/error responses.

**User Stories:**
- As a user, I want to save my changes so that the book record is updated permanently

**Acceptance Criteria:**
- [ ] Clicking "Update Book" triggers validation
- [ ] If validation fails, show errors and prevent submission
- [ ] If validation passes, send PUT request to /api/v1/books/{id}
- [ ] Request body includes all modified fields
- [ ] Loading state during submission (button disabled, "Updating..." text)
- [ ] On success (200 OK), show success toast: "Book updated successfully!"
- [ ] On success, navigate to Book Details or Books List View
- [ ] System sets updatedAt = current timestamp on backend
- [ ] System recalculates isAvailable based on new stockQuantity
- [ ] On error, show error toast and remain on form

**Priority:** Must Have
**Requirement ID:** FR-037

---

**FR-038: No Changes Detection**
**Description:** System shall detect when no changes were made and show informational message instead of submitting.

**User Stories:**
- As a user, I want to know when I haven't made any changes so that I don't submit unnecessarily

**Acceptance Criteria:**
- [ ] System compares current form values with original loaded values
- [ ] If identical (no changes), show info toast: "No changes detected"
- [ ] Do not send API request if no changes
- [ ] User remains on form or can navigate away

**Priority:** Could Have
**Requirement ID:** FR-038

---

**FR-039: Cancel Edit Book**
**Description:** System shall allow canceling edit and returning to previous screen without saving changes.

**User Stories:**
- As a user, I want to cancel editing so that I can discard unwanted changes

**Acceptance Criteria:**
- [ ] "Cancel" button at bottom-left (gray)
- [ ] Clicking Cancel navigates to Book Details screen
- [ ] Alternative: "← Back" button navigates to Books List
- [ ] No changes saved to database
- [ ] No confirmation dialog (data loss expected)

**Priority:** Must Have
**Requirement ID:** FR-039

---

**FR-040: Update Automatic Fields**
**Description:** System shall automatically update updatedAt timestamp and recalculate isAvailable on book update.

**User Stories:**
- As a system, I want to maintain accurate metadata so that change history is tracked

**Acceptance Criteria:**
- [ ] updatedAt set to current timestamp on every update
- [ ] isAvailable recalculated: true if stockQuantity > 0, else false
- [ ] User cannot directly modify these fields
- [ ] Changes visible immediately after update

**Priority:** Must Have
**Requirement ID:** FR-040

---

#### Feature 5: View Book Details (FR-041 to FR-050)

**FR-041: Book Details Screen Layout**
**Description:** System shall display comprehensive book information in rich detail view with large cover, metadata grid, and action buttons.

**User Stories:**
- As a user, I want to view all information about a book so that I can see comprehensive details in one place

**Acceptance Criteria:**
- [ ] Left column: Large cover image (300x450px approximately)
- [ ] Right column: Book title (H1), author (H2), rating, price box, details grid, description
- [ ] Availability badge overlaid on cover: "In Stock" (green) or "Out of Stock" (red)
- [ ] Bottom metadata bar: Book ID, Added date, Last Updated date, View count
- [ ] Bottom action buttons: Edit Book, Duplicate, Export Data, Delete Book
- [ ] Back button (← Back) in top-right navigates to Books List

**Priority:** Must Have
**Requirement ID:** FR-041

---

**FR-042: Load Book Details**
**Description:** System shall fetch book via GET /api/v1/books/{id} and increment view count.

**User Stories:**
- As a system, I want to load and display complete book details so that users can view all information

**Acceptance Criteria:**
- [ ] On screen load, fetch book via GET /api/v1/books/{id}
- [ ] Display loading skeleton while fetching
- [ ] On success, display all 18 book fields
- [ ] After displaying, send PUT request to increment viewCount by 1
- [ ] View count increment happens asynchronously (doesn't block display)
- [ ] On fetch error (404), show "Book not found" and redirect to Books List
- [ ] On fetch error (500), show error toast

**Priority:** Must Have
**Requirement ID:** FR-042

---

**FR-043: Display Large Cover Image**
**Description:** System shall display book cover in large format with availability badge overlay.

**User Stories:**
- As a user, I want to see the book cover prominently so that I can visually identify the book

**Acceptance Criteria:**
- [ ] Cover displayed at ~300x450px (or aspect-fit to container)
- [ ] If no cover exists, show placeholder image
- [ ] Availability badge positioned in top-right corner of image
- [ ] Badge shows "In Stock" (green) if isAvailable = true
- [ ] Badge shows "Out of Stock" (red) if isAvailable = false
- [ ] Badge has contrasting text color for readability

**Priority:** Should Have
**Requirement ID:** FR-043

---

**FR-044: Display Title, Author, and Rating**
**Description:** System shall prominently display book title, author name, and star rating with review count.

**User Stories:**
- As a user, I want to see the title, author, and rating clearly so that I can quickly identify the book and its popularity

**Acceptance Criteria:**
- [ ] Title displayed as H1 heading (24-28px font)
- [ ] Author displayed as secondary text: "by {author name}"
- [ ] Rating displayed as stars (★★★★☆) with numeric value
- [ ] Rating shows: "{rating} / 5.0 ({reviewCount} reviews)"
- [ ] Example: "4.2 / 5.0 (1,234 reviews)"
- [ ] If rating is NULL, display: "No ratings yet"
- [ ] Stars partially filled based on decimal rating (e.g., 4.2 shows 4 full stars + partial 5th)

**Priority:** Should Have
**Requirement ID:** FR-044

---

**FR-045: Display Price Box**
**Description:** System shall display price information with current price, original price (if different), discount, and stock quantity.

**User Stories:**
- As a user, I want to see pricing and availability so that I know if the book is in stock and affordable

**Acceptance Criteria:**
- [ ] Price box with border and background color
- [ ] Current price displayed prominently: "$15.99" (large, bold font)
- [ ] If original price exists and differs: Show original price as strikethrough and discount percentage
- [ ] Example: "~$19.99~ $15.99 (20% OFF)"
- [ ] Stock info displayed: "{stockQuantity} copies available"
- [ ] If stockQuantity = 0: "Out of stock"
- [ ] Price formatted with currency symbol and 2 decimal places

**Priority:** Should Have
**Requirement ID:** FR-045

---

**FR-046: Display Details Grid**
**Description:** System shall display book metadata in definition list format: ISBN, Publisher, Publication Year, Language, Pages, Genre.

**User Stories:**
- As a user, I want to see all book metadata so that I have complete information

**Acceptance Criteria:**
- [ ] Details displayed as label-value pairs in 2-column grid
- [ ] Labels: ISBN, Publisher, Publication Year, Language, Pages, Genre
- [ ] Values populated from book data
- [ ] If value is NULL, display: "—" or "Not specified"
- [ ] Grid responsive: single column on mobile

**Priority:** Must Have
**Requirement ID:** FR-046

---

**FR-047: Display Description**
**Description:** System shall display full book description in text block below details grid.

**User Stories:**
- As a user, I want to read the book description so that I understand what the book is about

**Acceptance Criteria:**
- [ ] Section header: "Description"
- [ ] Full description text displayed with proper line breaks
- [ ] If description is NULL or empty, display: "No description available."
- [ ] Long descriptions support scrolling or expand/collapse (if >500 chars)

**Priority:** Should Have
**Requirement ID:** FR-047

---

**FR-048: Display Metadata Bar**
**Description:** System shall display metadata footer showing Book ID, Added date, Last Updated date, and View count.

**User Stories:**
- As a user, I want to see when the book was added and last updated so that I know data recency

**Acceptance Criteria:**
- [ ] Gray background info bar at bottom of detail view
- [ ] Format: "Book ID: BK-2024-001234 | Added: 2023-12-10 | Last Updated: 2024-01-15 | Views: 3,456"
- [ ] Dates formatted as YYYY-MM-DD
- [ ] View count formatted with comma separators for large numbers

**Priority:** Could Have
**Requirement ID:** FR-048

---

**FR-049: Navigate to Edit from Details**
**Description:** System shall provide "Edit Book" button to navigate to Edit Book Form.

**User Stories:**
- As a user, I want to edit book details from the details view so that I can quickly make changes

**Acceptance Criteria:**
- [ ] Blue "Edit Book" button at bottom of screen
- [ ] Clicking navigates to Edit Book Form with book ID
- [ ] Edit form pre-populated with current data (as per FR-032)

**Priority:** Must Have
**Requirement ID:** FR-049

---

**FR-050: Back Navigation**
**Description:** System shall provide "← Back" button to return to Books List View.

**User Stories:**
- As a user, I want to return to the list after viewing details so that I can continue browsing

**Acceptance Criteria:**
- [ ] "← Back" button in top-right corner of screen
- [ ] Clicking navigates to Books List View
- [ ] Books List preserves any active filters/search/pagination from before navigation

**Priority:** Must Have
**Requirement ID:** FR-050

---

#### Feature 6: Delete Book with Confirmation (FR-051 to FR-055)

**FR-051: Delete Button from Details**
**Description:** System shall provide "Delete Book" button on Book Details screen that triggers delete confirmation modal.

**User Stories:**
- As a user, I want to delete books that are obsolete or incorrect so that the catalog remains accurate

**Acceptance Criteria:**
- [ ] Red "Delete Book" button at bottom of Book Details screen
- [ ] Button color: #F44336 (danger red)
- [ ] Clicking button shows modal overlay (does not immediately delete)
- [ ] Background dims to indicate modal focus

**Priority:** Must Have
**Requirement ID:** FR-051

---

**FR-052: Delete Confirmation Modal**
**Description:** System shall display modal dialog with book details and confirmation buttons to prevent accidental deletion.

**User Stories:**
- As a user, I want to confirm deletion with book details shown so that I don't accidentally delete the wrong book

**Acceptance Criteria:**
- [ ] Modal centered on screen with overlay background
- [ ] Modal title: "⚠ Confirm Delete" with warning icon
- [ ] Modal body shows highlighted box with book details:
  - Title: {book title}
  - ISBN: {book isbn}
- [ ] Warning text: "This action cannot be undone"
- [ ] Two action buttons: "Cancel" (gray, left) and "Delete Permanently" (red, right)
- [ ] ESC key closes modal without deleting
- [ ] Clicking outside modal closes without deleting
- [ ] Focus trapped within modal for accessibility

**Priority:** Must Have
**Requirement ID:** FR-052

---

**FR-053: Cancel Delete**
**Description:** System shall allow canceling deletion, closing modal and returning to Book Details.

**User Stories:**
- As a user, I want to cancel deletion if I change my mind so that the book is not deleted

**Acceptance Criteria:**
- [ ] Clicking "Cancel" button closes modal
- [ ] No API request sent
- [ ] User returned to Book Details view unchanged
- [ ] ESC key also cancels

**Priority:** Must Have
**Requirement ID:** FR-053

---

**FR-054: Confirm Delete**
**Description:** System shall execute deletion via DELETE /api/v1/books/{id} and redirect to Books List on success.

**User Stories:**
- As a user, I want to permanently delete the book when I confirm so that it is removed from the catalog

**Acceptance Criteria:**
- [ ] Clicking "Delete Permanently" sends DELETE request to /api/v1/books/{id}
- [ ] Modal shows loading state during deletion (button disabled, spinner)
- [ ] On success (200/204), show success toast: "Book deleted permanently"
- [ ] On success, navigate to Books List View
- [ ] Deleted book no longer appears in list
- [ ] Pagination updates to reflect new count
- [ ] On error (404/500), show error toast and close modal

**Priority:** Must Have
**Requirement ID:** FR-054

---

**FR-055: Hard Delete Implementation**
**Description:** System shall perform hard delete (permanent removal from database) rather than soft delete.

**User Stories:**
- As a system, I want to permanently remove deleted books so that database remains clean (V1 implementation)

**Acceptance Criteria:**
- [ ] DELETE request removes book row from database
- [ ] Book ID no longer exists in system
- [ ] Attempting to access deleted book via URL returns 404
- [ ] No soft delete flag (isDeleted) used in V1
- [ ] Note: Future versions may implement soft delete for audit trail

**Priority:** Must Have
**Requirement ID:** FR-055

---

#### Feature 7: Duplicate Book (FR-056 to FR-058)

**FR-056: Duplicate Button from Details**
**Description:** System shall provide "Duplicate" button on Book Details screen to create copy of book.

**User Stories:**
- As a data entry clerk, I want to duplicate books so that I can quickly add similar books with minor changes

**Acceptance Criteria:**
- [ ] Orange "Duplicate" button at bottom of Book Details screen
- [ ] Button color: #FF9800
- [ ] Clicking button creates copy and navigates to Edit form

**Priority:** Could Have
**Requirement ID:** FR-056

---

**FR-057: Create Book Copy**
**Description:** System shall create copy of book via POST /api/v1/books/{id}/duplicate with modified title and new ISBN requirement.

**User Stories:**
- As a system, I want to create a duplicate book record so that user can make modifications before saving

**Acceptance Criteria:**
- [ ] Clicking Duplicate sends POST to /api/v1/books/{id}/duplicate
- [ ] System creates new book record with:
  - All fields copied from original
  - Title appended with " (Copy)"
  - ISBN field cleared (must be unique, user must enter new ISBN)
  - New id generated
  - createdAt, updatedAt set to current timestamp
  - viewCount, reviewCount, rating reset to 0/NULL
- [ ] System returns new book ID
- [ ] User navigated to Edit Book Form with new book ID

**Priority:** Could Have
**Requirement ID:** FR-057

---

**FR-058: Edit Duplicated Book**
**Description:** System shall navigate to Edit Book Form for duplicated book, requiring user to modify ISBN before saving.

**User Stories:**
- As a user, I want to edit the duplicated book before final save so that I can provide unique ISBN and adjust details

**Acceptance Criteria:**
- [ ] Edit form opens with duplicated book data
- [ ] Title shows " (Copy)" suffix
- [ ] ISBN field is empty and shows validation error until filled
- [ ] User must provide new unique ISBN
- [ ] User can modify any other fields as needed
- [ ] User can cancel (which deletes the duplicate) or save changes

**Priority:** Could Have
**Requirement ID:** FR-058

---

#### Feature 8: Export Book Data (FR-059 to FR-061)

**FR-059: Export Data Button from Details**
**Description:** System shall provide "Export Data" button on Book Details screen to download book information.

**User Stories:**
- As a user, I want to export book data so that I can use it in external systems or create reports

**Acceptance Criteria:**
- [ ] Gray "Export Data" button at bottom of Book Details screen
- [ ] Clicking button triggers file download

**Priority:** Could Have
**Requirement ID:** FR-059

---

**FR-060: Export Formats**
**Description:** System shall support exporting book data in JSON, CSV, and PDF formats.

**User Stories:**
- As a user, I want to choose export format so that I can use the data in different applications

**Acceptance Criteria:**
- [ ] Clicking Export Data shows format selection dropdown or exports default format (JSON)
- [ ] JSON format: Complete book object with all 18 fields
- [ ] CSV format: Single-row CSV with headers for spreadsheet import
- [ ] PDF format: Formatted document with cover image and all details
- [ ] File downloaded to user's default download location
- [ ] Filename format: book_{isbn}_{date}.{ext}
- [ ] Example: book_9780451524935_2024-02-11.json

**Priority:** Could Have
**Requirement ID:** FR-060

---

**FR-061: Export API Endpoint**
**Description:** System shall provide GET /api/v1/books/{id}/export?format={format} endpoint returning file download.

**User Stories:**
- As a system, I want to generate export files on-demand so that users can download book data

**Acceptance Criteria:**
- [ ] GET /api/v1/books/{id}/export?format=json returns JSON
- [ ] GET /api/v1/books/{id}/export?format=csv returns CSV
- [ ] GET /api/v1/books/{id}/export?format=pdf returns PDF
- [ ] Response includes Content-Disposition header for download
- [ ] Default format is JSON if parameter omitted

**Priority:** Could Have
**Requirement ID:** FR-061

---

### 3.2 User Management

**Note:** User authentication and authorization are **OUT OF SCOPE** for V1. The system is currently designed for single-user or internal network use without login requirements.

**Future V2 Considerations:**
- User registration with email and password
- Login with JWT token authentication
- Password reset functionality
- Role-based access control (Admin, Librarian, Viewer)
- Session management and token refresh
- Logout functionality
- Audit logs tracking user actions

---

### 3.3 Data Management

#### Entity: Book

**CRUD Operations:**

**Create (FR-011 to FR-020):**
- **Screen:** Add Book Form (02_add_book_form.svg)
- **Fields:** All 18 fields (7 required user input, 11 optional user input, 4 system-generated)
- **Validation:** ISBN format and uniqueness, required fields, field length limits, data type validation
- **API:** POST /api/v1/books

**Read (FR-001 to FR-010, FR-021 to FR-030, FR-041 to FR-050):**
- **Screens:** Books List View, Search Books, Book Details
- **Operations:**
  - List all books with pagination (GET /api/v1/books?page=0&size=4)
  - Filter by genre, author, year (GET /api/v1/books?genre=Fiction)
  - Quick search (GET /api/v1/books?search=gatsby)
  - Advanced search with multiple criteria (GET /api/v1/books/search?title=...&author=...)
  - Get single book by ID (GET /api/v1/books/{id})

**Update (FR-031 to FR-040):**
- **Screen:** Edit Book Form (04_update_book_form.svg)
- **Fields:** All fields except ISBN, id, createdAt (14 editable fields)
- **Validation:** Same as Create, minus ISBN uniqueness check
- **API:** PUT /api/v1/books/{id}

**Delete (FR-051 to FR-055):**
- **Screen:** Book Details with Delete Modal (05_book_details_delete.svg)
- **Confirmation:** Required via modal dialog
- **API:** DELETE /api/v1/books/{id}
- **Type:** Hard delete (permanent removal)

**Business Rules:**
1. **ISBN Uniqueness:** ISBN must be unique across all books; enforced at database level with UNIQUE constraint
2. **ISBN Immutability:** ISBN cannot be changed after book creation; enforced by read-only field in edit form
3. **Availability Calculation:** isAvailable automatically computed: true if stockQuantity > 0, false otherwise
4. **Timestamp Management:** createdAt set on INSERT, updatedAt set on INSERT and every UPDATE
5. **View Count Increment:** viewCount incremented by 1 each time Book Details screen accessed
6. **Rating and Review Count:** Display only in V1 (static data); actual review system out of scope for V1
7. **Stock Quantity:** Must be >= 0; cannot be negative
8. **Publication Year:** Must be between 1000 and current year
9. **Price:** If provided, must be >= 0
10. **Field Lengths:** Enforced at database and form validation level

---

## 4. Non-Functional Requirements

### 4.1 Performance

**NFR-001: Page Load Time**
- **Requirement:** Page load time shall be < 2 seconds for all screens on standard broadband (>10 Mbps)
- **Priority:** Must Have
- **Metric:** Time to interactive (TTI) measured via Lighthouse
- **Implementation:** Code splitting, lazy loading, optimized images

**NFR-002: API Response Time**
- **Requirement:** API response time shall be < 500ms for 95% of requests under normal load
- **Priority:** Must Have
- **Metric:** P95 response time measured via application monitoring
- **Implementation:** Database indexes, query optimization, connection pooling

**NFR-003: Search Performance**
- **Requirement:** Search results shall be returned < 1 second for collections up to 10,000 books
- **Priority:** Should Have
- **Metric:** Search query execution time
- **Implementation:** Full-text indexes on title, author, description fields

**NFR-004: Concurrent Users**
- **Requirement:** System shall support 50 concurrent users without performance degradation
- **Priority:** Should Have
- **Metric:** Response time under concurrent load
- **Implementation:** Stateless backend, efficient database queries

**NFR-005: Database Query Optimization**
- **Requirement:** All database queries shall use appropriate indexes and avoid N+1 queries
- **Priority:** Must Have
- **Metric:** Query execution plans, slow query logs
- **Implementation:** JPA eager/lazy loading optimization, database indexes

**NFR-006: Image Loading**
- **Requirement:** Book cover images shall load progressively with placeholders to avoid layout shift
- **Priority:** Should Have
- **Metric:** Cumulative Layout Shift (CLS) < 0.1
- **Implementation:** Skeleton loaders, image lazy loading, aspect-ratio preservation

### 4.2 Security

**NFR-007: HTTPS Enforcement**
- **Requirement:** All API endpoints and web pages shall use HTTPS only in production
- **Priority:** Must Have
- **Metric:** Boolean: HTTPS enabled, HTTP redirects to HTTPS
- **Implementation:** SSL/TLS certificate, HTTPS redirect configuration

**NFR-008: SQL Injection Prevention**
- **Requirement:** All database queries shall use parameterized queries to prevent SQL injection
- **Priority:** Must Have
- **Metric:** Code review, security audit
- **Implementation:** JPA/Hibernate parameterized queries, no string concatenation for SQL

**NFR-009: XSS Protection**
- **Requirement:** All user input shall be sanitized and escaped to prevent cross-site scripting attacks
- **Priority:** Must Have
- **Metric:** Security testing, penetration testing
- **Implementation:** React's built-in XSS protection, input sanitization, Content Security Policy headers

**NFR-010: CSRF Protection**
- **Requirement:** All state-changing operations shall include CSRF token validation
- **Priority:** Should Have (less critical without authentication in V1)
- **Metric:** Security testing
- **Implementation:** CSRF tokens in forms, SameSite cookie attribute

**NFR-011: Input Validation**
- **Requirement:** All user input shall be validated on both client and server side
- **Priority:** Must Have
- **Metric:** Test coverage, validation error handling
- **Implementation:** React Hook Form + Zod validation (client), Spring Validation annotations (server)

**NFR-012: File Upload Security**
- **Requirement:** File uploads shall validate file type, size, and scan for malicious content
- **Priority:** Must Have
- **Metric:** Upload validation tests
- **Implementation:** MIME type validation, file size limits, virus scanning (optional)

**NFR-013: Rate Limiting**
- **Requirement:** API endpoints shall implement rate limiting to prevent abuse
- **Priority:** Could Have
- **Metric:** Rate limit configuration and testing
- **Implementation:** Spring rate limiter, bucket4j, or reverse proxy rate limiting

**NFR-014: Secure Headers**
- **Requirement:** HTTP response shall include security headers (X-Frame-Options, X-Content-Type-Options, etc.)
- **Priority:** Should Have
- **Metric:** Security header scanner
- **Implementation:** Spring Security configuration

### 4.3 Scalability

**NFR-015: Horizontal Scaling**
- **Requirement:** Backend shall be stateless to support horizontal scaling
- **Priority:** Could Have
- **Metric:** Load testing with multiple instances
- **Implementation:** Stateless REST API, no server-side sessions

**NFR-016: Database Connection Pooling**
- **Requirement:** Database connections shall use pooling to optimize resource usage
- **Priority:** Must Have
- **Metric:** Connection pool metrics (active, idle, wait time)
- **Implementation:** HikariCP (default in Spring Boot)

**NFR-017: Caching Strategy**
- **Requirement:** Frequently accessed data shall be cached to reduce database load
- **Priority:** Could Have
- **Metric:** Cache hit ratio
- **Implementation:** Spring Cache with in-memory cache (Caffeine)

**NFR-018: Collection Size Support**
- **Requirement:** System shall perform acceptably with collections up to 10,000 books
- **Priority:** Should Have
- **Metric:** Performance testing with 10K records
- **Implementation:** Pagination, indexes, query optimization

### 4.4 Reliability

**NFR-019: Uptime Target**
- **Requirement:** System shall maintain 99.5% uptime during business hours
- **Priority:** Should Have
- **Metric:** Uptime monitoring, incident logs
- **Implementation:** Health checks, automated restarts, monitoring alerts

**NFR-020: Error Handling**
- **Requirement:** All errors shall be caught, logged, and displayed to user with helpful messages
- **Priority:** Must Have
- **Metric:** Error tracking coverage, user feedback
- **Implementation:** Global exception handler, error boundaries, toast notifications

**NFR-021: Logging**
- **Requirement:** All API requests, errors, and important operations shall be logged
- **Priority:** Must Have
- **Metric:** Log coverage, log analysis
- **Implementation:** SLF4J with Logback, structured JSON logging

**NFR-022: Graceful Degradation**
- **Requirement:** System shall handle backend failures gracefully with user-friendly error messages
- **Priority:** Should Have
- **Metric:** Error handling tests
- **Implementation:** Error boundaries, fallback UI, retry logic

**NFR-023: Data Integrity**
- **Requirement:** Zero data loss or corruption under normal operations
- **Priority:** Must Have
- **Metric:** Data validation tests, consistency checks
- **Implementation:** Database constraints, transaction management, validation

### 4.5 Usability

**NFR-024: Intuitive Navigation**
- **Requirement:** Users shall complete core tasks (add, search, edit book) without training or documentation
- **Priority:** Must Have
- **Metric:** Usability testing, task completion rate
- **Implementation:** Clear labels, breadcrumbs, consistent navigation patterns

**NFR-025: Responsive Design**
- **Requirement:** Application shall be fully functional and visually appropriate on mobile, tablet, and desktop devices
- **Priority:** Should Have
- **Metric:** Visual verification across devices, responsive testing tools
- **Implementation:** Tailwind CSS responsive utilities, mobile-first design

**NFR-026: Accessibility**
- **Requirement:** Application shall meet WCAG 2.1 Level AA accessibility standards
- **Priority:** Should Have
- **Metric:** Automated accessibility testing (axe, Lighthouse), manual testing
- **Implementation:** Semantic HTML, ARIA labels, keyboard navigation, color contrast

**NFR-027: Browser Support**
- **Requirement:** Application shall work correctly on latest 2 versions of Chrome, Firefox, Safari, and Edge
- **Priority:** Must Have
- **Metric:** Cross-browser testing
- **Implementation:** Modern JavaScript (ES6+), polyfills if needed, browser testing

**NFR-028: User Feedback**
- **Requirement:** All user actions shall provide immediate visual feedback (loading states, success/error messages)
- **Priority:** Must Have
- **Metric:** User testing, feedback coverage
- **Implementation:** Toast notifications, loading spinners, button disabled states

**NFR-029: Form Usability**
- **Requirement:** Forms shall provide inline validation, clear error messages, and field-level help text
- **Priority:** Must Have
- **Metric:** Usability testing, user satisfaction
- **Implementation:** React Hook Form validation, error messages below fields, placeholder text

### 4.6 Maintainability

**NFR-030: Clean Code Practices**
- **Requirement:** Code shall follow industry best practices and style guides
- **Priority:** Must Have
- **Metric:** Code review, linting
- **Implementation:** ESLint, Prettier, Checkstyle, code review process

**NFR-031: Comprehensive Documentation**
- **Requirement:** All APIs, components, and setup procedures shall be documented
- **Priority:** Should Have
- **Metric:** Documentation coverage
- **Implementation:** JSDoc, Javadoc, OpenAPI spec, README files

**NFR-032: Unit Test Coverage**
- **Requirement:** Unit test coverage shall be > 80% for backend and > 70% for frontend
- **Priority:** Should Have
- **Metric:** JaCoCo coverage report (backend), Vitest coverage (frontend)
- **Implementation:** JUnit 5 + Mockito (backend), Vitest + React Testing Library (frontend)

**NFR-033: Integration Test Coverage**
- **Requirement:** All critical user flows shall have integration tests
- **Priority:** Should Have
- **Metric:** Critical path coverage
- **Implementation:** MockMvc (backend), Playwright/Cypress (E2E)

**NFR-034: Logging and Monitoring**
- **Requirement:** Application shall log sufficient information for debugging and monitoring
- **Priority:** Must Have
- **Metric:** Log coverage, incident resolution time
- **Implementation:** SLF4J, structured logs, log levels (ERROR, WARN, INFO, DEBUG)

---

## 5. Technical Requirements

### 5.1 Frontend

**Tech Stack:**
- **Framework:** React 18.2+
- **Build Tool:** Vite 4.x
- **Language:** JavaScript (ES6+) or TypeScript (optional for V1, recommended for V2)
- **Routing:** React Router v6
- **State Management:** Context API (for simple global state) or Zustand (for complex state)
- **Forms:** React Hook Form 7.x + Zod validation
- **HTTP Client:** Axios 1.x
- **Styling:** Tailwind CSS 3.x
- **Testing:** Vitest + React Testing Library
- **Linting:** ESLint + Prettier

**Project Structure:**
```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── components/
│   │   ├── common/         # Button, Input, Dropdown, etc.
│   │   ├── layout/         # Header, Footer
│   │   └── features/       # BooksTable, BookForm, etc.
│   ├── pages/
│   │   ├── BooksListPage.jsx
│   │   ├── AddBookPage.jsx
│   │   ├── EditBookPage.jsx
│   │   ├── SearchBooksPage.jsx
│   │   └── BookDetailsPage.jsx
│   ├── services/
│   │   └── api/
│   │       └── booksApi.js
│   ├── hooks/
│   │   └── useBooks.js
│   ├── context/
│   │   └── AppContext.jsx
│   ├── utils/
│   │   ├── validation.js
│   │   └── formatters.js
│   ├── styles/
│   │   └── tailwind.css
│   ├── types/              # TypeScript types (if using TS)
│   ├── App.jsx
│   ├── main.jsx
│   └── router.jsx
├── package.json
├── vite.config.js
├── tailwind.config.js
└── README.md
```

**Key Dependencies:**
- react, react-dom: ^18.2.0
- react-router-dom: ^6.10.0
- axios: ^1.4.0
- react-hook-form: ^7.43.0
- zod: ^3.21.0
- tailwindcss: ^3.3.0
- vitest: ^0.31.0
- @testing-library/react: ^14.0.0

### 5.2 Backend

**Tech Stack:**
- **Language:** Java 17
- **Framework:** Spring Boot 3.2.x
- **Database:** H2 (in-memory for development)
- **ORM:** Spring Data JPA (Hibernate)
- **API:** RESTful with JSON
- **Documentation:** Springdoc OpenAPI 3 (Swagger UI)
- **Build Tool:** Maven 3.8+ (or Gradle 8+)
- **Testing:** JUnit 5, Mockito, MockMvc

**Project Structure:**
```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/booksonline/
│   │   │   ├── Application.java
│   │   │   ├── config/
│   │   │   │   ├── WebConfig.java
│   │   │   │   └── OpenApiConfig.java
│   │   │   ├── controller/
│   │   │   │   └── BookController.java
│   │   │   ├── service/
│   │   │   │   ├── BookService.java
│   │   │   │   └── BookServiceImpl.java
│   │   │   ├── repository/
│   │   │   │   └── BookRepository.java
│   │   │   ├── model/
│   │   │   │   ├── entity/
│   │   │   │   │   └── Book.java
│   │   │   │   └── dto/
│   │   │   │       ├── BookRequestDto.java
│   │   │   │       └── BookResponseDto.java
│   │   │   ├── exception/
│   │   │   │   ├── BookNotFoundException.java
│   │   │   │   ├── DuplicateIsbnException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── mapper/
│   │   │   │   └── BookMapper.java
│   │   │   └── util/
│   │   │       └── IsbnValidator.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── schema.sql
│   └── test/
│       └── java/com/booksonline/
│           ├── controller/
│           │   └── BookControllerTest.java
│           ├── service/
│           │   └── BookServiceTest.java
│           └── repository/
│               └── BookRepositoryTest.java
├── pom.xml
└── README.md
```

**Key Dependencies:**
- spring-boot-starter-web: 3.2.x
- spring-boot-starter-data-jpa: 3.2.x
- spring-boot-starter-validation: 3.2.x
- h2: 2.2.x
- springdoc-openapi-starter-webmvc-ui: 2.2.x (for OpenAPI 3)
- lombok: 1.18.x (optional, for reducing boilerplate)
- junit-jupiter: 5.9.x
- mockito-core: 5.3.x

### 5.3 Database

**Development Database:**
- **Type:** H2 in-memory database
- **Mode:** Embedded, file-based (optional persistence)
- **Configuration:**
  - URL: jdbc:h2:mem:booksdb
  - Username: sa
  - Password: (empty)
  - H2 Console: Enabled at /h2-console for debugging

**Production Considerations (Future):**
- Migrate to PostgreSQL 14+ or MySQL 8+
- Connection pooling: HikariCP (default in Spring Boot)
- Schema versioning: Flyway or Liquibase

**Database Schema:**
```sql
CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    author VARCHAR(100) NOT NULL,
    publisher VARCHAR(100),
    genre VARCHAR(50) NOT NULL,
    publication_year INT NOT NULL CHECK (publication_year >= 1000 AND publication_year <= YEAR(CURDATE())),
    pages INT CHECK (pages > 0),
    language VARCHAR(30) NOT NULL,
    price DECIMAL(10, 2) CHECK (price >= 0),
    stock_quantity INT DEFAULT 0 CHECK (stock_quantity >= 0),
    description TEXT,
    cover_image_url VARCHAR(500),
    rating DECIMAL(2, 1) CHECK (rating >= 0 AND rating <= 5.0),
    review_count INT DEFAULT 0,
    view_count INT DEFAULT 0,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_title ON books(title);
CREATE INDEX idx_author ON books(author);
CREATE INDEX idx_genre ON books(genre);
CREATE INDEX idx_year ON books(publication_year);
CREATE INDEX idx_available ON books(is_available);
CREATE INDEX idx_created ON books(created_at);
CREATE INDEX idx_genre_year ON books(genre, publication_year);
CREATE INDEX idx_author_title ON books(author, title);
```

### 5.4 Deployment

**Containerization:**
- **Backend:** Dockerfile with OpenJDK 17 base image
- **Frontend:** Dockerfile with Node 18 (build) + Nginx (serve)
- **Orchestration:** docker-compose for local development

**CI/CD:**
- **Platform:** GitHub Actions
- **Pipeline:**
  1. Lint and test on pull request
  2. Build Docker images on merge to main
  3. Deploy to staging/production (manual approval)
  4. Run smoke tests post-deployment

**Environment Configuration:**
- **Development:** H2 database, hot reload, debug logs
- **Production:** External database, optimized builds, error logs only

---

## 6. API Requirements

### 6.1 API Design Principles

- **RESTful Design:** Resources identified by URLs, HTTP methods for actions
- **JSON Format:** All request/response bodies use JSON
- **Consistent Error Responses:** Standard error format with status code, message, timestamp
- **Versioning:** /api/v1/... prefix for all endpoints
- **Pagination:** List endpoints support page and size query parameters
- **Filtering and Sorting:** List endpoints support filtering and sorting query parameters
- **HATEOAS:** Optional for V1, recommended for V2

**Standard Error Response Format:**
```json
{
  "timestamp": "2024-02-11T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "ISBN is required and must be a valid format",
  "path": "/api/v1/books"
}
```

### 6.2 Endpoint Categories

#### Books Endpoints

**GET /api/v1/books**
- **Description:** List all books with pagination and optional filters
- **Query Parameters:**
  - page: int (default 0)
  - size: int (default 4)
  - genre: string (optional)
  - author: string (optional)
  - year: int (optional)
  - search: string (optional, quick search)
- **Response:** 200 OK
```json
{
  "content": [...],
  "page": 0,
  "size": 4,
  "totalElements": 24,
  "totalPages": 6
}
```

**GET /api/v1/books/{id}**
- **Description:** Get single book by ID
- **Path Parameters:** id (long)
- **Response:** 200 OK with book object
- **Errors:** 404 Not Found if book doesn't exist

**POST /api/v1/books**
- **Description:** Create new book
- **Request Body:** BookRequestDto (JSON)
- **Response:** 201 Created with created book object, Location header
- **Errors:** 400 Bad Request (validation errors), 409 Conflict (duplicate ISBN)

**PUT /api/v1/books/{id}**
- **Description:** Update existing book
- **Path Parameters:** id (long)
- **Request Body:** BookRequestDto (JSON, ISBN excluded)
- **Response:** 200 OK with updated book object
- **Errors:** 404 Not Found, 400 Bad Request

**DELETE /api/v1/books/{id}**
- **Description:** Delete book permanently
- **Path Parameters:** id (long)
- **Response:** 204 No Content
- **Errors:** 404 Not Found

**GET /api/v1/books/search**
- **Description:** Advanced search with multiple criteria
- **Query Parameters:**
  - title, author, isbn, publisher: string (optional)
  - genre, language, availability: string (optional)
  - yearFrom, yearTo: int (optional)
  - minPrice, maxPrice: decimal (optional)
  - inStockOnly, onSale, newReleases: boolean (optional)
  - sortBy: string (optional)
  - page, size: int (optional)
- **Response:** 200 OK with paginated results

**POST /api/v1/books/{id}/duplicate**
- **Description:** Duplicate book (create copy)
- **Path Parameters:** id (long)
- **Response:** 201 Created with new book ID
- **Errors:** 404 Not Found

**GET /api/v1/books/{id}/export**
- **Description:** Export book data
- **Path Parameters:** id (long)
- **Query Parameters:** format (json, csv, pdf)
- **Response:** 200 OK with file download
- **Errors:** 404 Not Found

---

## 7. Data Requirements

### 7.1 Data Models

#### Entity: Book

**Fields:**

| Field Name | Data Type | Required | Validation | Notes |
|------------|-----------|----------|------------|-------|
| id | Long | Yes | Auto-generated | Primary key |
| title | String(200) | Yes | 1-200 characters | Book title |
| isbn | String(20) | Yes | Valid ISBN-10/13, unique | International Standard Book Number |
| author | String(100) | Yes | 1-100 characters | Primary author name |
| publisher | String(100) | No | Max 100 characters | Publishing company |
| genre | String(50) | Yes | Enum: Fiction, Non-Fiction, Dystopian, Romance, Mystery, Science, Biography, Fantasy | Book category |
| publicationYear | Integer | Yes | 1000 to current year | Year published |
| pages | Integer | No | > 0 | Number of pages |
| language | String(30) | Yes | Enum: English, Spanish, French, German, Italian, Portuguese, Chinese, Japanese, Korean, Arabic | Book language |
| price | Decimal(10,2) | No | >= 0 | Book price |
| stockQuantity | Integer | No | >= 0, default 0 | Available copies |
| description | Text | No | Max 5000 characters | Book description/summary |
| coverImageUrl | String(500) | No | Valid URL/path | Path to cover image |
| rating | Decimal(2,1) | No | 0.0 to 5.0 | Average rating (V1: display only) |
| reviewCount | Integer | No | Default 0 | Number of reviews |
| viewCount | Integer | No | Default 0 | Number of detail views |
| isAvailable | Boolean | Yes | Computed from stockQuantity | Availability status |
| createdAt | Timestamp | Yes | Auto-generated | Record creation time |
| updatedAt | Timestamp | Yes | Auto-updated | Last modification time |

**Relationships:**
- None in V1 (single-entity system)
- Future: Book ↔ Author (Many-to-Many), Book ↔ Genre (Many-to-One), Book ↔ Review (One-to-Many)

---

## 8. User Interface Requirements

### 8.1 Screen Inventory

**1. Books List View (books-list)**
- **Purpose:** Main landing page for browsing all books
- **Components:** Header, search bar, filter dropdowns (genre, author, year), books table (6 columns), pagination, footer
- **Actions:** Navigate to Add Book, Edit book, Delete book, View book details, Apply filters, Quick search, Navigate pages

**2. Add Book Form (add-book-form)**
- **Purpose:** Create new book record
- **Components:** Back button, cover upload area, form fields (12 inputs/dropdowns/textarea), Cancel button, Add Book button
- **Actions:** Upload cover, Fill fields, Validate, Submit (create book), Cancel (return to list)

**3. Search Books (search-books)**
- **Purpose:** Advanced multi-criteria search
- **Components:** Quick search bar, advanced search form (12 fields + 3 checkboxes), sort dropdown, Clear Filters button, Search button, search tips banner
- **Actions:** Quick search, Fill advanced criteria, Sort, Clear filters, Execute search

**4. Edit Book Form (update-book-form)**
- **Purpose:** Update existing book record
- **Components:** Book ID/metadata banner, cover change button, pre-filled form fields (ISBN read-only), Cancel button, Update Book button
- **Actions:** Modify fields, Change cover, Validate, Submit (update book), Cancel (return to details)

**5. Book Details (book-details)**
- **Purpose:** View comprehensive book information
- **Components:** Large cover image, availability badge, title, author, rating, price box, details grid, description, metadata bar, action buttons (Edit, Duplicate, Export, Delete)
- **Actions:** Navigate to edit, Duplicate book, Export data, Delete book (with confirmation modal), Back to list

**Global Components:**
- **Header:** Logo, search bar (on list view), action button
- **Footer:** Copyright text
- **Delete Modal:** Overlay modal with confirmation (on book details)

### 8.2 Navigation

**Primary Navigation:**
- Header with logo (always links to Books List)
- Contextual action buttons in header (+ Add Book, ← Back)

**Navigation Flows:**
- Books List → Add Book Form → Books List (after save)
- Books List → Book Details → Edit Form → Book Details (after save)
- Books List → Book Details → Delete Modal → Books List (after delete)
- Books List → Search Books → Books List (with filtered results)
- Any screen → Books List (via Back button or logo click)

**Breadcrumbs:** Not used in V1 (flat navigation structure)

**Back Navigation:**
- All forms and detail views include ← Back button in top-right
- Back button returns to previous screen (Books List or Book Details)
- Browser back button supported via React Router

### 8.3 Responsive Design

**Mobile (<768px):**
- Header: Logo + hamburger menu (action buttons in menu)
- Filter dropdowns: Stack vertically
- Books table: Convert to card layout (cover, title, author, action buttons)
- Forms: Single column layout
- Book details: Stack cover and info vertically

**Tablet (768px - 1024px):**
- Header: Logo + search + action button (shrink search bar width)
- Filter dropdowns: 3 columns
- Books table: Scrollable horizontally or reduce columns
- Forms: 2-column layout maintained
- Book details: 2-column layout maintained

**Desktop (>1024px):**
- As shown in wireframes
- Full 6-column table
- 2-column forms with large cover upload area
- Book details with large cover and full details grid

---

## 9. Integration Requirements

### 9.1 Third-Party Integrations

**V1:** None

**Future Considerations:**
- **Google Books API:** Auto-populate book metadata from ISBN lookup
- **OpenLibrary API:** Alternative book data source
- **Cloudinary / S3:** Cloud storage for book cover images
- **Stripe:** Payment processing for bookstore features
- **SendGrid / AWS SES:** Email notifications

### 9.2 APIs

**V1:** Internal REST APIs only (Books API described in Section 6)

**Future:**
- Public API for third-party integrations (requires authentication)
- Webhook support for inventory changes
- GraphQL API (alternative to REST)

---

## 10. Compliance and Legal

### 10.1 Data Privacy

**GDPR Compliance (if applicable):**
- V1 stores no personal user data (no user accounts)
- Book data is not personally identifiable
- If future versions add user accounts: Implement consent, right to access, right to deletion

**User Data Handling:**
- Book cover images stored securely with restricted access
- No user tracking or analytics in V1
- No cookies used (except session if authentication added in V2)

**Privacy Policy:**
- Not required for V1 (no user data collected)
- Required if authentication/user accounts added in V2

### 10.2 Terms of Service

**V1:** Not applicable (internal tool or single-organization use)

**Future:** If published as SaaS:
- User agreement for terms of use
- Acceptable use policy
- Data retention policy
- Service level agreement (SLA)

---

## 11. Assumptions and Constraints

### 11.1 Assumptions

1. **User Environment:** Users have modern web browsers (Chrome, Firefox, Safari, Edge) updated to latest or previous version
2. **Internet Connectivity:** Users have stable internet connection (broadband or mobile 4G+)
3. **Screen Size:** Primary usage on desktop/laptop (1920x1080 or 1366x768), with secondary support for tablet/mobile
4. **Network:** Application deployed on internal network or public internet with HTTPS
5. **Single Organization:** System used by single library, bookstore, or organization (not multi-tenant)
6. **English Language:** UI text in English only for V1
7. **Currency:** Prices in USD ($) for V1
8. **Time Zone:** All timestamps in UTC or server local time
9. **Book Covers:** Cover images provided by user (no auto-fetch in V1)
10. **Data Volume:** Collection size up to 10,000 books for V1 performance targets

### 11.2 Constraints

1. **H2 Database:** Limited to in-memory or file-based H2, not suitable for production scale or multi-instance deployment
2. **Single Deployment:** V1 supports single application instance (no horizontal scaling)
3. **No Authentication:** Open access, no user login or access control in V1
4. **No Multi-User:** No concurrent edit protection, last write wins
5. **Image Storage:** Images stored on local filesystem or application server, not CDN
6. **Budget:** Open-source technologies only, no paid services or licenses
7. **Timeline:** 8-week development timeline for V1 (see Section 13)
8. **Team Size:** Small team (1-2 developers assumed)
9. **Browser Support:** Modern browsers only, no IE11 support
10. **Mobile App:** No native mobile app, web-only

---

## 12. Risks and Mitigation

| Risk | Impact | Probability | Mitigation Strategy | Owner |
|------|--------|-------------|---------------------|-------|
| H2 database limitations (data loss, single-instance) | High | Medium | Document migration path to PostgreSQL; implement export/import functionality; schedule periodic exports | Backend Dev |
| ISBN validation complexity and edge cases | Medium | Medium | Use established ISBN validation library; test with wide variety of ISBNs; provide manual override for edge cases | Backend Dev |
| Performance degradation with large collections (>10K books) | High | Low | Implement pagination, indexing, query optimization from start; load test with 10K+ records; optimize queries | Backend Dev |
| Security vulnerabilities (XSS, SQL injection) | High | Low | Follow OWASP best practices; use parameterized queries; sanitize all inputs; conduct security review | Full Stack |
| Image upload abuse (large files, malicious files) | Medium | Medium | Implement strict file size limits (5MB); validate MIME types; consider virus scanning; rate limit uploads | Backend Dev |
| Browser compatibility issues | Medium | Low | Test on all supported browsers; use polyfills if needed; avoid bleeding-edge features | Frontend Dev |
| User confusion with complex search interface | Low | Medium | Provide search tips and help text; consider tooltips; conduct usability testing; simplify if needed | Frontend Dev |
| Scope creep (adding authentication, reviews, etc.) | Medium | High | Strictly adhere to V1 scope; defer features to V2; document out-of-scope features clearly | PM/Tech Lead |
| Accidental deletion of books | Low | Medium | Implement confirmation modal with book details; consider implementing soft delete; provide data export | Full Stack |
| Data loss during H2 file corruption | Medium | Low | Implement automated backups; provide CSV export functionality; document recovery procedures | Backend Dev |
| Slow ISBN uniqueness checks on large datasets | Low | Low | Use database unique constraint; add index on ISBN field; optimize query | Backend Dev |

---

## 13. Timeline and Milestones

### Phase 1: Foundation and Core CRUD (Weeks 1-3)

**Week 1: Project Setup and Database**
- Set up frontend (React + Vite) and backend (Spring Boot) projects
- Configure H2 database and create Book entity
- Design and implement database schema with indexes
- Set up Docker and docker-compose for local development
- Deliverable: Running skeleton applications, database schema

**Week 2: Backend API Development**
- Implement BookRepository with Spring Data JPA
- Implement BookService with CRUD operations
- Implement BookController with REST endpoints
- Add validation annotations and global exception handler
- Write unit tests for service and repository layers
- Deliverable: Complete Books API (Create, Read, Update, Delete)

**Week 3: Frontend Core Components**
- Build reusable components: Button, Input, Dropdown, Table, Pagination
- Implement Books List View with table and pagination
- Implement Add Book Form with validation
- Implement Edit Book Form
- Implement API client with Axios
- Deliverable: Working CRUD operations on frontend

### Phase 2: Search, Filters, and Details (Weeks 4-5)

**Week 4: Search and Filter Functionality**
- Implement quick search in header
- Implement genre, author, year filter dropdowns on Books List
- Implement Advanced Search screen with all criteria
- Update backend to support advanced search query parameters
- Add sorting functionality
- Deliverable: Complete search and filter features

**Week 5: Book Details and Actions**
- Implement Book Details screen layout
- Implement delete confirmation modal
- Implement book duplication feature
- Implement export data functionality (JSON, CSV)
- Add view count tracking
- Deliverable: Complete Book Details with all actions

### Phase 3: Polish and Testing (Weeks 6-7)

**Week 6: UI/UX Refinement**
- Implement responsive design for mobile and tablet
- Add loading states, skeletons, and spinners
- Add success/error toast notifications
- Implement empty states with helpful messages
- Add form validation error messages
- Improve accessibility (ARIA labels, keyboard navigation)
- Deliverable: Polished, responsive UI

**Week 7: Comprehensive Testing**
- Write frontend component tests (Vitest + RTL)
- Write backend integration tests (MockMvc)
- Write end-to-end tests for critical flows
- Conduct cross-browser testing
- Conduct usability testing with target users
- Fix identified bugs and issues
- Deliverable: Test coverage >80% backend, >70% frontend

### Phase 4: Deployment and Launch (Week 8)

**Week 8: Deployment and Documentation**
- Set up CI/CD pipeline with GitHub Actions
- Create production Dockerfiles
- Write deployment documentation
- Write user guide and API documentation (OpenAPI/Swagger)
- Conduct final security review
- Deploy to staging environment
- Conduct smoke tests
- Deploy to production
- Deliverable: Deployed, documented application

**Milestone Summary:**
- **Week 3:** Core CRUD complete
- **Week 5:** All features complete
- **Week 7:** Testing complete, production-ready
- **Week 8:** Deployed to production

---

## 14. Success Criteria

### Launch Criteria

- [ ] All functional requirements (FR-001 to FR-061) implemented
- [ ] All Must Have and Should Have non-functional requirements met
- [ ] Security requirements (HTTPS, input validation, SQL injection prevention, XSS protection) implemented
- [ ] Performance benchmarks achieved (page load <2s, API response <500ms)
- [ ] Unit test coverage >80% backend, >70% frontend
- [ ] Integration test coverage for all critical user flows (add, edit, delete, search book)
- [ ] Cross-browser testing passed (Chrome, Firefox, Safari, Edge latest 2 versions)
- [ ] Responsive design verified on mobile, tablet, desktop
- [ ] API documentation complete (OpenAPI/Swagger)
- [ ] User guide and deployment documentation complete
- [ ] Deployment pipeline functional and tested
- [ ] Staging environment smoke tests passed
- [ ] Zero critical or high-severity bugs open
- [ ] Usability testing completed with target users

### Post-Launch Metrics (30 Days)

**User Adoption:**
- 100% of library staff trained and using system
- 90% of new book additions done through Books Online (vs. old system)
- User satisfaction score >4.0/5.0

**Engagement:**
- Daily active users: 80% of staff
- Average session duration: 15-30 minutes
- All primary features (add, search, edit) used by >75% of users

**Performance:**
- Page load time <2 seconds (95th percentile)
- API response time <500ms (95th percentile)
- Search results returned <1 second
- Zero data loss incidents
- System uptime >99.5%

**Business:**
- Cataloging time reduced by 40%
- Search/retrieval time reduced by 60%
- Data accuracy >99%
- Zero security incidents

**Error Rate:**
- Application error rate <0.1%
- User-reported bugs <5 per week
- Critical bugs: 0

---

## 15. Appendices

### Appendix A: Wireframe References

**Source Files:**
- 01_books_list_view.svg
- 02_add_book_form.svg
- 03_search_books.svg
- 04_update_book_form.svg
- 05_book_details_delete.svg

**Analysis Files:**
- artifacts/01-wireframe-analysis/wireframe-analysis.json
- artifacts/01-wireframe-analysis/component-hierarchy.md
- artifacts/01-wireframe-analysis/user-flows.md
- artifacts/01-wireframe-analysis/data-requirements.md

### Appendix B: Glossary

- **Book:** A physical or digital publication with title, author, ISBN, and other metadata
- **ISBN:** International Standard Book Number, unique identifier for books (ISBN-10 or ISBN-13)
- **CRUD:** Create, Read, Update, Delete - basic database operations
- **SPA:** Single-Page Application - web app that loads single HTML page and dynamically updates content
- **JWT:** JSON Web Token - standard for secure authentication (future V2 feature)
- **ORM:** Object-Relational Mapping - technique for converting database tables to objects (e.g., JPA/Hibernate)
- **DTO:** Data Transfer Object - object used to transfer data between layers
- **H2:** Lightweight Java SQL database engine, in-memory or file-based
- **OpenAPI:** Standard specification for describing REST APIs (formerly Swagger)
- **Pagination:** Dividing large datasets into pages for efficient browsing
- **Seed Data:** Initial sample data for development and testing
- **Soft Delete:** Marking records as deleted without physically removing from database
- **Hard Delete:** Permanently removing records from database

### Appendix C: References

**React:**
- React Documentation: https://react.dev/
- React Router v6: https://reactrouter.com/
- React Hook Form: https://react-hook-form.com/
- Zod Validation: https://zod.dev/

**Spring Boot:**
- Spring Boot Documentation: https://docs.spring.io/spring-boot/docs/current/reference/html/
- Spring Data JPA: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
- Spring Web MVC: https://docs.spring.io/spring-framework/docs/current/reference/html/web.html

**Best Practices:**
- OWASP Top 10: https://owasp.org/www-project-top-ten/
- REST API Design Best Practices: https://restfulapi.net/
- Conventional Commits: https://www.conventionalcommits.org/
- WCAG 2.1 Accessibility Guidelines: https://www.w3.org/WAI/WCAG21/quickref/

**Testing:**
- Vitest: https://vitest.dev/
- React Testing Library: https://testing-library.com/react
- JUnit 5: https://junit.org/junit5/docs/current/user-guide/
- Mockito: https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html

---

## Document Approval

| Role | Name | Date | Signature |
|------|------|------|--------------|
| Product Owner | TBD | | |
| Tech Lead | TBD | | |
| QA Lead | TBD | | |
| Stakeholder | TBD | | |

---

## Revision History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | 2024-02-11 | AI Generated | Initial PRD based on wireframe analysis (5 screens, 1 entity, 61 functional requirements, 34 non-functional requirements) |

---

**END OF PRODUCT REQUIREMENTS DOCUMENT**
