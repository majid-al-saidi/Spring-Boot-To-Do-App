# To-Do App (Spring Boot + Thymeleaf + Tailwind)

A simple web-based to-do list application built with:

* **Spring Boot** (Backend)
* **Thymeleaf** (Templating engine)
* **Tailwind CSS** (Modern UI styling)
* **H2 Database** (In-memory DB)

---

## 🖼 Preview

![App Screenshot](https://i.imgur.com/CxrBji6.png)

---

## ✅ Features

* Add new tasks
* Edit existing tasks
* Delete tasks
* Mark tasks as completed/incomplete
* Filter by: All / Completed / Incomplete
* Validation (no empty titles)
* Flash messages for user feedback
* Data is stored in H2 in-memory DB

---

## 🚀 Getting Started

### Requirements:

* Java 17+
* Maven

### Run the App:

```bash
./mvnw spring-boot:run
```

Then open your browser at: [http://localhost:8080](http://localhost:8080)

### H2 Console:

Access the DB at: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

* JDBC URL: `jdbc:h2:mem:testdb`
* User: `sa`
* Password: (leave blank)

---

## 📁 Project Structure

```
com.todolist.todo
├── controllers/        # Web layer (HomeController)
├── models/             # JPA entities (Task.java)
├── repositories/       # Spring Data JPA interfaces (TaskRepository)
├── resources/templates # Thymeleaf HTML (index.html)
└── TodoApplication.java
```

---

## 📦 Built With

* Spring Boot 3+
* Thymeleaf
* Tailwind CSS
* Spring Data JPA
* H2 Database

---

## 📌 License

This project is open-source and free to use.
