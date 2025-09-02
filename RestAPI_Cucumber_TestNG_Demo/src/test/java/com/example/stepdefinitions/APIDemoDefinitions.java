package com.example.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.testng.Assert;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

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
}