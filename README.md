# DevTracker 🚀

A modern, high-performance project management dashboard built with **Vue 3** and **Tailwind CSS**.

DevTracker is designed to help development teams track their progress, manage tasks via a Kanban-style board, and visualize performance through a dedicated statistics dashboard.

## ✨ Features

- **Kanban Board**: Visualize your workflow with interactive task columns.
- **Dynamic Layout System**: Seamlessly switch between `MainLayout` (for dashboard views) and `AuthLayout` (for login/registration).
- **Responsive Design**: Fully optimized for desktop and mobile using Tailwind CSS.
- **Modern Tech Stack**: Leveraging the latest Vue 3 features like `<script setup>` and Composition API.
- **Statistics Dashboard**: Track team velocity and task completion rates.

## 🛠 Tech Stack

- **Framework**: [Vue 3](https://vuejs.org/) (Composition API)
- **Build Tool**: [Vite](https://vitejs.dev/)
- **Styling**: [Tailwind CSS v4](https://tailwindcss.com/)
- **State Management**: [Pinia](https://pinia.vuejs.org/)
- **Routing**: [Vue Router 4](https://router.vuejs.org/)

## 🚀 Getting Started

### Prerequisites
- Node.js (v20+ recommended)
- npm

### Installation
1. Clone the repository
   ```bash
   git clone https://github.com/your-username/dev-tracker.git
   ```
2. Install dependencies
   ```bash
   npm install
   ```
3. Start development server
   ```bash
   npm run dev
   ```

## 📂 Project Structure

- `src/layouts/`: Dynamic layout components.
- `src/views/`: Main page components (Board, Stats, Login, etc.).
- `src/components/`: Reusable UI components like the NavBar.
- `src/router/`: Navigation logic and layout metadata.
- `src/assets/`: Global styles and Tailwind configuration.

## 📄 License
This project is licensed under the MIT License.
