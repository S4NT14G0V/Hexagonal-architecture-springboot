Feature: Obtain User

    As a user of the system
    I want to obtain the information of all users or a user by its id
    So that I can verify that the information is correct

    Scenario Outline: Successfully Obtain all users
        Given A request to obtain all users is prepared
        When The "GET" endpoint is invoked
        Then The response status code is <statusCode>
        And The response message is "<message>"
        
        Examples:
        | statusCode | message |
        | 200 | "User obtained successfully" |

    Scenario Outline: Successfully Obtain user by id
        Given A request to obtain a user by id is prepared with id <id>
        When The "GET" endpoint is invoked
        Then The response status code is <statusCode>
        And The response message is "<message>"
        
        Examples:
        | id | statusCode | message |
        | 1 | 200 | "User obtained successfully" |

    Scenario Outline: Unsuccess Obtain user by id validation error
        Given A request to obtain a user by id is prepared with id <id>
        When The "GET" endpoint is invoked
        Then The response status code is <statusCode>
        And The response message is "<message>"

        Examples:
        | id | statusCode | message |  
        | 999 | 404 | "User not found" |

    Scenario Outline: Unsuccess Obtain all users
        Given A request to obtain all users is prepared
        When The "GET" endpoint is invoked
        Then The response status code is <statusCode>
        And The response size have to be <size> 

        Examples:
        | statusCode | size |
        | 200 | 0 |