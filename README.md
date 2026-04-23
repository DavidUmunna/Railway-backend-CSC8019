# CSC8019

# Contributing & Git Workflow

**Main branch is PROTECTED.** Direct pushes to `main` are **blocked** by GitHub settings. All changes require pull requests.

## Branch Structure

- `main`  – production (protected, deploys to prod)
- `develop` – integration / staging (default for features)

---

## 1. Cloning the Repo

```bash
git clone <REPO_URL>
cd <REPO_NAME>
git checkout develop
```

##  Workflow

start from development do not push to main 
```bash

git checkout development
git pull origin development
```


create feature branch when adding anew feature to the app
```bash
git checkout -b feature/<short-description>
# e.g.: git checkout -b feature/user-auth


```

<<<<<<< HEAD
## sql scripts usage
the sql scripts are going to be used to connect to our backend java application and  get data from the database
- use mysql workbench to create start server
- use [coffee_shop.sql](Backend/sql%20scripts) to create the database tables on your machine
- use [getallorders.sql](Backend/sql%20scripts) to view all orders
- use [getOrderById.sql](Backend/sql%20scripts) to retrieve a specific order
- use [inprogress_orders.sql](Backend/sql%20scripts) to create a procedure for getting orders with 'in progress' status
- use [Ready_orders_view.sql](Backend/sql%20scripts) to create a tabular view of all ready orders

You can add more functions and procedures that are relevant and useful.

---

## Always commit with description

Multi‑station coffee kiosk backend (e.g. Cramlington, Newcastle) sharing a **single database**.  
Stations are separated logically using `station_id` on orders.

---

## High‑Level Backend Flow

```text
Frontend  →  DTO  →  Controller  →  Service  →  Entity/SQL  →  DB
   ↑                        ↓           ↓            ↑
   └────────────── DTO (response)  ←  Entity/SQL  ←─┘
```

DTOs: What the API exposes (request/response shapes, validation, no JPA annotations).

Entities: How we store data in the database (JPA mappings, relationships).

Services: Business logic (validation, station/menu checks, calling stored procedures).

Controller: A thin HTTP adapter that accepts the request, delegates to the service, and returns the response.

Repositories / SQL: Data access (Spring Data JPA + stored procedures / views).

This separation keeps the API stable while allowing us to change database details independently.

---

## Project Structure

```text 
├───Backend
│   ├───sql scripts
│   │   ├───coffee_shop.sql
│   │   ├───getallorders.sql
│   │   ├───getOrderById.sql
│   │   ├───inprogress_orders.sql
│   │   └───Ready_orders_view.sql
│   └───src
│       ├────main
│       │   ├───java
│       │   │   └───org
│       │   │       └───coffeeshop
│       │   │           ├───auth
│       │   │           ├───purchaseorders
│       │   │           ├───security
│       │   │           ├───stations
│       │   │           └───users
│       │   └───resources
│       │       └───application.properties
│       └───test
├───Frontend
│   ├───src
│   │   ├───components
│   │   ├───views
│   │   └───store
│   └───package.json
└───README.md
```

## Backend - Resources Folder

The `Backend/src/main/resources/` folder contains database environment variables and configuration stored in `application.properties`.

---

## Staff Workflow – CoffeeShop API

This module provides the backend workflow for managing staff in the CoffeeShop application. It exposes REST endpoints for creating staff, persists staff data using Spring Data JPA, and securely stores passwords using `PasswordEncoder` (BCrypt).

### Tech Stack

- Java 17+
- Spring Boot (Web, Data JPA, Security)
- Spring Security `PasswordEncoder` (BCrypt)
- JPA/Hibernate
- MySQL (or any configured relational DB)
- JUnit 5, Spring Boot Test, MockMvc for tests

### Package Structure

- `org.coffeeshop.staff.model` – `Staff` JPA entity
- `org.coffeeshop.staff.dto` – `StaffDto` used for requests/responses
- `org.coffeeshop.staff.repository` – `StaffRepository` (extends `JpaRepository`)
- `org.coffeeshop.staff.service` – `StaffService` with business logic
- `org.coffeeshop.staff.controller` – `StaffController` REST endpoints
- `org.coffeeshop.security` – `SecurityConfig` with `PasswordEncoder` bean

### Staff Entity

Core fields (typical example):

- `id` (Long)
- `email` (unique, used as username for login)
- `firstName`
- `lastName`
- `role` (e.g. `BARISTA`, `MANAGER`)
- `active` (boolean)
- `passwordHash` (BCrypt hash, never plain text)

### DTO

`StaffDto` is used as the API contract:

- Request: includes `email` (acts as username) and `password` (plain text) for creation.
- Response: **excludes** `password`, returns other staff details and generated `id`.

Example DTO fields:

- `id`
- `email`
- `firstName`
- `lastName`
- `role`
- `active`
- `password` (request only)

### Security and Password Encoding

Passwords are encoded using Spring Security's `PasswordEncoder`:

```java
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```


Always commit with description

- `id`
- `email`
- `firstName`
- `lastName`
- `role`
- `active`
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
=======
Always commit with description

>>>>>>> 176b9e4ccc70ffd53bf6fda6ab060cb2ba608fe4



