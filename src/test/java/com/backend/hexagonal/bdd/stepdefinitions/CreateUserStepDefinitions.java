package com.backend.hexagonal.bdd.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class CreateUserStepDefinitions {

    // @Autowired
    //private SpringCucumberContext springCucumberContext;

    //@Autowired
    //private TestContext testContext;

    @Given("^A request to create a new user is prepared with name (.+), email (.+) and password (.+)$")
    public void aRequestToCreateANewUserIsPrepared(String name, String email, String password) {
    }

    // When is already defined in the CommonHttpStepDefinitions context

    @Then("The response body contains the created user's details")
    public void theResponseBodyContainsTheCreatedUserDetails() {
    }
}
