# Testing Dependencies & Configuration

> **Navigation:** [← Testing Strategy](testing-strategy.md) | [Architecture Overview](../architecture/architecture.md) | **Dependencies** | [BDD](bdd/bdd-strategy.md) | [TDD](tdd/tdd-strategy.md) | [JUnit & Mockito Standards](tdd/junit-mockito-assertj.md)

This document lists the dependencies and build configurations required to support unit, integration, and BDD acceptance testing in this project.

---

## Table of Contents

1. [Gradle Dependencies (`build.gradle`)](#1-gradle-dependencies-buildgradle)
2. [Dependency Breakdown](#2-dependency-breakdown)
3. [Gradle Test Task Configuration](#3-gradle-test-task-configuration)

---

## 1. Gradle Dependencies (`build.gradle`)

```groovy
dependencies {
    // Spring Boot Test Starters
    testImplementation 'org.springframework.boot:spring-boot-starter-data-jpa-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-validation-test'
    testImplementation 'org.springframework.boot:spring-boot-starter-webmvc-test'

    // Cucumber (BDD)
    testImplementation 'io.cucumber:cucumber-java:7.27.0'
    testImplementation 'io.cucumber:cucumber-junit-platform-engine:7.27.0'
    testImplementation 'io.cucumber:cucumber-spring:7.27.0'

    // JUnit Platform Launcher (for running tests in modern IDEs & Gradle)
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

---

## 2. Dependency Breakdown

### **JUnit 5 (Jupiter Engine)**
- **Role:** The core testing platform for Java 21.
- **Key Modules:**
  - `junit-jupiter-api`: Annotations (`@Test`, `@BeforeEach`, `@DisplayName`, `@Nested`, `@ParameterizedTest`).
  - `junit-jupiter-engine`: Runtime test execution engine.
  - `junit-platform-suite`: Allows grouping and executing suites (e.g. Cucumber BDD suite).

### **AssertJ (`org.assertj:assertj-core`)**
- **Role:** Fluent assertion library included transitively with Spring Boot test starters.
- **Usage:** Provides readable assertions like `assertThat(user.getEmail()).isEqualTo("test@example.com")` and `assertThatThrownBy(...)`.

### **Mockito (`org.mockito:mockito-core` & `mockito-junit-jupiter`)**
- **Role:** Mocking framework for isolating the Application Layer from Output Ports (repositories/external services).
- **Usage:** `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks`, `when()`, `verify()`.

### **Cucumber (`io.cucumber`)**
- **`cucumber-java`:** Provides step definition annotations (`@Given`, `@When`, `@Then`, `@And`).
- **`cucumber-junit-platform-engine`:** Enables running Gherkin `.feature` files directly via standard JUnit 5 test runners (`gradle test`).
- **`cucumber-spring`:** Integrates Cucumber step definitions with Spring's dependency injection container (`@SpringBootTest`).

---

## 3. Gradle Test Task Configuration

To ensure JUnit 5 and Cucumber tests run smoothly, the test task in `build.gradle` is configured as:

```groovy
tasks.named('test') {
    useJUnitPlatform()
    systemProperty("cucumber.publish.quiet", "true")
    testLogging {
        events "passed", "skipped", "failed"
    }
}
```

---

[▲ Back to Top](#testing-dependencies--configuration) | [← Testing Strategy](testing-strategy.md) | [TDD Strategy →](tdd/tdd-strategy.md)
