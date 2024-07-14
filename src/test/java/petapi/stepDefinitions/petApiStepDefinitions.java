package petapi.stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import petapi.setData.petAPI;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class petApiStepDefinitions extends BaseTest{

    private petAPI PetApi;
    private Response response;
    private int petID;

    public petApiStepDefinitions(){
        System.out.println("Loading the petApiStetDefinition");
        initBaseTest();
    }

    @Given("I have a new pet with name {string}, age {int}, avatarUrl {string} and category {string}")
    public void iHaveANewPetWithNameAgeAvatarUrlAndCategory(String name, int age, String avatarUrl, String category) {
        initializeData(name, age, avatarUrl, category);
    }

    @Given("I have a new pet with name {string} and age {int}")
    public void iHaveANewPetWithNameAndAge(String name, int age) {
        initializeData(name, age, null, null);
    }

    @When("I add the pet")
    public void iAddThePet() {
        System.out.println("Base URI: " + RestAssured.baseURI);
        System.out.println("Port: " + RestAssured.port);
        System.out.println("Base Path: " + RestAssured.basePath);
        response = given()
                .contentType(ContentType.JSON)
                .body(PetApi)
                .when()
                .post("/pet");
    }

    @Then("the pet is added successfully")
    public void thePetIsAddedSuccessfully() {
        response.then().statusCode(201);
        Integer id = response.jsonPath().getInt("id");
        assertNotNull(id, "Id returned should not be null");
    }

    @Given("a pet exists with ID {int}")
    public void aPetExistsWithID(int id) {
        this.petID = id;
    }

    @When("I retrieve the pet by ID")
    public void iRetrieveThePetByID() {
        response = given()
                .pathParam("petID", petID)
                .when()
                .get("/pet/{petID}");
    }

    @Then("the pet details are returned")
    public void thePetDetailsAreReturned() {
        response.then().statusCode(201);
        petAPI responsePet = response.as(petAPI.class);
        System.out.println("Response value "+responsePet);

        assertNotNull(responsePet, "ResponsePet object is null");

        assertEquals(PetApi.getName(), responsePet.getName(), "Name mismatch");
        assertEquals(PetApi.getAge(), responsePet.getAge(), "Age mismatch");
        assertEquals(PetApi.getAvatarUrl(), responsePet.getAvatarUrl(), "Avatar URL mismatch");
        assertEquals(PetApi.getCategory(), responsePet.getCategory(), "Category mismatch");
    }

    @When("I update the pet with name {string}, age {int}, avatarUrl {string} and category {string}")
    public void iUpdateThePetWithNameAgeAvatarUrlAndCategory(String name, int age, String avatarUrl, String category) {
        initializeData(name, age, avatarUrl, category);

        response = given()
                .pathParam("petId", petID)
                .contentType(ContentType.JSON)
                .body(PetApi)
                .when()
                .put("/pet/{petId}");
    }

    @Then("the pet is updated successfully")
    public void thePetIsUpdatedSuccessfully() {
        response.then().statusCode(200);
    }

    @When("I delete the pet")
    public void iDeleteThePet() {
        response = given()
                .pathParam("petId", petID)
                .when()
                .delete("/pet/{petId}");
    }

    @Then("the pet is deleted successfully")
    public void thePetIsDeletedSuccessfully() {
        response.then().statusCode(200);
    }

    @Given("I have a new pet with name {string}")
    public void iHaveANewPetWithName(String name) {
        initializeData(name, 0, null, null);
    }

    @Then("the pet is not updated")
    public void thePetIsNotUpdated() {
        response.then().statusCode(400);
    }

    @Then("the pet is not deleted")
    public void thePetIsNotDeleted() {
        response.then().statusCode(400);
    }

    @Then("the pet is not retrieved")
    public void thePetIsNotRetrieved() {
        response.then().statusCode(400);
    }

    @Then("a validation error occurs")
    public void aValidationErrorOccurs() {
        response.then().statusCode(422);
    }

    // Function to initialize test data values
    private void initializeData(String name, int age, String avatarUrl, String category) {
        PetApi = new petAPI();
        PetApi.setName(name);
        PetApi.setAge(age);
        PetApi.setAvatarUrl(avatarUrl);
        PetApi.setCategory(category);
    }
}
