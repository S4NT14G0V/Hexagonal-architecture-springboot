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

    @Given("^A request to obtain a user by id is prepared with id (.+)$")
    public void aRequestToObtainAUserByIdIsPrepared(Long id) {
    }
    
    @Then("^The response size have to be (.+)$")
    public void theResponseSizeHaveToBe(Integer size) {
    }

    // When and Then are already defined in the CommonHttpStepDefinitions context   
}
