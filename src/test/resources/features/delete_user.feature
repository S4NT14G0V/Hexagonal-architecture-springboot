Feature: Delete User

    As a user of the system
    I want to be able to delete my account
    So that I can remove my personal information from the system

    Scenario Outline: Successfully Delete a User
        Given A request to delete a user is prepared with id <id>
        When The "DELETE" endpoint is invoked
        Then The response status code is <statusCode>
        And The response message is <message>

        Examples:
        | id  | statusCode | message |
        | 1   | 204        | User deleted successfully |

    Scenario Outline: Delete User Validation Error
        Given A request to delete a user is prepared with id <id>
        When The "DELETE" endpoint is invoked
        Then The response status code is <statusCode>
        And The response message is "<message>"

        Examples:
        | id  | statusCode | message |
        | 999 | 404        | User not found |
        | "invalid" | 400        | Invalid user id |
