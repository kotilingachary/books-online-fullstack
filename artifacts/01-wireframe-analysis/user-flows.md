# User Flows

## Project: Books Online - Book Management System
**Analysis Date:** 2024-02-10

---

## Primary User Flows

### Flow 1: Add New Book
**Goal:** Library administrator adds a new book to the system

**Steps:**
1. User lands on **Books List View**
2. User clicks **"+ Add Book"** button in header
3. System navigates to **Add Book Form**
4. User uploads book cover image (drag-drop or click)
5. User fills in required fields:
   - Title *
   - ISBN *
   - Author *
   - Genre * (dropdown)
   - Publication Year *
   - Language * (dropdown)
6. User fills in optional fields:
   - Publisher
   - Pages
   - Price
   - Stock Quantity
   - Description
7. User clicks **"Add Book"** button
8. System validates input
9. System saves book to database
10. System navigates back to **Books List View**
11. **Result:** New book appears in the table

**Alternative Paths:**
- **Cancel:** User clicks "Cancel" or "← Back" → Returns to Books List View without saving
- **Validation Error:** System shows error messages, user corrects and resubmits

---

### Flow 2: View Book Details and Delete
**Goal:** View detailed information about a book and optionally delete it

**Steps:**
1. User is on **Books List View**
2. User clicks on a book row or "View" action
3. System navigates to **Book Details** screen
4. User views comprehensive book information:
   - Large cover image with availability badge
   - Title, author, rating, reviews
   - Price with discount information
   - Full details (ISBN, publisher, year, language, pages, genre)
   - Description
   - Metadata (book ID, dates, view count)
5. User decides to delete the book
6. User clicks **"Delete Book"** button (red)
7. System displays **Delete Confirmation Modal** overlay
8. Modal shows:
   - Warning: "⚠ Confirm Delete"
   - Book details (title, ISBN) in highlighted box
   - Warning: "This action cannot be undone"
9. User has two options:
   - **Cancel:** Closes modal, returns to Book Details
   - **Delete Permanently:** Confirms deletion
10. If confirmed, system deletes book from database
11. System navigates back to **Books List View**
12. **Result:** Book is removed from the list

**Alternative Paths:**
- **Edit Book:** User clicks "Edit Book" → Navigates to Update Book Form
- **Duplicate:** User clicks "Duplicate" → Creates copy of book
- **Export Data:** User clicks "Export Data" → Downloads book data as file
- **Back:** User clicks "← Back" → Returns to Books List View

---

### Flow 3: Edit/Update Existing Book
**Goal:** Update information for an existing book

**Steps:**
1. User is on **Books List View**
2. User clicks **"Edit"** button (blue) on a book row
3. System navigates to **Update Book Form**
4. Form displays with pre-filled data:
   - Book ID and last updated date shown at top
   - Existing cover image displayed
   - All fields populated with current values
   - ISBN field is read-only (🔒 icon)
5. User modifies desired fields
6. User can click "Change Image" to update cover
7. User clicks **"Update Book"** button (orange)
8. System validates changes
9. System updates book in database
10. System shows success message
11. System navigates back to **Book Details** or **Books List View**
12. **Result:** Book information is updated

**Alternative Paths:**
- **Cancel:** User clicks "Cancel" or "← Back" → Returns without saving changes
- **Validation Error:** System shows errors, user corrects and resubmits
- **No Changes:** User submits without changes → System shows "No changes detected" message

---

### Flow 4: Search Books (Quick Search)
**Goal:** Quickly find books using simple search

**Steps:**
1. User is on **Books List View**
2. User types query in **header search bar** ("Search books...")
3. System performs real-time filtering as user types
4. System displays filtered results in the books table
5. User sees matching books based on:
   - Title
   - Author
   - ISBN
   - Any keyword match
6. **Result:** Filtered list of books

**Alternative Paths:**
- **No Results:** System shows "No books found" message
- **Clear Search:** User clears search box → Full list returns

---

### Flow 5: Advanced Search
**Goal:** Find books using multiple search criteria

**Steps:**
1. User is on **Books List View** or clicks "Advanced Search" link
2. System navigates to **Search Books** screen
3. User can use **Quick Search** (single large search box at top)
   - OR -
4. User expands **Advanced Search** section
5. User fills in desired search criteria:
   - **Basic:** Title, Author, ISBN, Publisher
   - **Filters:** Genre (dropdown), Language (dropdown), Availability (dropdown)
   - **Ranges:** Publication year (From/To), Price (Min/Max)
   - **Sort:** Sort Results By dropdown
   - **Checkboxes:** In Stock Only, On Sale, New Releases
6. User clicks **"Search"** button (green)
7. System queries database with all criteria
8. System displays results in **Books List View**
9. **Result:** Precise search results matching all criteria

**Alternative Paths:**
- **Clear Filters:** User clicks "Clear Filters" → All fields reset to empty
- **Partial Criteria:** User fills some fields, leaves others empty → Search works with provided criteria only
- **Back:** User clicks "← Back" → Returns to Books List View
- **Search Tip:** Info banner provides tips about exact phrase matching

---

### Flow 6: Browse and Filter Books
**Goal:** Browse books with simple filters

**Steps:**
1. User is on **Books List View**
2. User sees **Filter by:** section with three dropdowns
3. User selects filter options:
   - **Genre:** Fiction, Non-Fiction, Dystopian, Romance, etc.
   - **Author:** Filter by specific author
   - **Year:** Filter by publication year
4. System applies filters immediately
5. Books table updates to show only matching books
6. Pagination updates to reflect filtered count
7. User can browse through filtered results using pagination
8. **Result:** Filtered view of books

**Alternative Paths:**
- **Multiple Filters:** User applies multiple filters → Combined filtering (AND logic)
- **Clear Filters:** User deselects all filters → Full list returns
- **No Results:** System shows "No books match your filters" message

---

### Flow 7: Paginate Through Books
**Goal:** Navigate through multiple pages of books

**Steps:**
1. User is on **Books List View** with multiple pages
2. User sees pagination controls at bottom:
   - "Showing 1-4 of 24 books"
   - Previous button (◀)
   - Page numbers (1, 2, ...)
   - Next button (▶)
3. User clicks page number or Next/Previous button
4. System loads selected page of results
5. Table updates with new set of books
6. Current page is highlighted (blue background)
7. Pagination text updates (e.g., "Showing 5-8 of 24 books")
8. **Result:** User views different page of books

---

## Secondary User Flows

### Flow 8: Duplicate Book
**From:** Book Details screen
**Action:** User clicks "Duplicate" button
**Result:** System creates a copy of the book with "-copy" suffix, user navigates to Edit form to modify the duplicate

### Flow 9: Export Book Data
**From:** Book Details screen
**Action:** User clicks "Export Data" button
**Result:** System downloads book information as JSON/CSV/PDF file

### Flow 10: View Book Statistics
**From:** Book Details screen
**Displayed:** Metadata bar shows book ID, added date, last updated date, view count
**Result:** User sees when book was added and how popular it is

---

## User Flow Diagram

```
┌─────────────────┐
│  Books List     │ (Landing Page)
│  View           │
└────┬────────────┘
     │
     ├──────────────────┬──────────────────┬──────────────────┬──────────────┐
     │                  │                  │                  │              │
     ▼                  ▼                  ▼                  ▼              ▼
┌─────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────┐
│ Add Book    │  │ Edit Book    │  │ Book Details │  │ Search Books │  │ Filters  │
│ Form        │  │ Form         │  │ Screen       │  │ Screen       │  │ Applied  │
└─────┬───────┘  └─────┬────────┘  └──────┬───────┘  └──────┬───────┘  └────┬─────┘
      │                │                   │                  │               │
      │                │                   │                  │               │
    Submit           Update            View/Edit          Search          Update
      │                │                Delete              │              Table
      │                │               Duplicate            │               │
      │                │               Export               │               │
      └────────────────┴────────────────┴──────────────────┴───────────────┘
                                        │
                                        ▼
                              ┌──────────────────┐
                              │ Delete Modal     │
                              │ (Confirmation)   │
                              └──────┬───────────┘
                                     │
                                   Confirm
                                     │
                                     ▼
                              ┌──────────────────┐
                              │ Book Deleted     │
                              │ → List View      │
                              └──────────────────┘
```

---

## Navigation Map

### Screen Connections

```
Books List View ←──────── [Entry Point / Home]
    ├→ Add Book Form (+ Add Book button)
    ├→ Update Book Form (Edit button on row)
    ├→ Book Details (Row click or View action)
    ├→ Search Books (Search link or Advanced search)
    └→ Filtered List (Apply filters)

Add Book Form
    ├→ Books List View (Cancel, Back, or Success)
    └→ Books List View (After successful add)

Update Book Form
    ├→ Book Details (Cancel or Success)
    ├→ Books List View (Back button)
    └→ Book Details (After successful update)

Book Details
    ├→ Update Book Form (Edit Book button)
    ├→ Delete Modal (Delete Book button)
    ├→ Books List View (Back button or Delete success)
    └→ Duplicate Action (Duplicate button → Edit Form with copy)

Search Books
    ├→ Books List View (Search results or Back button)
    └→ Search Results View (After search)

Delete Modal
    ├→ Book Details (Cancel)
    └→ Books List View (Delete Permanently → Delete → Redirect)
```

---

## User Interaction Patterns

### Pattern 1: Create-Read-Update-Delete (CRUD)
- **Create:** Add Book Form → Submit → List View
- **Read:** List View → Details Screen
- **Update:** List View → Edit Form → Submit → Details/List View
- **Delete:** Details → Delete Modal → Confirm → List View

### Pattern 2: Search-Filter-View
- **Quick Search:** Type in header → Filter results
- **Advanced Search:** Search screen → Multiple criteria → Results
- **Filters:** Dropdown filters → Apply → Filtered list

### Pattern 3: Navigation Consistency
- **Back Button:** Always returns to previous screen (top-right on forms/details)
- **Cancel Button:** Returns without saving (bottom-left on forms)
- **Success Redirect:** After successful action, redirect to logical next screen

### Pattern 4: Confirmation for Destructive Actions
- **Delete:** Requires modal confirmation with warning
- **Other Actions:** Direct execution (Edit, Add, Update)

---

## Error Handling Flows

### Validation Error Flow
1. User submits form with invalid/missing data
2. System shows error messages next to problem fields
3. Submit button remains enabled
4. User corrects errors
5. User resubmits
6. If valid → Success flow continues

### Network Error Flow
1. User performs action requiring server
2. Network request fails
3. System shows error message: "Unable to connect. Please try again."
4. User clicks "Retry" or dismisses error
5. User can retry action or navigate away

### "No Results" Flow
1. User performs search or filter
2. No books match criteria
3. System shows: "No books found" message
4. System suggests: "Try different search terms" or "Clear filters"
5. User adjusts search/filters or returns to full list

---

## User Personas & Their Flows

### Persona 1: Library Administrator (Primary User)
**Goal:** Manage book inventory
**Primary Flows:** Add Book, Edit Book, Delete Book, Search Books
**Frequency:** Daily, multiple times

### Persona 2: Catalog Manager
**Goal:** Browse and organize book collection
**Primary Flows:** Browse List, Apply Filters, Update Book Info
**Frequency:** Daily

### Persona 3: Data Entry Clerk
**Goal:** Add new books in bulk
**Primary Flows:** Add Book (repeated), Duplicate Book
**Frequency:** Weekly

---

## Flow Success Metrics

| Flow | Steps | Estimated Time | Critical Path | User Errors | Success Rate |
|------|-------|----------------|---------------|-------------|--------------|
| Add New Book | 10 | 2-3 min | Form validation | ISBN format, required fields | 95% |
| View & Delete | 12 | 1-2 min | Confirmation modal | Accidental clicks | 100% |
| Edit Book | 11 | 2-3 min | Form validation | N/A (pre-filled) | 98% |
| Quick Search | 6 | 10-30 sec | Search query | Spelling errors | 90% |
| Advanced Search | 9 | 1-2 min | Multiple criteria | Over-filtering | 85% |
| Browse & Filter | 8 | 30-60 sec | Filter selection | N/A | 100% |

---

## Edge Cases & Alternative Scenarios

### Empty States
- **No Books in System:** Show "No books yet. Add your first book!" with prominent Add button
- **Search No Results:** Show "No books found" with "Clear search" option
- **Filtered No Results:** Show "No books match filters" with "Reset filters" option

### Loading States
- **Fetching Books:** Show skeleton loaders or spinner in table
- **Submitting Form:** Disable button, show "Saving..." text
- **Uploading Image:** Show progress bar or spinner

### Success States
- **Book Added:** Show success toast: "Book added successfully!"
- **Book Updated:** Show success toast: "Book updated successfully!"
- **Book Deleted:** Show success toast: "Book deleted permanently"

---

## Summary

- **Total Primary Flows:** 7
- **Total Secondary Flows:** 3
- **Total Screens:** 5
- **Most Complex Flow:** Advanced Search (9 steps, multiple criteria)
- **Most Used Flow:** Browse & Filter (simple, frequent)
- **Critical Flow:** Add New Book (core functionality)

All flows follow intuitive patterns with clear navigation, consistent button placement, and proper user feedback for actions.
