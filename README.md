# 🔍 Lost & Found — Campus Recovery Platform

A full-stack campus platform for reporting lost and found items, matching potential items automatically, and managing secure item recoveries between students.

---

## 🚀 Quick Start Guide

### 1. Prerequisites
- **Java 17+**
- **Node.js 18+** & **npm**
- **PostgreSQL 14+**

---

### 2. First-Time Database Setup & Backend Launch

1. Make sure PostgreSQL is installed and running on `localhost:5432`.
2. Open PowerShell in the project root directory.
3. Run the automated setup script:

```powershell
.\setup.ps1
```

4. Follow the interactive prompts to provide your local PostgreSQL username (defaults to `postgres`) and password.
5. The script automatically:
   - Verifies PostgreSQL availability and connectivity.
   - Checks if the `lostandfound` database exists and creates it only if needed.
   - Safely initializes required seed data (categories & test users) without modifying or destroying existing data.
   - Configures the runtime `SPRING_DATASOURCE_PASSWORD` environment variable.
   - Starts the Spring Boot backend on `http://localhost:8080`.

---

### 3. Subsequent Runs

Once the database has been initialized, developers do **NOT** need to recreate it.

To launch the project on subsequent runs, you can simply run:

```powershell
.\setup.ps1
```

Or, if your database is already running and you only want to start the Spring Boot backend directly:

- **Windows (PowerShell)**:
  ```powershell
  $env:SPRING_DATASOURCE_PASSWORD="your_postgres_password"; .\mvnw.cmd spring-boot:run
  ```
- **Linux / macOS**:
  ```bash
  SPRING_DATASOURCE_PASSWORD="your_postgres_password" ./mvnw spring-boot:run
  ```

The backend starts at `http://localhost:8080`. Hibernate will update schema tables automatically on startup.

---

### 4. Run Frontend (React + Vite)

In a new terminal window:

```bash
cd frontend
npm install   # First time only
npm run dev
```

For subsequent frontend runs:

```bash
cd frontend
npm run dev
```

- Open **[http://localhost:5173](http://localhost:5173)** on your machine.
- To access from another device on the same local network, open the Network URL printed in the terminal.
- API requests are automatically forwarded to backend `http://localhost:8080`.

---

## 🔑 Default Accounts for Testing (Development / Test Only)

The setup script automatically provisions these development accounts:

| Role | Email | Password | Access |
|---|---|---|---|
| **Student (User)** | `student@college.edu` | `password` | Explore, Search, Report, Claim & Recover items |
| **Admin** | `admin@college.edu` | `password` | All user features + Review & Resolve Complaints |

---

## 🛠️ Tech Stack

- **Backend**: Spring Boot 4, Spring Security, Spring Data JPA, JJWT
- **Database**: PostgreSQL
- **Frontend**: React 19, React Router v7, Axios, Vite
