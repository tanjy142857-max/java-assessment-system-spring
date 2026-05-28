# Assessment Feedback System (Spring Boot)

Spring Boot web application for academic management — a refactored version of the original Java Swing desktop app. Built as a coursework project.

**Live demo**: `http://localhost:8080` | **Default login**: `admin` / `admin123`

## Features by Role

| Role | Capabilities |
|------|-------------|
| **Admin Staff** | User CRUD, approve/reject student registrations, manage classes, configure grading system |
| **Academic Leader** | Module CRUD, assign lecturers to classes, generate and export reports |
| **Lecturer** | Design assessments (weightage caps), enter grades (auto letter-grade), provide student feedback |
| **Student** | Register/drop classes, submit assessments, view grades & feedback, add comments, view GPA |

## Tech Stack

- **Spring Boot 3.2.5** (Web, JPA, Security, Thymeleaf)
- **H2 Database** (file-mode, zero setup)
- **Bootstrap 5.3** (CDN, basic styling)
- **Maven** (build tool)
- **Java 17+**

## Quick Start

```bash
# 1. Clone
git clone https://github.com/tanjy142857-max/java-assessment-system.git
cd java-assessment-system-spring

# 2. Run (requires Maven)
mvn spring-boot:run

# 3. Open browser
# http://localhost:8080
```

Or double-click `run.bat` on Windows.

## Default Accounts

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `admin123` |

Other users can be registered via `/register` and approved by an admin.

## Project Structure

```
src/main/java/com/assessment/
├── Application.java              # Entry point
├── config/
│   ├── SecurityConfig.java       # Form login, role-based access
│   ├── CustomUserDetailsService.java
│   └── DataInitializer.java      # Seeds admin + grading system
├── model/                        # JPA entities (9 classes)
├── repository/                   # Spring Data JPA interfaces (9)
├── service/                      # Business logic (9 classes)
└── controller/                   # Web controllers (5 classes)
src/main/resources/
├── templates/                    # Thymeleaf HTML pages
├── application.properties        # H2 + server config
└── static/css/style.css
```

## Key Design Decisions

- **H2 file database**: Self-contained, no external DB install needed
- **Single-table user inheritance**: All roles in one `users` table via `@DiscriminatorColumn`
- **Server-side rendering**: Pure Thymeleaf + form submits, no REST API or JavaScript frontend
- **BCrypt password hashing** via Spring Security
- **Cascading deletes** on modules and classes for data integrity
- **Weightage enforcement**: Assessment total ≤ 100% per module; GPA includes modules at exactly 100%

## Related Projects

- [Java Swing version](https://github.com/tanjy142857-max/java-assessment-system) — original desktop application
