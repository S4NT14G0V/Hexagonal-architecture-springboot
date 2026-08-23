package com.backend.hexagonal.bdd.stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CommonHttpStepDefinitions {

    @When("The {string} endpoint is invoked")
    public void theEndpointIsInvoked(String endpointName) {
    }

    @Then("The response status code is {int}")
    public void theResponseStatusCodeIs(int expectedStatusCode) {
    }

    @Then("^The response message is (.+)$")
    public void theResponseMessageIs(String expectedMessage) {
    }
}
