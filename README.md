# 🎓 Student Management System (Spring Boot + Supabase)

A full-stack **role-based Student Management System** built using **Spring Boot, Thymeleaf, and Supabase (PostgreSQL)**.

🔗 **Live Demo:**  
https://student.gridly.xyz/

---

## 🚀 Overview

This system provides:

- Secure authentication (Login / Register / Forgot Password)
- Role-based dashboards (Admin & Student)
- CRUD operations for student management
- Course management
- Supabase PostgreSQL integration
- Clean UI with sidebar navigation and charts

---

## 🔐 Authentication Features

- Login  
- Register  
- Forgot Password  
- Logout  
- Role-based session handling  
- Access control (Admin / Student)  

---

## 👨‍💼 Admin Dashboard

- Total student count  
- Students by course (Pie Chart using Chart.js)  
- Add Student  
- Update Student  
- Delete Student  
- Manage Courses  
- Sidebar navigation with active state  

---

## 👨‍🎓 Student Dashboard

- View personal details  
- Update email & password  
- Secure session-based access  
- Dedicated sidebar UI  

---

## 🧰 Tech Stack

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
- Maven  
- Git & GitHub  
- VS Code  
- Supabase  

---

## 📂 Project Structure

```text
student-management
│
├── app
│   ├── src/main/java/com/example/App
│   ├── src/main/resources
│   │   ├── static/css
│   │   ├── templates
│   │   └── application.properties
│   └── pom.xml
│
├── .env
├── .gitignore
└── README.md
```

---

## ⚙️ Setup & Run Locally

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/krushna1133/student-management
```

### 2️⃣ Move Into Project Directory

```bash
cd student-management/app
```

### 3️⃣ Configure Database

Open:

```
src/main/resources/application.properties
```

Add your Supabase PostgreSQL credentials:

```properties
spring.datasource.url=jdbc:postgresql://YOUR_HOST:5432/YOUR_DB
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080
```

---

### 4️⃣ Run the Application

```bash
mvn spring-boot:run
```

Open in browser:

```
http://localhost:8080
```

---

## 🌍 Environment Variables (.env)

Example:

```
SUPABASE_DB_URL=
SUPABASE_DB_USER=
SUPABASE_DB_PASSWORD=
```

---

## 📊 Database

- Hosted on Supabase  
- PostgreSQL  
- Connected via JDBC Template  
- Auto table creation with `hibernate.ddl-auto=update`  

---

## 📌 Future Improvements

- JWT Authentication  
- Password encryption with BCrypt  
- REST API version  
- Docker deployment  
- Role management system  
- Audit logging  

---

## 👨‍💻 Author

Krushna Rathod  
B.Tech – Artificial Intelligence & Data Science  
Full Stack Developer  

---

⭐ If you found this project useful, consider giving it a star.