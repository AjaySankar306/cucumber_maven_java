Feature: Validation of API endpoints for ReqRes demo

  @GetUserDetails
  Scenario Outline: Send a valid Request to get user details
    Given I send a request to the URL to get user details
    Then the response will return status <statusCode> and id <id> and email "<employee_email>" and first name "<employee_firstname>" and last name "<employee_lastname>"

    Examples:
      | statusCode | id | employee_email         | employee_firstname | employee_lastname |
      | 200        | 2  | janet.weaver@reqres.in | Janet              | Weaver            |

  @GetUserById
  Scenario Outline: Retrieve a user by valid ID
    Given I send a GET request with user id <id>
    Then the response should return status <statusCode> and user id <id> and email "<employee_email>" and first name "<employee_firstname>" and last name "<employee_lastname>"

    Examples:
      | statusCode | id | employee_email         | employee_firstname | employee_lastname |
      | 200        | 2  | janet.weaver@reqres.in | Janet              | Weaver            |

  @GetUserInvalidId
  Scenario Outline: Retrieve a user with invalid ID
    Given I send a GET request with user id <id>
    Then the response should return status <statusCode> and an empty body

    Examples:
      | statusCode | id   |
      | 404        | 9999 |
    
  @CreateUser
  Scenario: Create a new user successfully
    Given I have user details with name "John Doe" and job "Software Engineer"
    When I send a POST request to create the user
    Then the response status should be 201
    And the response should contain the created user details

  @DeleteUser
  Scenario Outline: Delete user successfully
    Given I send a DELETE request for user id <userId>
    Then the user should be deleted successfully with status <statusCode>
    
    Examples:
      | userId | statusCode |
      | 2      | 204        |
  
  @GetAllUsers
  Scenario Outline: Retrieve all users with pagination
    Given I send a GET request to retrieve all users on page <page>
    Then the response should return status <statusCode> and contain <usersPerPage> users per page
    
    Examples:
      | page | statusCode | usersPerPage |
      | 1    | 200        | 6            |
      | 2    | 200        | 6            |


  @GetUnknownResources
  Scenario: Retrieve a list of unknown resources
    Given I send a GET request to retrieve unknown resources
    Then the response should return status 200 and contain a list of unknown resources

  @DeleteUserById
  Scenario Outline: Delete a user by ID
    Given I send a DELETE request for user id <userId>
    Then the user should be deleted successfully with status <statusCode>

    Examples:
      | userId | statusCode |
      | 3      | 204        |

  @GetUnknownResourceById
Scenario Outline: Retrieve a specific unknown resource by ID
  Given I send a GET request to retrieve unknown resource with id <resourceId>
  Then the response should return status <statusCode> and contain the details of the unknown resource with id <resourceId>

  Examples:
    | resourceId | statusCode |
    | 2          | 200        |

    @CreateUserLargePayload
  Scenario: Create a new user with a large payload
    Given I have user details with large name and job
    When I send a POST request to create the user with large payload
    Then the response status should be 201
    And the response should contain the created user details with large payload

  @UpdateUserPartial
  Scenario: Update user with partial data
    Given I send a PUT request to update user id 2 with name only
    Then the response status should be 200
    And the response should contain the updated user name and updatedAt timestamp

  @DeleteUserInvalidId
Scenario: Delete user with invalid ID
  Given I send a DELETE request for invalid user id 9999
  Then the response status should be 204
  And the response should indicate no content returned


  @HealthCheck
  Scenario: Perform a comprehensive health check
    Given I perform a health check on all endpoints
    Then all endpoints should return status 200 with valid responses

    @RegisterUser
  Scenario: Register a new user with valid email and password
    Given I have registration details with email "eve.holt@reqres.in" and password "pistol"
    When I send a POST request to register the user
    Then the registration response status should be 200
    And the response should contain a token
 
   @RegisterUserInvalid
  Scenario: Attempt to register a user with missing or invalid email/password
    Given I have registration details with missing or invalid email or password
    When I send a POST request to register the user with invalid data
    Then the registration response status should be 400 for invalid data
    And the response should indicate missing email or password


  @LoginUser
  Scenario: Login a user with valid credentials
    Given I have login details with email "eve.holt@reqres.in" and password "cityslicka"
    When I send a POST request to login the user
    Then the login response status should be 200
    And the login response should contain a token



