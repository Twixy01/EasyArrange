# 💇‍♂️ EasyArrange – Booking Web Application For a Salon

A modern, full-stack web application designed for managing bookings in a multifunctional salon (haircuts, beard trims, nail care, etc.). Built with **Spring Boot** for a robust backend and **React** for a fluid, responsive frontend.

---

## 🚀 Tech Stack

| Component | Technology |
| :--- | :--- |
| **Backend** | Spring Boot (Java 17) |
| **Frontend** | React + Vite |
| **Database** | MySQL / MariaDB |
| **Styling/UI** | Framer Motion, Axios |
| **Security** | BCrypt Password Hashing |

---

## ⚙️ Setup Instructions

Follow these steps to run the application locally:

### 0️⃣ Clone the Repository

```bash
git clone https://github.com/Twixy01/EasyArrange.git
cd EasyArrange
```

### 1️⃣ Start the Database

1. Make sure your **MySQL server** is running.
2. Create a database named `easyarrange`.
3. Import the `easyarrange.sql` file located in the `Database/` folder to set up tables and initial data.

> ⚠️ **Note:** Ensure your database credentials in `Backend/src/main/resources/application.properties` match your local MySQL settings.

### 2️⃣ Run the Backend

Navigate to the backend folder and start the Spring Boot application using the Maven wrapper:

```bash
cd Backend
```

**On Windows:**

```bash
.\mvnw.cmd spring-boot:run
```

**On Linux/macOS:**

```bash
./mvnw spring-boot:run
```

### 3️⃣ Run the Frontend

Navigate to the frontend folder, install the necessary dependencies, and start the development server:

```bash
cd Frontend
npm install
npm run dev
```

---

## 📦 Project Structure

```plaintext
EasyArrange/
│
├── Backend/          # Spring Boot application
├── Database/         # Database dump (easyarrange.sql)
├── Frontend/         # React (Vite) application
└── README.md         # Documentation
```

---

## 🌐 Access Ports

| Service | URL |
| :--- | :--- |
| **Frontend** | http://localhost:5173 |
| **Backend API** | http://localhost:8080 |

---

## 🛡️ Key Database Features

- **Normalized Schema:** Efficient storage with 3NF design.
- **Integrity Constraints:** Prevents double-booking via `uq_active_booking_slot` unique keys.
- **Status Tracking:** Comprehensive booking lifecycle (`BOOKED`, `CANCELLED`, `COMPLETED`, `NO_SHOW`).
- **Shift Management:** Flexible scheduling for staff members across the week.
