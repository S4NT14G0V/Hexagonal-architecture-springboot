package com.backend.hexagonal.bdd.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class GetUserStepDefinitions {

    // @Autowired
    // private SpringCucumberContext springCucumberContext;

    // @Autowired
    // private TestContext testContext;

    @Given("A request to obtain all users is prepared")
    public void aRequestToObtainAllUsersIsPrepared() {
    }

    @Then("The response body contains the users")
    public void theResponseBodyContainsTheUsers() {
    }

    @Given("^A request to obtain a user by id is prepared with id (.+)$")
    public void aRequestToObtainAUserByIdIsPrepared(Long id) {
    }

    @Then("The response body contains the user's details")
    public void theResponseBodyContainsTheUsersDetails() {
    }
    
    @Then("^The response size is (.+)$")
    public void theResponseSizeIs(Integer size) {
    }

    // When and Then are already defined in the CommonHttpStepDefinitions context   
}
