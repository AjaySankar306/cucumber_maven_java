Feature: Validation of get method

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