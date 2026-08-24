Feature: Create User
    As a user of the system
    I want to be able to create a new user
    So that I can create an account in the system

  Scenario Outline: Create User Successfully
    Given A request to create a new user is prepared with name <name> and email <email>
    When The "Create User" endpoint is invoked
    Then The response status code is <status>
    And The response message is "<message>"
    And The response body contains the created user's details

    Examples:
      | name | email            | status | message                   |
      | John | john@example.com |    201 | User created successfully |

  Scenario Outline: Create User Validation Error
    Given A request to create a new user is prepared with name <name> and email <email>
    When The "Create User" endpoint is invoked
    Then The response status code is <status>
    And The response message is "<message>"

    Examples:
      | name | email            | status | message                |
      |      | john@example.com |    400 | User validation failed |
      | John | invalid-email    |    400 | User validation failed |
      | John | john@example.com |    400 | User validation failed |

  Scenario Outline: Create User With Existing Email
    Given A user already exists with email <email>
    And A request to create a new user is prepared with name <name> and email <email>
    When The "Create User" endpoint is invoked
    Then The response status code is <status>
    And The response message is "<message>"

    Examples:
      | name | email            | status | message             |
      | John | john@example.com |    409 | User already exists |
