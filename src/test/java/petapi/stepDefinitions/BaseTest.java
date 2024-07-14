package petapi.stepDefinitions;

import io.restassured.RestAssured;

public class BaseTest {

    public void initBaseTest() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
        RestAssured.basePath = "/api";
    }
}
