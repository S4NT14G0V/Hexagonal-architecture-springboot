package com.backend.hexagonal.bdd.stepdefinitions;

import java.util.UUID;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class UpdateUserStepDefinitions {

    // @Autowired
    // private SpringCucumberContext springCucumberContext;

    // @Autowired
    // private TestContext testContext;

    @Given("^A request to update a user is prepared with id (.*), name (.*) and email (.*)$")
    public void aRequestToUpdateAUserIsPrepared(UUID id, String name, String email) {
    }

    // When and Then are already defined in the CommonHttpStepDefinitions context
    
    @Then("The response body contains the updated user's details")
    public void theResponseBodyContainsTheUpdatedUserDetails() {
    }
    
}
