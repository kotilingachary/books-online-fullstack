# Wireframes Folder

## Purpose
Place your Figma wireframe exports here. The wireframe-analyzer agent will process these images to extract UI components, navigation flows, and data requirements.

## Supported Formats
- PNG (recommended)
- JPG/JPEG
- PDF

## Naming Conventions
Use descriptive names that reflect the screen purpose:
- `home-page.png`
- `login-screen.png`
- `dashboard.png`
- `user-profile.png`
- `product-list.png`
- `product-detail.png`

## Requirements
- Minimum: 3 screens
- Recommended: 5-10 screens for complete application
- Include: Authentication, main features, CRUD operations

## Tips for Best Results
1. **Clear and legible** - High resolution images
2. **Complete flows** - Include all steps in user journey
3. **Consistent** - Use same design language across screens
4. **Annotated** (optional) - Add labels for clarity
5. **Show navigation** - Make it clear how screens connect

## Example Structure
```
wireframes/
├── 01-landing-page.png
├── 02-login.png
├── 03-register.png
├── 04-dashboard.png
├── 05-user-profile.png
├── 06-settings.png
└── README.md (this file)
```

## What the Analyzer Looks For
- Navigation elements (header, footer, sidebar)
- Forms (input fields, buttons, dropdowns)
- Data displays (tables, cards, lists)
- Interactive elements (buttons, links)
- Modals and dialogs
- Search and filter components

## After Placing Wireframes
Run the orchestrator:
```
Tell Claude Code: "Run the orchestrator agent"
```

Or run wireframe analyzer individually:
```
Tell Claude Code: "Run the wireframe-analyzer agent"
```

---

**Ready to start?** Drop your wireframes here and let the agents do the work!
