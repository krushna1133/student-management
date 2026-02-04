This project is using Spring Boot and supabase with login, register, forget password . After login there are logout option, student tab where student can add, delete, update
You can check here: https://student.gridly.xyz/

# Student Management System (Spring Boot)

A role-based **Student Management System** built using **Spring Boot** and **Thymeleaf**.  
The application provides separate dashboards for **Admin** and **Student** users with secure login and session-based access control.

The backend database is powered by **Supabase (PostgreSQL)**.

---

## 🚀 Features

### 🔐 Authentication

- Admin and Student login
- Role-based access control
- Secure session handling
- Logout functionality

### 👨‍💼 Admin Features

- Admin dashboard with total student count
- Pie chart showing students by course
- View students list
- Manage courses
- Sidebar navigation with active state

### 👨‍🎓 Student Features

- Student dashboard
- View personal details
- Update profile (email & password)
- Separate student sidebar with active state

---

## 🧰 Technologies Used

### Backend

- Java 21
- Spring Boot
- Spring MVC
- JDBC Template
- PostgreSQL (Supabase)

### Frontend

- HTML5
- CSS3
- Thymeleaf
- Chart.js
- Font Awesome

### Tools

- VS Code
- Maven
- Git & GitHub
- Supabase

---

## 📂 Project Structure

```text
student-management-system
│
├── src/main/java/com/example/App
│   ├── AppApplication.java
│   ├── AuthController.java
│   ├── AdminController.java
│   └── StudentController.java
│
├── src/main/resources
│   ├── static/css
│   │   └── sidebar.css
│   │
│   ├── templates
│   │   ├── index.html
│   │   ├── login.html
│   │   ├── register.html
│   │   ├── admin-dashboard.html
│   │   ├── admin-students.html
│   │   ├── admin-courses.html
│   │   ├── student-dashboard.html
│   │   ├── student-updateprofile.html
│   │   └── fragments
│   │       ├── admin-sidebar.html
│   │       └── student-sidebar.html
│   │
│   ├── application.properties
│   └── error/404.html
│
├── .env
├── .gitignore
├── pom.xml
└── README.md
```
