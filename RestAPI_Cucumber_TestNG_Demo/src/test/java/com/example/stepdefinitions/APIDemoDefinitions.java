package com.example.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class APIDemoDefinitions {
    private ValidatableResponse validatableResponse;
    private String endpoint = "https://reqres.in/api/users/2";

    @Given("I send a request to the URL to get user details")
    public void sendRequest(){
        validatableResponse = given().contentType(ContentType.JSON)
                .when().get(endpoint).then();

        System.out.println("Response :"+validatableResponse.extract().asPrettyString());
    }
    @Then("the response will return status {int} and id {int} and email {string} and first name {string} and last name {string}")
    public void verifyStatus(int expectedStatusCode, int expectedId, String expectedEmail, String expectedFirstName, String expectedLastName){

        validatableResponse.assertThat().statusCode(expectedStatusCode).body("data.id",equalTo(expectedId)).and()
                .body("data.email",equalTo(expectedEmail)).body("data.first_name",equalTo(expectedFirstName))
                .body("data.last_name",equalTo(expectedLastName));

      /*  validatableResponse.assertThat().statusCode(expectedStatusCode);

        validatableResponse.assertThat().body("data.id",equalTo(expectedId));

        validatableResponse.assertThat().body("data.email",equalTo(expectedEmail));

        validatableResponse.assertThat().body("data.first_name",equalTo(expectedFirstName));

        validatableResponse.assertThat().body("data.last_name",equalTo(expectedLastName)); */

    }
}