package com.example.stepdefinitions;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import org.testng.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

public class APIDemoDefinitions {

    private Response response;
    private static final String BASE_URL = "https://reqres.in/api";
    private static final String API_KEY = "reqres-free-v1";  // Free API key from ReqRes

    // Scenario 1: Get default user details
    @Given("I send a request to the URL to get user details")
    public void i_send_request_to_get_user_details() {
        response = given()
                .header("Accept", "application/json")
                .header("x-api-key", API_KEY)
                .when()
                .get(BASE_URL + "/users/2");

        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Body: " + response.getBody().asString());
    }

    @Then("the response will return status {int} and id {int} and email {string} and first name {string} and last name {string}")
    public void verify_user_details(int statusCode, int id, String email, String firstname, String lastname) {
        response.then().statusCode(statusCode)
                .body("data.id", equalTo(id))
                .body("data.email", equalTo(email))
                .body("data.first_name", equalTo(firstname))
                .body("data.last_name", equalTo(lastname));
    }

    // Scenario 2: GET by valid user ID
    @Given("I send a GET request with user id {int}")
    public void i_send_get_request_with_id(int id) {
        response = given()
                .header("Accept", "application/json")
                .header("x-api-key", API_KEY)
                .when()
                .get(BASE_URL + "/users/" + id);

        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Body: " + response.getBody().asString());
    }

    @Then("the response should return status {int} and user id {int} and email {string} and first name {string} and last name {string}")
    public void verify_user_by_id(int statusCode, int id, String email, String firstname, String lastname) {
        response.then().statusCode(statusCode)
                .body("data.id", equalTo(id))
                .body("data.email", equalTo(email))
                .body("data.first_name", equalTo(firstname))
                .body("data.last_name", equalTo(lastname));
    }

    // Scenario 3: Invalid user ID --> expect 404 + empty body
    @Then("the response should return status {int} and an empty body")
    public void verify_user_invalid_id(int expectedStatusCode) {
        int actualStatus = response.getStatusCode();
        String body = response.getBody().asString().trim();

        System.out.println("Expected Status: " + expectedStatusCode);
        System.out.println("Actual Status: " + actualStatus);
        System.out.println("Response Body: " + body);

        Assert.assertEquals(actualStatus, expectedStatusCode,
                "Expected status " + expectedStatusCode + " but got " + actualStatus);

        Assert.assertTrue(body.equals("{}") || body.isEmpty(),
                "Expected empty body but got: " + body);
    }

    // Scenario 4 - Create User
    @Given("I have user details with name {string} and job {string}")
    public void i_have_user_details_with_name_and_job(String name, String job) {
        System.out.println("Preparing to create user with name: " + name + " and job: " + job);
    }

    @Given("I send a POST request to create the user")
    public void i_send_post_request_to_create_user() {
        String requestBody = "{\n" +
                "    \"name\": \"John Doe\",\n" +
                "    \"job\": \"Software Engineer\"\n" +
                "}";

        response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("x-api-key", API_KEY)
                .body(requestBody)
                .when()
                .post(BASE_URL + "/users");

        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }

    @Then("the response status should be {int}")
    public void verify_response_status(int expectedStatusCode) {
        int actualStatus = response.getStatusCode();
        Assert.assertEquals(actualStatus, expectedStatusCode,
                "Expected status " + expectedStatusCode + " but got " + actualStatus);
    }

    @Then("the response should contain the created user details")
    public void verify_created_user_details() {
        response.then()
                .body("name", equalTo("John Doe"))
                .body("job", equalTo("Software Engineer"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());

        System.out.println("User created successfully with ID: " + response.jsonPath().getString("id"));
        System.out.println("Created at: " + response.jsonPath().getString("createdAt"));
    }

    // ✅ Scenario 5: DELETE user → 204 + empty body
    @Given("I send a DELETE request for user id {int}")
    public void i_send_delete_request_for_user(int userId) {
        response = given()
                .header("Accept", "application/json")
                .header("x-api-key", API_KEY)
                .when()
                .delete(BASE_URL + "/users/" + userId);

        System.out.println("DELETE Status: " + response.getStatusCode());
        System.out.println("DELETE Response: " + response.getBody().asString());
    }

    @Then("the user should be deleted successfully with status {int}")
    public void verify_user_deletion(int expectedStatusCode) {
        int actualStatus = response.getStatusCode();
        Assert.assertEquals(actualStatus, expectedStatusCode,
                "Expected status " + expectedStatusCode + " but got " + actualStatus);

        String body = response.getBody().asString().trim();
        Assert.assertTrue(body.isEmpty(), "Expected empty body but got: " + body);

        System.out.println("User deleted successfully with empty body");
    }

    // Scenario 6: Get All Users with Pagination
    @Given("I send a GET request to retrieve all users on page {int}")
    public void i_send_get_request_for_all_users_on_page(int pageNumber) {
        response = given()
                .header("Accept", "application/json")
                .header("x-api-key", API_KEY)
                .queryParam("page", pageNumber)
                .when()
                .get(BASE_URL + "/users");

        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Page requested: " + pageNumber);
    }

    @Then("the response should return status {int} and contain {int} users per page")
    public void verify_users_list_response(int statusCode, int expectedUsersPerPage) {
        response.then().statusCode(statusCode)
                .body("per_page", equalTo(expectedUsersPerPage))
                .body("data", not(empty()))
                .body("data.size()", greaterThan(0));

        System.out.println("Users returned: " + response.jsonPath().getList("data").size());
    }

        // Scenario 7 already exists above (Get All Users with Pagination)
        //scenario 8 already exists above(written in test case 5)

    // ✅ Scenario 9: GET /api/unknown
    @Given("I send a GET request to retrieve unknown resources")
    public void i_send_get_request_for_unknown_resources() {
        response = given()
                .header("Accept", "application/json")
                .when()
                .get(BASE_URL + "/unknown");

        System.out.println("GET Unknown Resources Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }

    @Then("the response should return status {int} and contain a list of unknown resources")
    public void verify_unknown_resources(int statusCode) {
        response.then().statusCode(statusCode)
                .body("data", not(empty()))
                .body("data[0].id", notNullValue())
                .body("data[0].name", notNullValue())
                .body("data[0].year", notNullValue())
                .body("data[0].color", notNullValue())
                .body("data[0].pantone_value", notNullValue());

        System.out.println("Unknown resources list validated successfully.");
    }
// ✅ Scenario 10: GET /api/unknown/{id}
@Given("I send a GET request to retrieve unknown resource with id {int}")
public void i_send_get_request_for_unknown_resource_by_id(int resourceId) {
    response = given()
            .header("Accept", "application/json")
            .when()
            .get(BASE_URL + "/unknown/" + resourceId);

    System.out.println("GET Unknown Resource Status: " + response.getStatusCode());
    System.out.println("Response Body: " + response.getBody().asString());
}

@Then("the response should return status {int} and contain the details of the unknown resource with id {int}")
public void verify_unknown_resource_details(int statusCode, int resourceId) {
    response.then().statusCode(statusCode)
            .body("data.id", equalTo(resourceId))
            .body("data.name", notNullValue())
            .body("data.year", notNullValue())
            .body("data.color", notNullValue())
            .body("data.pantone_value", notNullValue());

    System.out.println("Unknown resource with ID " + resourceId + " validated successfully.");
}
// --- (existing code above remains unchanged) ---

    // Scenario 17: Create User with Large Payload
    @Given("I have user details with large name and job")
    public void i_have_user_details_with_large_name_and_job() {
        System.out.println("Preparing large payload user details...");
    }

    @When("I send a POST request to create the user with large payload")
    public void i_send_post_request_to_create_user_with_large_payload() {
        String largeName = "John".repeat(100);  // very long string
        String largeJob = "Engineer".repeat(100);

        String requestBody = "{\n" +
                "    \"name\": \"" + largeName + "\",\n" +
                "    \"job\": \"" + largeJob + "\"\n" +
                "}";

        response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("x-api-key", API_KEY)
                .body(requestBody)
                .when()
                .post(BASE_URL + "/users");

        System.out.println("Large Payload Status: " + response.getStatusCode());
    }

    @Then("the response should contain the created user details with large payload")
    public void verify_created_user_details_large_payload() {
        response.then()
                .body("id", notNullValue())
                .body("createdAt", notNullValue());

        System.out.println("Created User ID: " + response.jsonPath().getString("id"));
        System.out.println("Created At: " + response.jsonPath().getString("createdAt"));
    }

    // Scenario 18: PUT Partial Update
    @Given("I send a PUT request to update user id {int} with name only")
    public void i_send_put_request_to_update_user_with_name_only(int id) {
        String requestBody = "{\n" +
                "    \"name\": \"UpdatedName\"\n" +
                "}";

        response = given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("x-api-key", API_KEY)
                .body(requestBody)
                .when()
                .put(BASE_URL + "/users/" + id);

        System.out.println("PUT Status: " + response.getStatusCode());
        System.out.println("PUT Response: " + response.getBody().asString());
    }

    @Then("the response should contain the updated user name and updatedAt timestamp")
    public void verify_updated_user_partial() {
        response.then()
                .body("name", equalTo("UpdatedName"))
                .body("updatedAt", notNullValue());

        System.out.println("Updated At: " + response.jsonPath().getString("updatedAt"));
    }

    // Scenario 19: DELETE with Invalid ID
    @Given("I send a DELETE request for invalid user id {int}")
    public void i_send_delete_request_for_invalid_user(int userId) {
        response = given()
                .header("Accept", "application/json")
                .header("x-api-key", API_KEY)
                .when()
                .delete(BASE_URL + "/users/" + userId);

        System.out.println("DELETE Invalid ID Status: " + response.getStatusCode());
    }

    @Then("the response should indicate no content returned")
public void verify_user_invalid_delete_behavior() {
    int actualStatus = response.getStatusCode();
    Assert.assertEquals(actualStatus, 204, "Expected 204 for invalid user deletion");

    String body = response.getBody().asString().trim();
    Assert.assertTrue(body.isEmpty(), "Expected empty body for invalid delete");
    System.out.println("DELETE Invalid User ID returned 204 with no content (ReqRes behavior).");
}

    // Scenario 20: Health Check
    @Given("I perform a health check on all endpoints")
    public void i_perform_health_check_on_all_endpoints() {
        String[] endpoints = {"/users", "/register", "/login", "/unknown"};

        for (String endpoint : endpoints) {
            Response resp = given()
                    .header("Accept", "application/json")
                    .header("x-api-key", API_KEY)
                    .when()
                    .get(BASE_URL + endpoint);

            System.out.println("Health Check - " + endpoint + " : " + resp.getStatusCode());
            Assert.assertEquals(resp.getStatusCode(), 200, "Health check failed for " + endpoint);
        }
    }

    @Then("all endpoints should return status 200 with valid responses")
    public void verify_health_check_responses() {
        System.out.println("✅ All endpoints returned 200 with valid responses");
    }
// ✅ Scenario 21: Register a new user
@Given("I have registration details with email {string} and password {string}")
public void i_have_registration_details(String email, String password) {
    System.out.println("Preparing registration details with email: " + email);
}

@When("I send a POST request to register the user")
public void i_send_post_request_to_register_user() {
    String requestBody = "{\n" +
            "    \"email\": \"eve.holt@reqres.in\",\n" +
            "    \"password\": \"pistol\"\n" +
            "}";

    response = given()
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("x-api-key", API_KEY)
            .body(requestBody)
            .when()
            .post(BASE_URL + "/register");

    System.out.println("Register Status: " + response.getStatusCode());
    System.out.println("Response Body: " + response.getBody().asString());
}

@Then("the registration response status should be {int}")
public void verify_registration_response_status(int expectedStatusCode) {
    int actualStatus = response.getStatusCode();
    Assert.assertEquals(actualStatus, expectedStatusCode,
            "Expected status " + expectedStatusCode + " but got " + actualStatus);
}

@Then("the response should contain a token")
public void verify_registration_token() {
    response.then()
            .body("token", notNullValue());

    System.out.println("User registered successfully with token: " + response.jsonPath().getString("token"));
}
// ✅ Scenario 22: Register a user with invalid data
@Given("I have registration details with missing or invalid email or password")
public void i_have_invalid_registration_details() {
    System.out.println("Preparing invalid registration details (missing or invalid email/password)...");
}

@When("I send a POST request to register the user with invalid data")
public void i_send_post_request_to_register_user_with_invalid_data() {
    String requestBody = "{\n" +
            "    \"email\": \"\", \n" +  // missing email
            "    \"password\": \"\" \n" + // missing password
            "}";

    response = given()
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("x-api-key", API_KEY)
            .body(requestBody)
            .when()
            .post(BASE_URL + "/register");

    System.out.println("Invalid Register Status: " + response.getStatusCode());
    System.out.println("Response Body: " + response.getBody().asString());
}

@Then("the registration response status should be {int} for invalid data")
public void verify_registration_invalid_response_status(int expectedStatusCode) {
    int actualStatus = response.getStatusCode();
    Assert.assertEquals(actualStatus, expectedStatusCode,
            "Expected status " + expectedStatusCode + " but got " + actualStatus);
}

@Then("the response should indicate missing email or password")
public void verify_registration_invalid_response_message() {
    response.then()
            .body("error", notNullValue());

    System.out.println("Error message for invalid registration: " + response.jsonPath().getString("error"));
}
// ✅ Scenario 13: Login a user with valid credentials
@Given("I have login details with email {string} and password {string}")
public void i_have_login_details(String email, String password) {
    System.out.println("Preparing login details with email: " + email);
}

@When("I send a POST request to login the user")
public void i_send_post_request_to_login_user() {
    String requestBody = "{\n" +
            "    \"email\": \"eve.holt@reqres.in\",\n" +
            "    \"password\": \"cityslicka\"\n" +
            "}";

    response = given()
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("x-api-key", API_KEY)
            .body(requestBody)
            .when()
            .post(BASE_URL + "/login");

    System.out.println("Login Status: " + response.getStatusCode());
    System.out.println("Response Body: " + response.getBody().asString());
}

@Then("the login response status should be {int}")
public void verify_login_response_status(int expectedStatusCode) {
    int actualStatus = response.getStatusCode();
    Assert.assertEquals(actualStatus, expectedStatusCode,
            "Expected status " + expectedStatusCode + " but got " + actualStatus);
}

@Then("the login response should contain a token")
public void verify_login_token() {
    response.then()
            .body("token", notNullValue());

    System.out.println("Login successful with token: " + response.jsonPath().getString("token"));
}


@Then("the response should return an invalid login error")
public void verify_login_invalid_response() {
    int status = response.getStatusCode();
    Assert.assertTrue(status == 400 || status == 401,
            "Expected status 400 or 401 but got " + status);

    String errorMessage = response.getBody().asString();
    System.out.println("Invalid login response: " + errorMessage);
}




}