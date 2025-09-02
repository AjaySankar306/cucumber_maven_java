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