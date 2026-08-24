# Cucumber Setup with Spring Boot

This document describes how Cucumber is configured and integrated with Spring Boot in this project using the JUnit 5 Platform Suite.

---

## 1. Directory Structure for BDD

```text
src/test/
├── resources/
│   ├── cucumber.properties                     <-- Cucumber configuration flags
│   └── features/                               <-- Gherkin feature files
│       ├── create-user.feature
│       ├── get-user.feature
│       ├── update-user.feature
│       └── delete-user.feature
│
└── java/com/backend/hexagonal/bdd/
    ├── CucumberTestRunner.java                 <-- JUnit 5 Suite Runner
    ├── SpringCucumberContext.java              <-- Spring Boot Test Context Bridge
    ├── TestContext.java                        <-- Scenario State / Context Holder
    └── stepdefinitions/                        <-- Step implementation classes (split by feature)
        ├── CommonHttpStepDefinitions.java      <-- Reusable HTTP status & message steps
        ├── CreateUserStepDefinitions.java      <-- Create User steps
        ├── GetUserStepDefinitions.java         <-- Get User steps
        ├── UpdateUserStepDefinitions.java      <-- Update User steps
        └── DeleteUserStepDefinitions.java      <-- Delete User steps
```

---

## 2. Configuration Files

### `src/test/resources/cucumber.properties`
```properties
cucumber.publish.quiet=true
cucumber.plugin=pretty, html:build/reports/cucumber/cucumber-report.html
cucumber.glue=com.backend.hexagonal.bdd
cucumber.features=src/test/resources/features
```

---

## 3. Runner & Spring Context Classes

### `CucumberTestRunner.java` (JUnit 5 Platform Suite)
```java
package com.backend.hexagonal.bdd;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.backend.hexagonal.bdd")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:build/reports/cucumber/report.html")
public class CucumberTestRunner {
}
```

### `SpringCucumberContext.java` (Context Bridge)
```java
package com.backend.hexagonal.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringCucumberContext {

    @LocalServerPort
    protected int port;
}
```

---

## 4. Running Cucumber Tests

To run the BDD suite from the command line:

```bash
# Run all tests (including Cucumber scenarios)
./gradlew test

# Generate and view the Cucumber HTML report in:
# build/reports/cucumber/cucumber-report.html
```
