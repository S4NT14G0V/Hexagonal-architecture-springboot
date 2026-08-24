Feature: Delete User
As a user of the system
I want to be able to delete a user
So that I can remove the user's information from the system

  Scenario Outline: Successfully Delete a User
    Given A request to delete a user is prepared with id <id>
    When The "DELETE" endpoint is invoked
    Then The response status code is <statusCode>

    Examples:
      | id | statusCode |
      |  1 |        204 |

  Scenario Outline: Delete User That Does Not Exist
    Given A request to delete a user is prepared with id <id>
    When The "DELETE" endpoint is invoked
    Then The response status code is <statusCode>
    And The response message is "<message>"

    Examples:
      | id  | statusCode | message        |
      | 999 |        404 | User not found |

  Scenario Outline: Delete User With Invalid Id
    Given A request to delete a user is prepared with id <id>
    When The "DELETE" endpoint is invoked
    Then The response status code is <statusCode>
    And The response message is "<message>"

    Examples:
      | id        | statusCode | message         |
      | "invalid" |        400 | Invalid user id |
