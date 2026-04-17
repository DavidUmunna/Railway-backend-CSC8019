# CSC8019 Coffee Shop Backend

## Contributing & Git Workflow

**Main branch is PROTECTED.** Direct pushes to `main` are blocked by GitHub settings. All changes require pull requests.

## Branch Structure

- `main` – production (protected, deploys to prod)
- `develop` – integration / staging (default for features)

---

## 1. Cloning the Repo

```bash
git clone <REPO_URL>
cd <REPO_NAME>
git checkout develop
```

## Workflow

Start from `develop`; do not push directly to `main`.

```bash
git checkout develop
git pull origin develop
git checkout -b feature/<short-description>
# e.g.: git checkout -b feature/user-auth
```

## SQL scripts usage
The SQL scripts are provided for database setup and reporting.
- use MySQL Workbench to start the server
- use `Backend/sql scripts/coffee_shop.sql` to create database tables
- use `Backend/sql scripts/getStatusNames.sql` or similar script to create helper functions
- use `Backend/sql scripts/inprogress_orders.sql` to create a procedure for orders with `IN PROGRESS` status
- use `Backend/sql scripts/Ready_orders_view.sql` to create a view of ready orders

You can add more stored procedures, views, or helper functions as needed.

Always commit with a clear description.

Multi-station coffee kiosk backend (e.g. Cramlington, Newcastle) sharing a **single database**.
Stations are separated logically using `station_id` on orders.

---

## High‑Level Backend Flow

```text
Frontend  →  DTO  →  Controller  →  Service  →  Repository  →  DB
   ↑                        ↓           ↓            ↑
   └────────────── DTO (response)  ← Repository  ←─┘
DTOs: What the API exposes (request/response shapes, validation, no JPA annotations).

Entities: How we store data in the database (JPA mappings, relationships).

Services: Business logic (validation, station/menu checks, calling stored procedures).

controller :  a thin HTTP adapter, it accepts the request, delegates to the service, and returns the response

Repositories / SQL: Data access (Spring Data JPA + stored procedures / views).

This separation keeps the API stable while allowing us to change database details independently.
```

## Project structure
```text
Backend/
├── src
│   ├── main
│   │   ├── java
│   │   │   └── org.coffeeshop
│   │   │       ├── auth
│   │   │       │   ├── dtos
│   │   │       │   └── AuthController.java
│   │   │       ├── security
│   │   │       │   ├── SecurityConfig.java
│   │   │       │   ├── JwtAuthenticationFilter.java
│   │   │       │   └── JwtService.java
│   │   │       ├── stations
│   │   │       │   ├── controller
│   │   │       │   ├── dtos
│   │   │       │   ├── models
│   │   │       │   ├── repositories
│   │   │       │   └── services
│   │   │       └── users
│   │   │           ├── controllers
│   │   │           ├── dtos
│   │   │           ├── models
│   │   │           ├── repositories
│   │   │           └── services
│   └── resources
│       └── application.properties
└── test
    └── java
        └── org.coffeeshop
            ├── SecurityTests
            ├── StationTests
            └── UserTests



```

## resources folder
- this is where your database environment variables are stored


# Staff Workflow – CoffeeShop API

This module provides the backend workflow for managing staff in the CoffeeShop application. It exposes REST endpoints for creating staff, persists staff data using Spring Data JPA, and securely stores passwords using a `PasswordEncoder` (BCrypt). 

## Tech Stack

- Java 17+
- Spring Boot (Web, Data JPA, Security)
- Spring Security `PasswordEncoder` (BCrypt)
- JPA/Hibernate
- MySQL (or any configured relational DB)
- JUnit 5, Spring Boot Test, MockMvc for tests 

## Package Structure

- `org.coffeeshop.auth` – authentication endpoints and login DTOs
- `org.coffeeshop.security` – JWT filter, security configuration, auth provider, and password encoder
- `org.coffeeshop.users.controllers` – staff and customer REST controllers
- `org.coffeeshop.users.dtos` – request and response DTOs for users/staff
- `org.coffeeshop.users.services` – business logic for users and staff
- `org.coffeeshop.users.repositories` – JPA repositories for users and staff
- `org.coffeeshop.stations` – station APIs, models, services, and repositories
- `org.coffeeshop.SecurityTests` – integration tests for authentication and authorization
- `org.coffeeshop.UserTests.StaffTests` – staff controller integration tests
- `org.coffeeshop.StationTests` – station controller/service tests

## Current Backend Security State

- Authentication is handled via JWT.
- Login is available at `POST /api/v1/auth/login`.
- `/api/v1/staff/**` endpoints require a valid JWT and `ADMIN` role.
- `/api/v1/customers/**` is currently public.
- All other non-whitelisted routes require authentication.

## Staff Entity

Core fields (typical example):

- `id` (Long)
- `username` (unique, used for login)
- `firstName`
- `lastName`
- `role` (`staff_user` or `Admin`)
- `passwordHash` (BCrypt hash, never plain text) 

## DTO

`StaffDto` is used as the API contract:

- Request: includes `username` and `password` (plain text) for creation.
- Response: **excludes** `password`, returns other staff details and generated `id`. 

Example DTO fields:

- `id`
- `username`
- `firstName`
- `lastName`
- `role`
- `password` (request only)

## Security and Password Encoding

Passwords are encoded using Spring Security’s `PasswordEncoder`:

```java
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



