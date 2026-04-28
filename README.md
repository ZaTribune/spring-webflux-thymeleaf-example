<div style="margin-top: 20px" align="center">
  <div style="background-color: white; border-radius: 50%; width: 280px; height: 280px; display: flex; align-items: center; justify-content: center; margin: 0 auto; box-shadow: 0 4px 15px rgba(0,0,0,0.1);">
    <img src="src/main/resources/static/images/logo.svg" alt="KitchenMaster Logo" width="250" height="250"/>
  </div>

# 🍽️ KitchenMaster

> Not your average recipe website — this one is fully reactive. Built with Spring WebFlux & Thymeleaf.

![Java](https://img.shields.io/badge/Java-26+-brown)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0+-olivegreen)


</div>

---

## Features & Concepts Demonstrated

### Backend

- **Fully Reactive & Non-blocking** - Built on Spring WebFlux with Project Reactor.
  - Non-blocking streams with `Flux` and `Mono`.
  - Composable async operations with backpressure handling.
  - Asynchronous operations for superior performance.

- **Router Functions** - Functional routing with query parameters.
  - Example of functional routing as alternative to `@Controller` approach.
  - Demonstrates Spring WebFlux's flexible routing capabilities.

- **MongoDB Integration** - Embedded MongoDB with Spring Data Reactive Repositories.
  - Complete recipe, category, and ingredient management.

- **Smart Pagination** - Efficient data loading with configurable page sizes (12 recipes/page).
  - Frontend-driven pagination detection.
  - Automatic "Next" button disabling.
  - Optimized database queries with `skip()` and `take()`.

### Frontend

- **AJAX-powered UI** - Dynamic, responsive interface without page reloads.
- **Thymeleaf Templates** - Server-side rendering with template engine.
- **Bootstrap Styling** - Clean, modern, mobile-responsive design.
- **Image Management** - Base64 encoding and byte array handling.
  - Base64 encoding for recipe images.
  - Byte array storage for category images.
  - Efficient image loading.
- **Form Validation** - Client and server-side validation.

### Security & Quality

- **Spring Security** - Authentication and authorization.
  - Form-based authentication.
  - Role-based access control with Admin and User roles.
  - Bcrypt password encoding (strength 12).

- **Exception Handling** - Global error handling with custom error pages.
- **Unit & Integration Tests** - Comprehensive test coverage.
- **Docker Support** - Containerized deployment.

---

## Technology Stack

|                     | Technology                       |
|---------------------|----------------------------------|
| **Language**        | Java                             |
| **Main Framework**  | Spring Boot                      |
| **Reactive Stack**  | Spring WebFlux & Project Reactor |
| **Template Engine** | Thymeleaf                        |
| **Database**        | MongoDB                          |
| **Build Tool**      | Maven                            |
| **Security**        | Spring Security                  |
| **Utilities**       | Project Lombok                   |
| **Testing**         | JUnit 5, Mockito                 |
| **IDE**             | IntelliJ IDEA                    |
| **CI/CD**           | Docker                           |

---

### Default Credentials

```
Username: user1@gmail.com
Password: pass
Role: ADMIN

Username: user2@gmail.com
Password: pass
Role: USER
```

---

## Testing locally

Run the `docker-compose.yml` file to start the MongoDB container:

```bash
docker-compose up -d
```
Also, run the application with profile `dev` to bootstrap/reset the database with sample data:

```bashbash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

To run the test suite:

```bash
mvn test
```

View coverage:

```bash
mvn jacoco:report
```

---
## Authors
[![Linkedin](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white&label=Muhammad%20Ali)](https://linkedin.com/in/zatribune)

**Made with ❤️ for the Java & Spring community**.

