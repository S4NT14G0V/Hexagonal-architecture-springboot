# Test-Driven Development (TDD)

> **Navigation:** [← Testing Strategy](../testing-strategy.md) | [Architecture Overview](../architecture/architecture.md) | [BDD](../bdd/bdd-strategy.md) | **TDD** | [JUnit, AssertJ & Mockito Standards →](junit-mockito-assertj.md)

This document outlines the TDD methodology, principles, and layer-by-layer testing workflow across our Hexagonal Architecture.

---

## Table of Contents

1. [What is TDD?](#1-what-is-tdd)
2. [Red - Green - Refactor](#2-red---green---refactor)
3. [TDD + Hexagonal Architecture](#3-tdd--hexagonal-architecture)
4. [Inside-Out Workflow](#4-inside-out-workflow)
5. [Testing Order by Architectural Layer](#5-testing-order-by-architectural-layer)
   - [Step 1: Domain Layer](#step-1-domain-layer-pure-java--zero-frameworks)
   - [Step 2: Application Layer](#step-2-application-layer-use-cases--services)
   - [Step 3: Infrastructure Layer](#step-3-infrastructure-layer-inbound--outbound-adapters)
     - [Inbound Adapters (Web / REST Controllers)](#inbound-adapters-web--rest-controllers)
     - [Outbound Adapters (Persistence / JPA)](#outbound-adapters-persistence--jpa)

---

## 1. What is TDD?

Test-Driven Development (TDD) is a software design technique where unit tests are written **before** the implementation code. Instead of writing code and testing it retroactively, tests drive the design, API contracts, and boundary behaviors of the system.

Key benefits:

- **Clean Architecture & Loose Coupling:** Code is naturally modular and testable from day one.
- **Fail-Fast Feedback:** Bugs and regression risks are caught immediately in milliseconds.
- **Living Documentation:** Unit tests serve as exact executable specifications of how the domain and application layers behave.
- **Confidence to Refactor:** Code can be reorganized, simplified, and improved fearlessly.

---

## 2. Red - Green - Refactor

TDD is practiced in short, repetitive cycles:

```text
       ┌──────────────┐
       │   1. RED     │  Write a small, failing unit test defining the next requirement
       └──────┬───────┘
              │
              ▼
       ┌──────────────┐
       │  2. GREEN    │  Write the minimum implementation code necessary to pass
       └──────┬───────┘
              │
              ▼
       ┌──────────────┐
       │ 3. REFACTOR  │  Clean up, eliminate duplication, and improve code design
       └──────┬───────┘
              │
              └─────────► Repeat for the next requirement
```

1. **RED:** Write a test describing expected behavior (e.g. `shouldThrowInvalidUserDataException_whenEmailIsBlank`). Run it and observe it fail for the right reason.
2. **GREEN:** Write the simplest possible code to make the test pass (no premature optimization).
3. **REFACTOR:** Improve code structure, readability, and performance while keeping all tests green.

---

## 3. TDD + Hexagonal Architecture

Hexagonal Architecture (Ports & Adapters) is ideal for TDD because it strictly decouples core business logic from external frameworks, web servers, and databases.

- **Domain & Application layers** contain zero framework dependencies, allowing ultra-fast unit tests (< 5ms) without Spring context.
- **Ports (Interfaces)** act as clear boundaries, making mocking straightforward and intentional.
- **Adapters** are tested independently to ensure proper translation between external protocols and internal ports.

---

## 4. Inside-Out Workflow

We follow an **Inside-Out** testing workflow to build the application from its innermost core outwards to the infrastructure boundaries:

```text
  [1. DOMAIN LAYER]         --> Pure Java unit tests (Entities, Invariants, Value Objects)
          │
          ▼
  [2. APPLICATION LAYER]    --> Application service unit tests (Mockito for Output Ports)
          │
          ▼
  [3. INFRASTRUCTURE LAYER] --> Slice integration tests (Inbound & Outbound Adapters)
```

---

## 5. Testing Order by Architectural Layer

---

### Step 1: Domain Layer (Pure Java / Zero Frameworks)

The Domain Layer is the heart of the application. It must be completely independent of Spring, databases, or third-party libraries.

- **What to test:**
  - Entity instantiation with automatic domain identity (`UUID.randomUUID()`).
  - Business invariants and self-validations (`validateName`, `validateEmail`).
  - Encapsulated mutation methods (e.g., `user.update(name, email)`).
  - Domain exceptions (`InvalidUserDataException`).
  - Identity equality (`equals` and `hashCode` based on `UUID`).

- **Rules:**
  - ❌ **No Spring annotations**, no `@MockBean`, no database.
  - ⚡ Ultra-fast execution (< 5 milliseconds).

---

### Step 2: Application Layer (Use Cases & Services)

Application services coordinate domain entities and interact with external systems through output ports.

- **What to test:**
  - Use case execution flow (`CreateUserService`, `GetByIdUserService`, `GetAllUserService`, `UpdateUserService`).
  - Business rule verification with output ports (e.g., checking `existsByEmail` before creating or updating).
  - Correct domain instantiation/mutation before invoking output ports.
  - Application exceptions (`UserNotFoundException`, `UserAlreadyExistsException`).
  - Proper data passed to output ports (`save`, `update`, `deleteById`).

- **Rules:**
  - Mock **only** output ports (`UserRepositoryPort`) using Mockito.
  - Instantiate real domain models and command records (do not mock entities or commands).
  - Verify short-circuiting: validation errors in domain must prevent calls to output ports (`verify(port, never())`).

---

### Step 3: Infrastructure Layer (Inbound & Outbound Adapters)

Adapters translate data between the external world and the internal ports of the application.

```text
               ┌────────────────────────────────────────────────────────┐
               │                   HEXAGONAL CORE                       │
               │                                                        │
HTTP JSON ──►  │ Inbound Adapter ──► Input Port ──► Application Service │
               │                                          │             │
               │                                          ▼             │
Database  ◄──  │ Outbound Adapter ◄─ Output Port ◄────────┘             │
               │                                                        │
               └────────────────────────────────────────────────────────┘
```

---

#### Inbound Adapters (Web / REST Controllers)

- **What to test:**
  - HTTP JSON request deserialization to DTO (`UserRequest`) and translation to input port commands (`CreateUserCommand`, `UpdateUserCommand`).
  - Invocation of the corresponding **Input Port** with correct parameters.
  - Domain result translation to HTTP response DTO (`UserResponse`) and JSON serialization.
  - HTTP response status codes:
    - `201 Created` for successful resource creation.
    - `200 OK` for successful retrieval or update.
    - `204 No Content` for successful deletion.
    - `400 Bad Request` for invalid request data.
    - `404 Not Found` when the requested resource does not exist.
    - `409 Conflict` when a business conflict occurs (e.g. duplicated email).
  - Controller advice exception mapping:
    - `InvalidUserDataException` $\rightarrow$ `400 Bad Request`
    - `UserNotFoundException` $\rightarrow$ `404 Not Found`
    - `UserAlreadyExistsException` $\rightarrow$ `409 Conflict`
  - Request validation and response structure.

- **Testing approach:**
  - Use `@WebMvcTest` with `MockMvc`.
  - Mock the **Inbound/Input Ports (Use Cases)** (e.g. `@MockBean CreateUserUseCase`), **NOT** concrete application services (`CreateUserService`).
  - Verify that controllers translate HTTP requests into input port commands.
  - Verify that controller responses correctly translate application results into HTTP responses.

- **Architectural rule:**
  Inbound adapters depend on **input ports** rather than concrete application service implementations.

  ```text
  HTTP Request
       ↓
  REST Controller
       ↓
  Input Port (Use Case)
       ↓
  Application Service
  ```

  And bidirectional translation flow:

  ```text
  HTTP JSON
     ↓
  UserRequest
     ↓
  Controller
     ↓
  CreateUserUseCase (Input Port)
     ↓
  CreateUserService (Application Service)

  And on return:

  Domain User
     ↓
  Controller
     ↓
  UserResponse
     ↓
  HTTP JSON
  ```

---

#### Outbound Adapters (Persistence / JPA)

- **What to test:**
  - Mapping between JPA entities and domain models:
    - `UserJpaEntity` $\leftrightarrow$ `User`
  - Persistence adapter behavior:
    - Saving domain entities.
    - Finding users by ID.
    - Finding all users.
    - Checking whether an email already exists.
    - Updating users.
    - Deleting users.
  - Database queries and persistence behavior.
  - Database constraint handling, such as unique email constraints.
  - Correct handling of missing entities and persistence failures.

- **Testing approach:**
  - Use `@DataJpaTest` for JPA and repository integration tests.
  - Use an in-memory H2 database for fast isolated tests.
  - Use Testcontainers when database-specific behavior must be tested against the actual production database engine.
  - Verify that the persistence adapter correctly translates between domain models and JPA entities.

- **Architectural rule:**
  Outbound adapters implement **output ports** and translate between the domain model and infrastructure-specific models.

  ```text
  Application Service
       ↓
  Output Port
       ↓
  Persistence Adapter
       ↓
  JPA Repository
       ↓
  Database
  ```

  And translation flow:

  ```text
  Domain User
      ↓
  Adapter
      ↓
  JPA Entity

  and:

  JPA Entity
      ↓
  Adapter
      ↓
  Domain User
  ```

---

[▲ Back to Top](#test-driven-development-tdd) | [← Testing Strategy](../testing-strategy.md) | [JUnit, AssertJ & Mockito Standards →](junit-mockito-assertj.md)
