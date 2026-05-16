# DevTracker 🚀

A modern, high-performance project management dashboard built with **Vue 3**, **TypeScript**, and **Tailwind CSS** — backed by a **Spring Boot** REST API.

---

## 📂 Monorepo Structure

```
dev-tracker/
├── frontend/               # Vue 3 + Vite + TypeScript SPA
│   ├── src/
│   │   ├── api/            # Axios HTTP client (http.ts)
│   │   ├── assets/         # Global styles
│   │   ├── components/     # Reusable UI components
│   │   ├── composables/    # Vue composables
│   │   ├── directives/     # Custom directives
│   │   ├── layouts/        # MainLayout / AuthLayout
│   │   ├── router/         # Vue Router config
│   │   ├── stores/         # Pinia stores
│   │   └── views/          # Page components
│   ├── cypress/            # End-to-end tests
│   ├── public/             # Static assets
│   ├── .env.development    # Dev environment variables
│   ├── .env.production     # Prod environment variables
│   ├── index.html
│   ├── vite.config.ts
│   └── package.json
├── backend/                # Spring Boot REST API (Java)
│   ├── src/
│   │   └── main/java/...
│   └── pom.xml
├── .gitignore              # Covers both frontend/ and backend/
└── README.md
```

---

## 🖥 Running Frontend

```bash
cd frontend
npm install
npm run dev
```

The dev server will start at **http://localhost:5173** by default.

Other useful commands:

```bash
npm run build       # Production build
npm run test        # Run Vitest unit tests
npm run test:e2e    # Run Cypress E2E tests
npm run lint        # ESLint
npm run format      # Prettier
```

---

## ☕ Running Backend

```bash
cd backend
./mvnw spring-boot:run
```

The API will be available at **http://localhost:8080**.

---

## 🔐 Environment Variables

All frontend environment variables are prefixed with `VITE_` and live in `frontend/.env.*` files.

| Variable | Development | Production | Description |
|---|---|---|---|
| `VITE_API_URL` | `http://localhost:8080` | `https://api.devtracker.com` | Base URL for the DevTracker REST API |

> **Note:** Never commit `.env*.local` files — they are gitignored.
> Copy `.env.development` to `.env.development.local` for local overrides.

---

## ✨ Features

- **Kanban Board** — Drag-and-drop task management across workflow columns
- **Statistics Dashboard** — Visualize team velocity and completion rates
- **Dynamic Layout System** — Seamlessly switches between `MainLayout` and `AuthLayout`
- **Responsive Design** — Optimized for desktop and mobile with Tailwind CSS v4

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Frontend Framework | [Vue 3](https://vuejs.org/) (Composition API + `<script setup>`) |
| Build Tool | [Vite](https://vitejs.dev/) |
| Language | TypeScript |
| Styling | [Tailwind CSS v4](https://tailwindcss.com/) |
| State Management | [Pinia](https://pinia.vuejs.org/) |
| Routing | [Vue Router 5](https://router.vuejs.org/) |
| HTTP Client | [Axios](https://axios-http.com/) |
| Backend | [Spring Boot](https://spring.io/projects/spring-boot) (Java) |

---

## 📄 License

This project is licensed under the MIT License.
