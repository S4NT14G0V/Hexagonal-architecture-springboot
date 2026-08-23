Feature: Create User
    As a user of the system
    I want to be able to create a new user
    So that the system can create a new user

  Scenario Outline: Create User Successfully
    Given A request to create a new user is prepared with name <name>, email <email> and password <password>
    When The "Create User" endpoint is invoked
    Then The response status code is <status>
    And The response message is <message>
    And The response body contains the created user's details

    Examples:
      | name | email           | password | status | message                   |
      | John | [EMAIL_ADDRESS] | password |    201 | User created successfully |

  Scenario Outline: Create User Validation Error
    Given A request to create a new user is prepared with name <name>, email <email> and password <password>
    When The "Create User" endpoint is invoked
    Then The response status code is <status>
    And The response message is <message>

    Examples:
      | name | email           | password | status | message                |
      | John | [EMAIL_ADDRESS] | password |    400 | User validation failed |
      | John | [EMAIL_ADDRESS] | password |    409 | User already exists    |
