# 🏗️ Construction Site Time & Attendance Management System

This is a full-stack Time and Attendance Management System specifically designed for construction sites. It is built using **Spring Boot** and uses **MongoDB** (via MongoDB Compass) for data persistence. The frontend is embedded within the backend's `static/` folder and served through Spring Boot.

---

## 🔧 Technologies Used

- **Java 17**
- **Spring Boot**
- **MongoDB Compass**
- **HTML5, CSS3, JavaScript** (Frontend)
- **REST APIs**
- **Postman** (for API testing)

---

## 📁 Folder Structure
backend/
│
├── src/
│ └── main/
│ ├── java/
│ │ └── com/timeattendance/ # All Java backend code
│ ├── resources/
│ │ ├── application.properties
│ │ └── static/ # Frontend (HTML/CSS/JS)
│
├── pom.xml # Maven build file
└── README.md # You are here!




---

## 🚀 How to Run the Project

> **Make sure MongoDB is running locally (or in MongoDB Compass) before starting the backend.**

### Step-by-Step:

| Task                       | Command                                                                        |
| -------------------------- | ------------------------------------------------------------------------------ |
| Clone repository           | `git clone https://github.com/YourUsername/Construction-Attendance-System.git` |
| Navigate into folder       | `cd Construction-Attendance-System`                                            |
| Start MongoDB (if not GUI) | `mongod`                                                                       |
| Start Spring Boot app      | `./mvnw spring-boot:run` or `mvn spring-boot:run`                              |
| Open in browser            | `http://localhost:8080/`                                                       |



📌 Key Features

✅ Admin, Manager, Worker Login
👷 Worker self-registration with validation
📅 View attendance records per worker
📤 Leave request submission and status
💰 Wage summary based on attendance
🧾 Individual worker profile view & update
📍 Assigned projects (static for now)
🔔 Manager messages & smart alerts
📱 Mobile responsive layout


-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
🛠️ Future Scope

QR or GPS-based attendance
Manager dashboard for approvals
PDF report downloads
Push notifications & email alerts
Project status tracking



🙌 Author
Sumedha Somani
GitHub Profile
