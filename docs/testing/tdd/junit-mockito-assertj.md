# Unit Testing Guidelines: JUnit 5, AssertJ & Mockito

> **Navigation:** [← TDD](tdd-strategy.md) | [Testing Strategy](../testing-strategy.md) | [Architecture Overview](../architecture/architecture.md) | [BDD](../bdd/bdd-strategy.md) | **JUnit & Mockito Standards**

This document establishes coding standards, conventions, and implementation examples for unit and component testing using JUnit 5, AssertJ, and Mockito.

---

## Table of Contents

1. [Naming Conventions](#1-naming-conventions)
2. [Test Structure: Arrange - Act - Assert (AAA)](#2-test-structure-arrange---act---assert-aaa)
3. [Fluent Assertions with AssertJ](#3-fluent-assertions-with-assertj)
4. [Parameterized Tests with @ParameterizedTest](#4-parameterized-tests-with-parameterizedtest)
5. [Mockito in Application Service Tests](#5-mockito-in-application-service-tests)
6. [ArgumentCaptor for Inspecting Port Invocations](#6-argumentcaptor-for-inspecting-port-invocations)
7. [Verifications (verify, never, verifyNoMoreInteractions)](#7-verifications-verify-never-verifynomoreinteractions)
8. [Nested Tests with @Nested](#8-nested-tests-with-nested)
9. [What to Mock](#9-what-to-mock)
10. [What NOT to Mock](#10-what-not-to-mock)

---

## 1. Naming Conventions

Use descriptive and standardized test method names that explicitly define the expected behavior and precondition:

```text
should[ExpectedBehavior]_when[StateOrCondition]()
```

**Examples:**

- `shouldCreateUser_whenAllParametersAreValid()`
- `shouldThrowInvalidUserDataException_whenEmailFormatIsInvalid()`
- `shouldThrowUserNotFoundException_whenUserDoesNotExist()`
- `shouldUpdateUser_whenEmailRemainsUnchanged()`
- `shouldReturnEmptyList_whenNoUsersExist()`

Use `@DisplayName` on both test classes and methods to generate human-readable test reports:

```java
@Test
@DisplayName("Should throw UserAlreadyExistsException when new email already exists")
void shouldThrowUserAlreadyExistsException_whenNewEmailAlreadyExists() {
    // ...
}
```

---

## 2. Test Structure: Arrange - Act - Assert (AAA)

Every test method should follow the clear 3-phase structure (Given - When - Then):

```java
@Test
@DisplayName("Should create user with automatic UUID when parameters are valid")
void shouldCreateUser_whenValid() {
    // Arrange (Given)
    String name = "John Doe";
    String email = "john.doe@email.com";

    // Act (When)
    User user = new User(name, email);

    // Assert (Then)
    assertThat(user).isNotNull();
    assertThat(user.getId()).isNotNull();
    assertThat(user.getId()).isInstanceOf(UUID.class);
    assertThat(user.getName()).isEqualTo(name);
    assertThat(user.getEmail()).isEqualTo(email);
}
```

---

## 3. Fluent Assertions with AssertJ

Prefer **AssertJ** (`assertThat`, `assertThatThrownBy`) over standard JUnit assertions for high readability and expressive failure diagnostics:

```java
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Object equality and properties
assertThat(createdUser).isNotNull();
assertThat(createdUser.getId()).isEqualTo(savedUser.getId());
assertThat(createdUser.getEmail()).isEqualTo("john.doe@email.com");

// Collections
assertThat(result)
    .isNotNull()
    .hasSize(2)
    .containsExactlyInAnyOrder(userOne, userTwo);

// Exceptions & Messages
assertThatThrownBy(() -> new User(invalidName, "john.doe@email.com"))
    .isInstanceOf(InvalidUserDataException.class)
    .hasMessage("User name cannot be null or empty");

assertThatThrownBy(() -> getByIdUserService.execute(missingId))
    .isInstanceOf(UserNotFoundException.class)
    .hasMessage("User with id " + missingId + " not found");
```

---

## 4. Parameterized Tests with `@ParameterizedTest`

Use parameterized tests to test multiple boundary values and corner cases without duplicating test logic:

```java
@ParameterizedTest
@NullSource
@ValueSource(strings = { "", "   ", "\t", "\n" })
@DisplayName("Should throw InvalidUserDataException when name is null, empty or blank")
void shouldThrowException_whenNameIsInvalid(String invalidName) {
    assertThatThrownBy(() -> new User(invalidName, "john.doe@email.com"))
            .isInstanceOf(InvalidUserDataException.class)
            .hasMessage("User name cannot be null or empty");
}

@ParameterizedTest
@ValueSource(strings = { "invalid-email", "john@", "@example.com", "john@com", "john.doe" })
@DisplayName("Should throw InvalidUserDataException when email format is invalid")
void shouldThrowException_whenEmailFormatIsInvalid(String invalidEmail) {
    assertThatThrownBy(() -> new User("John Doe", invalidEmail))
            .isInstanceOf(InvalidUserDataException.class)
            .hasMessage("User email format is invalid");
}
```

---

## 5. Mockito in Application Service Tests

Use Mockito (`@Mock`, `@InjectMocks`, `@ExtendWith(MockitoExtension.class)`) to isolate application use cases from external I/O ports:

```java
@ExtendWith(MockitoExtension.class)
@DisplayName("GetByIdUserService")
class GetByIdUserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private GetByIdUserService getByIdUserService;

    @Test
    @DisplayName("Should return user when exists")
    void shouldReturnUser_whenExists() {
        // Arrange
        User user = new User("John Doe", "john.doe@email.com");
        UUID id = user.getId();

        when(userRepositoryPort.findById(id)).thenReturn(Optional.of(user));

        // Act
        User result = getByIdUserService.execute(id);

        // Assert
        assertThat(result).isEqualTo(user);
    }
}
```

---

## 6. ArgumentCaptor for Inspecting Port Invocations

Use `ArgumentCaptor` to inspect and assert the exact state of objects passed to output ports:

```java
@Test
@DisplayName("Should pass updated user to repository port")
void shouldUpdateUser_whenDataIsValid() {
    // Arrange
    User existingUser = new User("John Doe", "john.doe@email.com");
    UUID userId = existingUser.getId();
    UpdateUserUseCase.UpdateUserCommand command =
            new UpdateUserUseCase.UpdateUserCommand("Jane Doe", "jane.doe@email.com");

    when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(existingUser));
    when(userRepositoryPort.existsByEmail(command.email())).thenReturn(false);
    when(userRepositoryPort.update(eq(userId), any(User.class)))
            .thenAnswer(invocation -> invocation.getArgument(1));

    // Act
    updateUserService.execute(userId, command);

    // Assert with ArgumentCaptor
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepositoryPort).update(eq(userId), userCaptor.capture());

    User capturedUser = userCaptor.getValue();
    assertThat(capturedUser.getName()).isEqualTo("Jane Doe");
    assertThat(capturedUser.getEmail()).isEqualTo("jane.doe@email.com");
}
```

---

## 7. Verifications (`verify`, `never`, `verifyNoMoreInteractions`)

Assert that interactions with ports strictly follow the expected business paths:

```java
// Assert that a method was called with exact arguments
verify(userRepositoryPort).findById(userId);

// Assert that a method was NEVER called (e.g. short-circuiting on validation errors)
verify(userRepositoryPort, never()).existsByEmail(any());
verify(userRepositoryPort, never()).save(any());

// Assert no further unexpected interactions occurred with the mock
verifyNoMoreInteractions(userRepositoryPort);
```

---

## 8. Nested Tests with `@Nested`

Group related test scenarios logically inside the same test class to improve structure and readability:

```java
@DisplayName("CreateUserService")
class CreateUserServiceTest {

    @Nested
    @DisplayName("Successful Creation")
    class SuccessTests {
        @Test
        @DisplayName("Should create and return user when email is unique")
        void shouldCreateAndReturnUser_whenEmailIsUnique() { ... }
    }

    @Nested
    @DisplayName("Validation Errors")
    class ValidationTests {
        @Test
        @DisplayName("Should throw InvalidUserDataException when name is null")
        void shouldThrowInvalidUserDataException_whenNameIsNull() { ... }
    }

    @Nested
    @DisplayName("Business Rules")
    class BusinessRuleTests {
        @Test
        @DisplayName("Should throw UserAlreadyExistsException when email exists")
        void shouldThrowUserAlreadyExistsException_whenEmailExists() { ... }
    }
}
```

---

## 9. What to Mock

- **Output Ports:** `UserRepositoryPort`, external APIs, event publishers, notification senders.
- **Input Ports (only in Web Slice tests):** When testing Controllers with `@WebMvcTest`, mock the Use Case interfaces (`CreateUserUseCase`, etc.).
- **Clock / Time providers:** If testing time-dependent logic, mock the system clock or time provider.

---

## 10. What NOT to Mock

- ❌ **Domain Entities & Value Objects:** Use real instances (e.g., `new User(...)`). Domain models should be simple, pure Java objects with real business invariant execution.
- ❌ **Input Commands & DTOs:** Use real Java records or POJOs (e.g., `new CreateUserCommand(...)`).
- ❌ **Pure Utility Classes:** Let real Java utility functions execute.
- ❌ **Concrete Application Services inside Controller tests:** Always mock the Inbound Port (interface), never the service implementation.

---

[▲ Back to Top](#unit-testing-guidelines-junit-5-assertj--mockito) | [← TDD](tdd-strategy.md) | [Testing Strategy](../testing-strategy.md) | [BDD →](../bdd/bdd-strategy.md)
