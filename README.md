# 💇‍♂️ EasyArrange – Booking Web Application For a Salon

A modern full-stack web application for managing bookings in a multifunctional salon (haircuts, beard trims, nail care, etc.).
Built with Spring Boot (backend) and React (frontend).

---

## 🚀 Tech Stack

* Backend: Spring Boot
* Frontend: React + Vite
* Database: MySQL
* Animations: Framer Motion
* Routing: React Router

---

## 📦 Project Structure

```text
EasyArrange/
│
├── Backend/        # Spring Boot application
├── Frontend/       # React (Vite) application
└── README.md
```

---

## ⚙️ Setup Instructions

Follow these steps to run the application locally:

### 1️⃣ Start the Database

Make sure your MySQL server is running.

> ⚠️ Ensure your database credentials match the `application.properties` in the backend.

---

### 2️⃣ Run the Backend

Navigate to the backend folder and start the Spring Boot application:

```bash
cd Backend
./mvnw spring-boot:run
```

Or run it from your IDE.

---

### 3️⃣ Run the Frontend

Navigate to the root project folder:

```bash
cd EasyArrange
cd Frontend
```

Install dependencies:

```bash
npm install
npm install react-router-dom framer-motion @tanstack/react-query
```

Start the development server:

```bash
npm run dev
```

---

## 🌐 Access the Application

* Frontend: http://localhost:5173
* Backend: http://localhost:8080
