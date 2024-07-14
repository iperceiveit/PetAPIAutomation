package petapi.stepDefinitions;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

public class BaseTest {

    @BeforeAll
    public static void initBaseTest() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
        RestAssured.basePath = "/api";
        System.out.println("Base Test Base URI: " + RestAssured.baseURI);
        System.out.println("Base Test Port: " + RestAssured.port);
        System.out.println("Base Test Base Path: " + RestAssured.basePath);
    }
}
