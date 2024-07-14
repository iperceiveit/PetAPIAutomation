package petapi.stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import petapi.setData.petAPI;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class petApiStepDefinitions extends BaseTest{

    private petAPI PetApi;
    private Response response;
    private static int AddedPetID;

    String validResponse = "{\n" +
            "    \"name\": \"Buddy Updated\",\n" +
            "    \"age\": 4,\n" +
            "    \"avatarUrl\": \"http://example.com/buddy_updated.jpg\",\n" +
            "    \"category\": \"Dog\"\n" +
            "}";

    public petApiStepDefinitions(){
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
        response = given()
                .contentType(ContentType.JSON)
                .body(PetApi)
                .when()
                .post("/pet");
    }

    @Then("the pet is added successfully")
    public void thePetIsAddedSuccessfully() {
        response.then().statusCode(201);
        AddedPetID = response.jsonPath().getInt("id");
        assertNotNull("Id returned should not be null",AddedPetID);
    }


    @When("I retrieve the pet by ID")
    public void iRetrieveThePetByID() {
        response = given()
                .pathParam("petID", AddedPetID)
                .when()
                .get("/pet/{petID}");
    }

    @Then("the pet details are returned")
    public void thePetDetailsAreReturned() {
        response.then().statusCode(200);
    }

    @When("I update the pet with name {string}, age {int}, avatarUrl {string} and category {string}")
    public void iUpdateThePetWithNameAgeAvatarUrlAndCategory(String name, int age, String avatarUrl, String category) {
                initializeData(name, age, avatarUrl, category);

        if(response.statusCode() == 201){
            response = given()
                    .pathParam("petId", AddedPetID)
                    .contentType(ContentType.JSON)
                    .body(PetApi)
                    .when()
                    .put("/pet/{petId}");
        }
    }

    @When("Send a update request with out Age parameter")
    public void SendaupdaterequestwithoutAgeparameter() {
        String petNoAge = "{\n" +
                "    \"name\": \"Trev\",\n" +
                "    \"avatarUrl\": \"www.verifypapu.com\",\n" +
                "    \"category\": \"cat\"\n" +
                "}";

        if(response.statusCode() == 201){
            response = given()
                    .pathParam("petId", AddedPetID)
                    .contentType(ContentType.JSON)
                    .body(petNoAge)
                    .when()
                    .put("/pet/{petId}");
        }
    }

    @Then("the pet is updated successfully")
    public void thePetIsUpdatedSuccessfully() {
        response.then().statusCode(200);
    }

    @When("I delete the pet")
    public void iDeleteThePet() {
        if(response.statusCode() == 201) {
            String reqForDelete = response.getBody().prettyPrint();
            response = given()
                    .pathParam("petId", AddedPetID)
                    .header("Content-Type", "application/json")
                    .body(reqForDelete)
                    .when()
                    .post("/pet/{petId}/remove");
        }
    }

    @Then("the pet is deleted successfully")
    public void thePetIsDeletedSuccessfully() {
        response.then().statusCode(200);
    }

    @Given("I have a new pet with out name")
    public void iHaveANewPetWithOutMandatoryDetails() {
        String petNoName = "{\n" +
                "    \"age\": -1,\n" +
                "    \"avatarUrl\": \"www.verifypapu.com\",\n" +
                "    \"category\": \"cat\"\n" +
                "}";
    }

    @When("I {string} the pet")
    public void iPerformOperationOnThePet(String operation) {
        int invalidID = 39484;
        String petNoName = "{\n" +
                "    \"age\": -1,\n" +
                "    \"avatarUrl\": \"www.verifypapu.com\",\n" +
                "    \"category\": \"cat\"\n" +
                "}";
        switch (operation) {
            case "add":
                response = given()
                        .contentType(ContentType.JSON)
                        .body(petNoName)
                        .when()
                        .post("/pet");
            case "update":
                response = given()
                        .pathParam("petId", AddedPetID)
                        .contentType(ContentType.JSON)
                        .body(petNoName)
                        .when()
                        .put("/pet/{petId}");
                break;
            case "delete":
                response = given()
                        .pathParam("petId", invalidID)
                        .when()
                        .contentType(ContentType.JSON)
                        .body(validResponse)
                        .post("/pet/{petId}/remove");
                break;
            case "retrieve":
                response = given()
                        .pathParam("petID", invalidID)
                        .when()
                        .get("/pet/{petID}");
                break;
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation);
        }
    }

    @Then("the pet is not {string} without {string}")
    public void thePetIsNotOperation(String operation, String withoutField) {
        String code;
        String error;
        String message;
        switch (operation) {
            case "update":
                code = response.jsonPath().getString("code");
                error = response.jsonPath().getString("error");
                message = response.jsonPath().getString("message");
                assertEquals("FST_ERR_VALIDATION", code);
                assertEquals("Bad Request", error);
                assertEquals("body must have required property '"+withoutField+"'", message);
                response.then().statusCode(400);
                break;
            case "delete":
                error = response.jsonPath().getString("error");
                message = response.jsonPath().getString("message");
                assertEquals("Not Found", error);
                assertEquals("Route POST:/api/pet/335 not found", message);
                response.then().statusCode(404);
                break;
            case "retrieve":
                response.then().statusCode(404);
                break;
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation);
        }
    }

    @Then("a validation error occurs")
    public void aValidationErrorOccurs() {
        response.then().statusCode(422);
    }

    // Function to initialize test data values
    private void initializeData(String name, int age, String avatarUrl, String category) {
        PetApi = new petAPI();
        if(!("null".equals(name))) {
            PetApi.setName(name);
        }
        if(age>0)
            PetApi.setAge(age);
        PetApi.setAvatarUrl(avatarUrl);
        PetApi.setCategory(category);
    }

    @Given("I retrieve the pet by the ID from the previous scenario")
    public void iRetrieveThePetByTheIDFromThePreviousScenario() {
        response = given()
                .pathParam("petID", AddedPetID)
                .when()
                .get("/pet/{petID}");
    }
}
