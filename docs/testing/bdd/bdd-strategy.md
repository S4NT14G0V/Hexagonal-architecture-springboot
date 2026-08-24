# Behavior-Driven Development (BDD)

> **Navigation:** [← Testing Strategy](../testing-strategy.md) | [Architecture Overview](../architecture/architecture.md) | **BDD** | [TDD](../tdd/tdd-strategy.md) | [JUnit & Mockito Standards](../tdd/junit-mockito-assertj.md) | [Cucumber Setup →](bdd/cucumber/setup.md)

This document outlines the BDD strategy and principles used to specify, test, and document system behavior in ubiquitous language.

---

## Table of Contents

1. [What is BDD?](#1-what-is-bdd)
2. [The BDD Workflow (Outer Loop)](#2-the-bdd-workflow-outer-loop)
3. [Gherkin Syntax Structure](#3-gherkin-syntax-structure)
4. [Best Practices for Writing Features](#4-best-practices-for-writing-features)

---

## 1. What is BDD?

Behavior-Driven Development (BDD) is an agile software development methodology that enhances collaboration between developers, QA, and business stakeholders.

Key principles:

- **Ubiquitous Language:** Scenarios are written in plain, human-readable language (Gherkin syntax) that reflects domain terminology.
- **Living Documentation:** Feature files serve as specifications and automated tests simultaneously.
- **Outcome-Oriented:** Focuses on business behavior rather than implementation details (e.g. "When a user registers with a valid email" instead of "When a POST request is sent to /api/v1/users").

---

## 2. The BDD Workflow (Outer Loop)

BDD represents the **Outer Loop** in our development cycle:

```text
1. Discover & Specify   --> Discuss business rules with domain language
2. Write Feature (.feature) -> Define Scenarios with Given / When / Then
3. Run Cucumber (RED)   --> Test fails (no code exists yet)
4. TDD Inner Loop       --> Implement Domain, Application, and Adapters via TDD
5. Re-run Cucumber (GREEN) -> Scenario passes, verifying the business feature
```

---

## 3. Gherkin Syntax Structure

Gherkin uses a structured set of keywords:

- **`Feature:`** High-level business capability or requirement being described.
- **`Rule:`** (Optional) A business rule that groups related scenarios.
- **`Scenario:`** A concrete business case or user interaction.
- **`Given:`** The initial context / preconditions of the system.
- **`When:`** The action, trigger, or event initiated by an actor.
- **`Then:`** The expected outcome or observable business consequence.
- **`And / But:`** Connectors to chain multiple preconditions or assertions.
- **`Scenario Outline:` / `Examples:`** Parameterized scenarios to test multiple inputs across a table.

---

## 4. Best Practices for Writing Features

1. **Declarative over Imperative:** Describe _what_ happens from a business standpoint, not technical step-by-step UI/HTTP clicks.
   - ❌ _Bad:_ `Given I send a POST request to "/api/v1/users" with JSON body "{...}"`
   - ✅ _Good:_ `When a new user registers with name "John Doe" and email "john@example.com"`
2. **Independent Scenarios:** Every scenario must set up its own state (`Given`) and not rely on previous scenarios.
3. **Single Responsibility:** Each scenario tests a single specific outcome or business rule.

---

[▲ Back to Top](#behavior-driven-development-bdd) | [← Testing Strategy](../testing-strategy.md) | [TDD Strategy →](../tdd/tdd-strategy.md)
