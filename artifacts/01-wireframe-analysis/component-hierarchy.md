# Component Hierarchy Analysis

## Project: Books Online - Book Management System
**Analysis Date:** 2024-02-10
**Total Screens:** 5

---

## Global Components (Appear on all screens)

### Header/Navigation
**Appears on:** All screens
**Contains:**
- **Logo/Brand Text**: "Books Online" (left-aligned)
- **Search Bar**: Global search functionality (center, appears on list view)
- **Action Buttons**: Context-sensitive (e.g., "+ Add Book" on list view, "← Back" on detail views)

### Footer
**Appears on:** All screens
**Contains:**
- Copyright text: "© 2024 Books Online. All rights reserved."
- Consistent dark background (#333333)

---

## Reusable Component Library

Based on wireframe analysis, these components should be built as reusable:

### 1. Button Component
**Variants:**
- **Primary**: Green (#4CAF50) - "Add Book", "Search"
- **Secondary**: Gray/Light (#EEEEEE) - "Cancel", "Back"
- **Danger**: Red (#F44336) - "Delete", "Delete Permanently"
- **Info**: Blue (#2196F3) - "Edit"
- **Warning**: Orange (#FF9800) - "Update Book", "Duplicate"

**Props:** label, variant, onClick, disabled, size

### 2. Input Component
**Types:**
- Text input
- Number input
- Search input (with search icon)
- Textarea (multi-line)

**Features:**
- Label support
- Placeholder text
- Required field indicator (*)
- Validation states
- Read-only state (with lock icon)

**Props:** label, type, placeholder, isRequired, isReadOnly, value, onChange, validation

### 3. Dropdown/Select Component
**Used for:**
- Genre selection
- Author filter
- Year filter
- Language selection
- Availability filter
- Sort by

**Features:**
- Dropdown arrow indicator (▼)
- Placeholder text
- Single selection

**Props:** label, options, placeholder, value, onChange, isRequired

### 4. File Upload Component
**Features:**
- Click to upload
- Drag and drop support
- Image preview
- Change image button (for edit mode)

**Props:** label, acceptedFormats, onUpload, currentImage

### 5. Table Component
**Features:**
- Column headers
- Sortable columns
- Row data display
- Action buttons per row
- Pagination

**Props:** columns, data, actions, pagination, onSort

### 6. Pagination Component
**Features:**
- Previous/Next buttons (◀ ▶)
- Page numbers
- Current page indicator
- Total count display ("Showing X-Y of Z books")

**Props:** currentPage, totalPages, totalItems, onPageChange

### 7. Card/Container Component
**Used for:**
- Form containers (white background with rounded corners)
- Filter sections
- Price display boxes

**Features:**
- Rounded corners (border-radius)
- Box shadow/border
- Padding

**Props:** children, variant, width

### 8. Badge Component
**Used for:**
- "In Stock" status badge
- Genre tags

**Features:**
- Colored background
- Rounded pill shape
- Small text

**Props:** label, color, variant

### 9. Modal/Dialog Component
**Features:**
- Overlay background (semi-transparent black)
- Centered dialog box
- Header with title
- Content area
- Action buttons (Cancel, Confirm)

**Props:** title, content, isOpen, onClose, onConfirm, severity

### 10. Rating Display Component
**Features:**
- Star rating visualization (★★★★☆)
- Numeric rating (4.2 / 5.0)
- Review count display

**Props:** rating, maxRating, reviewCount

### 11. Info Banner/Alert Component
**Used for:**
- Book ID display with metadata
- Search tips
- Required fields note

**Features:**
- Colored background (warning, info, etc.)
- Icon support
- Dismissible (optional)

**Props:** message, type, dismissible

### 12. Checkbox Component
**Used for:**
- Search filters (In Stock Only, On Sale, New Releases)

**Features:**
- Label
- Checked/unchecked states

**Props:** label, checked, onChange

---

## Screen-Specific Components

### Books List View Screen
**Unique components:**
- **BooksTable**: Specialized table showing book cover, title, author, genre, year, actions
  - Includes thumbnail images for covers
  - Edit and Delete action buttons per row
- **FilterBar**: Quick filters for Genre, Author, Year
  - Three dropdowns in a row

### Add Book Form Screen
**Unique components:**
- **BookForm**: Comprehensive form layout
  - Left column: Book cover upload (large area)
  - Right column: Input fields in 2-column grid
  - All form fields arranged in logical groups

### Search Books Screen
**Unique components:**
- **QuickSearchBar**: Large, prominent search input
  - Search icon
  - Primary search button
- **AdvancedSearchForm**: Complex form with multiple search criteria
  - 2-column grid layout
  - Grouped fields (basic info, filters, ranges)
  - Checkboxes for additional filters

### Update Book Form Screen
**Unique components:**
- **BookIDDisplay**: Info banner showing book ID and last updated date
  - Yellow/warning background
- **ExistingCoverPreview**: Shows current cover with "Change Image" button
  - Different from upload component in Add form

### Book Details Screen
**Unique components:**
- **BookDetailsView**: Large-format display
  - Left: Large cover image with availability badge
  - Right: Detailed information grid
  - Price display box with discount information
- **DetailsGrid**: Definition list layout for book metadata
  - Label-value pairs
- **MetadataBar**: Bottom info bar showing book ID, dates, view count
- **ActionButtonGroup**: Four action buttons in a row
  - Edit, Duplicate, Export, Delete

---

## Recommended Component Architecture

### Atomic Design Hierarchy

#### Atoms (Basic building blocks)
- Button
- Input
- Label
- Badge
- Icon
- Checkbox
- Text

#### Molecules (Simple component groups)
- InputField (Label + Input + Error)
- DropdownField (Label + Dropdown)
- SearchBar (Input + Search Icon + Button)
- PaginationControls (Previous + Page Numbers + Next)
- RatingDisplay (Stars + Text)

#### Organisms (Complex UI sections)
- Header (Logo + Search + Actions)
- Footer (Copyright text)
- BooksTable (Table + Rows + Actions + Pagination)
- BookForm (Multiple InputFields + FileUpload + Buttons)
- FilterBar (Multiple Dropdowns)
- AdvancedSearchForm (Multiple InputFields + Dropdowns + Checkboxes)
- DeleteConfirmationModal (Modal + Content + Buttons)
- BookDetailsView (Cover + Info Grid + Metadata)

#### Templates (Page layouts)
- ListPageLayout (Header + Filters + Table + Footer)
- FormPageLayout (Header + Centered Form Container + Footer)
- DetailPageLayout (Header + Content Area + Footer)

#### Pages (Actual route pages)
- BooksListPage
- AddBookPage
- EditBookPage
- SearchBooksPage
- BookDetailsPage

---

## Component Reusability Matrix

| Component | Books List | Add Book | Search | Edit Book | Details | Reusability Score |
|-----------|------------|----------|--------|-----------|---------|-------------------|
| Header | ✓ | ✓ | ✓ | ✓ | ✓ | 100% |
| Footer | ✓ | ✓ | ✓ | ✓ | ✓ | 100% |
| Button | ✓ | ✓ | ✓ | ✓ | ✓ | 100% |
| Input | ✗ | ✓ | ✓ | ✓ | ✗ | 60% |
| Dropdown | ✓ | ✓ | ✓ | ✓ | ✗ | 80% |
| Table | ✓ | ✗ | ✗ | ✗ | ✗ | 20% |
| Pagination | ✓ | ✗ | ✗ | ✗ | ✗ | 20% |
| Modal | ✗ | ✗ | ✗ | ✗ | ✓ | 20% |
| Badge | ✗ | ✗ | ✗ | ✗ | ✓ | 20% |
| File Upload | ✗ | ✓ | ✗ | ✓ | ✗ | 40% |
| Checkbox | ✗ | ✗ | ✓ | ✗ | ✗ | 20% |
| Rating Display | ✗ | ✗ | ✗ | ✗ | ✓ | 20% |
| Info Banner | ✗ | ✗ | ✓ | ✓ | ✓ | 60% |

**High reusability (80%+):** Header, Footer, Button, Dropdown
**Medium reusability (40-79%):** Input, File Upload, Info Banner
**Low reusability (20-39%):** Table, Pagination, Modal, Badge, Checkbox, Rating Display

---

## Component Dependencies

```
BooksListPage
  ├── Header
  │   ├── Logo
  │   ├── SearchBar
  │   └── Button (Add Book)
  ├── FilterBar
  │   └── Dropdown (x3)
  ├── BooksTable
  │   ├── TableHeader
  │   ├── TableRow (x N)
  │   │   ├── Image (Cover)
  │   │   ├── Text (Title, Author, etc.)
  │   │   └── ButtonGroup
  │   │       ├── Button (Edit)
  │   │       └── Button (Delete)
  │   └── Pagination
  └── Footer

AddBookPage
  ├── Header
  │   └── Button (Back)
  ├── BookForm
  │   ├── FileUpload (Cover)
  │   ├── InputField (x8)
  │   ├── DropdownField (x2)
  │   ├── Textarea
  │   └── ButtonGroup
  │       ├── Button (Cancel)
  │       └── Button (Add Book)
  └── Footer
```

---

## Styling Guidelines

### Color Palette
- **Primary Green**: #4CAF50 (Add, Success actions)
- **Primary Blue**: #2196F3 (Edit, Info actions)
- **Danger Red**: #F44336 (Delete actions)
- **Warning Orange**: #FF9800 (Update actions)
- **Gray**: #9E9E9E (Secondary actions)
- **Background**: #F5F5F5 (Page background)
- **Card Background**: #FFFFFF (Content cards)
- **Text Primary**: #333333
- **Text Secondary**: #666666
- **Text Light**: #999999
- **Border**: #E0E0E0, #CCCCCC

### Typography
- **Font Family**: Arial, sans-serif
- **Heading 1**: 28px, bold
- **Heading 2**: 24px, bold
- **Body Large**: 16px
- **Body Regular**: 13-14px
- **Small Text**: 11-12px

### Spacing
- **Border Radius**: 4px (inputs, cards), 8px (large containers)
- **Padding**: 20-40px for containers
- **Margins**: Consistent 10-20px between elements

---

## Responsive Design Considerations

While wireframes show desktop layout (1200x800/900), components should be designed responsively:

### Breakpoints Recommended:
- **Mobile**: < 768px - Stack form fields, full-width table
- **Tablet**: 768-1024px - 2-column forms, scrollable table
- **Desktop**: > 1024px - As shown in wireframes

### Component Adaptations:
- **BooksTable**: Should become scrollable horizontally on mobile, or switch to card layout
- **BookForm**: 2-column layout on desktop, single column on mobile
- **FilterBar**: Stack filters vertically on mobile
- **Header SearchBar**: Hide on mobile, add search icon button

---

## Accessibility Recommendations

1. **All buttons**: Proper ARIA labels and keyboard navigation
2. **Form inputs**: Associated labels, required field indicators
3. **Tables**: Proper table markup with <th> headers
4. **Modals**: Focus trap, ESC key to close, ARIA role="dialog"
5. **Images**: Alt text for book covers
6. **Color contrast**: Ensure WCAG AA compliance (especially for badges and buttons)

---

## Component Implementation Priority

**Phase 1 - Core Components (Must build first):**
1. Button
2. Input
3. Dropdown
4. Header
5. Footer

**Phase 2 - Forms & Data:**
6. InputField (Input + Label)
7. FileUpload
8. Textarea
9. Checkbox
10. BookForm

**Phase 3 - Complex Components:**
11. Table
12. Pagination
13. Modal
14. Badge
15. Rating Display

**Phase 4 - Page-Specific:**
16. BooksTable
17. FilterBar
18. AdvancedSearchForm
19. BookDetailsView

---

## Summary

- **Total Reusable Components:** 12 core + 4 molecules + 8 organisms = **24 components**
- **Most Used Components:** Button (appears 15+ times), Input (8+ instances), Dropdown (7+ instances)
- **Design System:** Consistent color palette, typography, and spacing throughout
- **Component Library:** Should be built using React with Tailwind CSS for rapid development

All components follow a consistent design language with clear visual hierarchy and user feedback patterns.
