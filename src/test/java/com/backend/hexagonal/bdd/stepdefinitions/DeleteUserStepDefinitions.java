package com.backend.hexagonal.bdd.stepdefinitions;

import io.cucumber.java.en.Given;

public class DeleteUserStepDefinitions {

    // @Autowired
    //private SpringCucumberContext springCucumberContext;

    //@Autowired
    //private TestContext testContext;

    @Given("^A request to delete a user is prepared with id (.+)$")
    public void aResquestToDeleteAUserIsPrepared(String id) {
    }

    // When and Then are already defined in the CommonHttpStepDefinitions context
}
