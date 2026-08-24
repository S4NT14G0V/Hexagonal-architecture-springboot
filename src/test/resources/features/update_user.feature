Feature: Update User
    As a user of the system
    I want to be able to update an existing user
    So that the system can update an existing user

  Scenario Outline: Update User Successfully
    Given A request to update a user is prepared with id <id>, name <name> and email <email>
    When The "Update User" endpoint is invoked
    Then The response status code is <status>
    And The response message is <message>
    And The response body contains the updated user's details

    Examples:
      | id | name | email            | status | message                   |
      |  1 | John | john@example.com |    200 | User updated successfully |

  Scenario Outline: Update User Validation Error
    Given A request to update a user is prepared with id <id>, name <name> and email <email>
    When The "Update User" endpoint is invoked
    Then The response status code is <status>
    And The response message is <message>

    Examples:
      | id | name | email          | status | message                |
      |  1 |      | john@email.com |    400 | User validation failed |
      |  1 | John | invalid-email  |    400 | User validation failed |

  Scenario Outline: Update User Not Found
    Given A request to update a user is prepared with id <id>, name <name> and email <email>
    When The "Update User" endpoint is invoked
    Then The response status code is <status>
    And The response message is <message>

    Examples:
      | id  | name | email            | status | message        |
      | 999 | John | john@example.com |    404 | User not found |
