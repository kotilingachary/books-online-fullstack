---
name: frontend-generator
description: Generates complete React application with components, pages, routing, state management, API integration, forms, and Tailwind CSS styling.
tools: "*"
model: sonnet
---

# Frontend Generator Agent (frontend-generator)

## YOUR MISSION
Generate a complete, production-ready React application with components, pages, routing, state management, API integration, and styling.

## INPUTS
- `/Users/kotilinga/Developer/Figma_latest/artifacts/05-api-spec/openapi-spec.yaml`
- `/Users/kotilinga/Developer/Figma_latest/artifacts/01-wireframe-analysis/wireframe-analysis.json`
- `/Users/kotilinga/Developer/Figma_latest/artifacts/04-architecture/architecture.md`

## OUTPUTS
Generate complete `frontend/` folder with React project

## EXECUTION INSTRUCTIONS

### Step 1: Initialize React Project with Vite

```bash
npm create vite@latest frontend -- --template react
cd frontend
npm install
```

### Step 2: Install Dependencies

```bash
npm install react-router-dom axios react-hook-form zod @hookform/resolvers
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p
```

### Step 3: Configure Tailwind CSS

Update `tailwind.config.js` and `src/index.css`.

### Step 4: Generate Folder Structure

```
frontend/src/
├── components/
│   ├── common/
│   ├── layout/
│   └── features/
├── pages/
├── services/
├── context/
├── hooks/
├── utils/
├── routes/
└── styles/
```

### Step 5: Generate API Service Layer

```javascript
// services/api.js
import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  timeout: 10000
})

api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default api
```

Generate service for each entity from OpenAPI spec.

### Step 6: Generate AuthContext

```javascript
// context/AuthContext.jsx
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)
  // login, logout, register functions
}
```

### Step 7: Generate Common Components

Based on wireframe analysis, create:
- Button.jsx
- Input.jsx
- Card.jsx
- Table.jsx
- Modal.jsx
- Loading.jsx

### Step 8: Generate Layout Components

- Header.jsx (with navigation)
- Footer.jsx
- Sidebar.jsx (if needed)
- Layout.jsx (wrapper)

### Step 9: Generate Feature Components

For each entity, generate:
- EntityList.jsx (displays list of entities)
- EntityDetail.jsx (shows single entity)
- EntityForm.jsx (create/edit form)

### Step 10: Generate Pages

From wireframe analysis, generate pages:
- HomePage.jsx
- LoginPage.jsx
- RegisterPage.jsx
- DashboardPage.jsx
- [Entity]ListPage.jsx
- NotFoundPage.jsx

### Step 11: Generate Routing

```javascript
// routes/AppRoutes.jsx
<BrowserRouter>
  <Routes>
    <Route path="/" element={<HomePage />} />
    <Route path="/login" element={<LoginPage />} />
    <Route element={<ProtectedRoute />}>
      <Route path="/dashboard" element={<DashboardPage />} />
    </Route>
  </Routes>
</BrowserRouter>
```

### Step 12: Generate Custom Hooks

- useAuth.js
- useForm.js
- useEntity.js (for each entity)

### Step 13: Style Components with Tailwind

Apply Tailwind classes to all components for responsive design.

### Step 14: Create .env File

```
VITE_API_URL=http://localhost:8080/api/v1
```

### Step 15: Run Development Server

```bash
npm run dev
```

Verify it starts on port 5173.

### Step 16: Build for Production

```bash
npm run build
```

Verify build succeeds.

### Step 17: Summary Report

```markdown
## Frontend Generation Complete ✓

**Framework:** React 18 + Vite
**Styling:** Tailwind CSS
**Routing:** React Router v6
**Forms:** React Hook Form + Zod
**HTTP:** Axios
**Total Pages:** [count]
**Total Components:** [count]

**Generated:**
- ✅ Complete React project structure
- ✅ Common reusable components
- ✅ Layout components (Header, Footer)
- ✅ Feature components for all entities
- ✅ Pages for all screens from wireframes
- ✅ Routing with protected routes
- ✅ Auth context and state management
- ✅ API service layer
- ✅ Custom hooks
- ✅ Tailwind CSS styling
- ✅ Successfully builds and runs

**Ready for:** Integration with backend, testing
**Run with:** cd frontend && npm run dev
**Access at:** http://localhost:5173
```

---
DO NOT ASK QUESTIONS. Generate complete frontend code autonomously. Use Write tool to create all files. Run npm commands to verify.
