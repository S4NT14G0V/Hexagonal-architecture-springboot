# Testing Strategy Documentation

> **Navigation:** [Architecture Overview](../architecture/architecture.md) | **Testing Strategy** | [Dependencies](dependencies.md) | [BDD](bdd.md) | [TDD](tdd.md) | [JUnit & Mockito Standards](junit-mockito-assertj.md)

This document outlines the testing strategy, methodology, and architectural alignment for the Hexagonal Architecture Spring Boot application.

---

## Table of Contents

- [Architecture Overview](../architecture/architecture.md)
- [Testing Strategy](#testing-strategy-documentation)
  - [Why Testing?](#why-testing)
  - [Double-Loop Testing (BDD + TDD)](#double-loop-testing-bdd--tdd)
  - [Testing in Hexagonal Architecture](#testing-in-hexagonal-architecture)
  - [Test Pyramid](#test-pyramid)
- [Testing Dependencies](dependencies.md)
- [BDD (Behavior-Driven Development)](bdd/bdd-strategy.md)
  - [BDD Strategy & Concepts](bdd/bdd-strategy.md)
  - [Cucumber Setup & Configuration](bdd/cucumber/setup.md)
  - **Features (Base Templates):**
    - [Create User Feature](bdd/cucumber/features/create-user.md)
    - [Get User Feature](bdd/cucumber/features/get-user.md)
    - [Update User Feature](bdd/cucumber/features/update-user.md)
    - [Delete User Feature](bdd/cucumber/features/delete-user.md)
- [TDD (Test-Driven Development)](tdd/tdd-strategy.md)
  - [TDD Strategy, Lifecycle & Layer Testing Order](tdd/tdd-strategy.md)
- [Unit Testing Standards](tdd/junit-mockito-assertj.md)
  - [JUnit 5, AssertJ & Mockito Best Practices](tdd/junit-mockito-assertj.md)

---

## Why Testing?

Testing is a core discipline in modern software engineering that ensures:
1. **Business Alignment:** Verifies that the software solves the actual business problem.
2. **Design Quality:** Writing tests first (TDD/BDD) drives cleaner, loosely-coupled modular architecture.
3. **Regression Prevention:** Allows continuous refactoring and feature additions with zero fear of breaking existing capabilities.
4. **Living Documentation:** Test scenarios serve as executable specifications that never go out of date.

---

## Double-Loop Testing (BDD + TDD)

Our methodology combines **Behavior-Driven Development (BDD)** as the outer loop and **Test-Driven Development (TDD)** as the inner loop.

```text
  ┌──────────────────────────────────────────────────────────────┐
  │  OUTER LOOP (BDD - Acceptance / End-to-End Behavior)         │
  │  1. Write failing business acceptance scenario (Gherkin)     │
  └──────────────────────────────┬───────────────────────────────┘
                                 │
                                 ▼
  ┌──────────────────────────────────────────────────────────────┐
  │  INNER LOOP (TDD - Unit / Component Design)                  │
  │  ┌────────────────────────────────────────────────────────┐  │
  │  │  a. Write failing unit test (RED)                      │  │
  │  │  b. Implement minimal code to pass (GREEN)             │  │
  │  │  c. Clean and improve design (REFACTOR)                │  │
  │  └────────────────────────────────────────────────────────┘  │
  │     (Repeat across Domain, Application, and Adapters)        │
  │                                                              │
  │  * Methodology details: docs/testing/tdd.md                  │
  │  * Code standards & Mockito: docs/testing/junit-mockito-...  │
  └──────────────────────────────┬───────────────────────────────┘
                                 │
                                 ▼
  ┌──────────────────────────────────────────────────────────────┐
  │  Outer Acceptance Scenario Passes (GREEN) -> Full Refactor   │
  └──────────────────────────────────────────────────────────────┘
```

---

## Testing in Hexagonal Architecture

Hexagonal Architecture (Ports and Adapters) makes testing straightforward by isolating business logic from external frameworks:

| Architectural Layer | Test Type | Dependencies & Tools | Execution Speed |
| :--- | :--- | :--- | :--- |
| **BDD Acceptance** | End-to-End / Feature | Cucumber, `@SpringBootTest`, REST Assured / MockMvc | Medium |
| **Domain** | Pure Unit Tests | JUnit 5, AssertJ (**No Spring, No Mocks**) | ⚡ Ultra-fast (< 10ms) |
| **Application** | Isolated Unit Tests | JUnit 5, Mockito (Mocks for Output Ports) | ⚡ Ultra-fast (< 50ms) |
| **Infrastructure (Inbound)** | Slice Integration | `@WebMvcTest`, MockMvc | Fast |
| **Infrastructure (Outbound)**| Persistence Integration| `@DataJpaTest`, H2 / Testcontainers | Fast |

---

## Test Pyramid

```text
          / \
         /   \        BDD Acceptance Tests (Cucumber / E2E)
        / BDD \       Few, high-level business scenarios
       /───────\
      / Adapter \     Integration / Slice Tests (@WebMvcTest, @DataJpaTest)
     /  Slices   \    Verifies web mappings and database queries
    /─────────────\
   /  Application  \  Application Unit Tests (Mockito)
  /     Services    \ Verifies use case orchestration and rules
 /───────────────────\
/       Domain        \ Domain Unit Tests (Pure Java / JUnit 5)
/   Entities & VO's   \ High volume, ultra-fast validation of business invariants
───────────────────────
```

---

[▲ Back to Top](#testing-strategy-documentation) | [Architecture Overview →](../architecture/architecture.md) | [TDD Strategy →](tdd.md) | [BDD Strategy →](bdd.md)