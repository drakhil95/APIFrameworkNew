package base;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import io.qameta.allure.restassured.AllureRestAssured;

import java.util.logging.Logger;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import config.ConfigReader;
import utils.ExtentManager;
import utils.LoggerUtils;

/**
 * Base Test class providing common setup and utilities
 */
public class BaseTest {

    protected ConfigReader config;
    protected RequestSpecification requestSpec;
    protected ResponseSpecification responseSpec;

    @BeforeClass
    public void globalSetup() {
        LoggerUtils.success("Starting API Test Suite...");
        LoggerUtils.loading("Loading configuration...");

        // Load configuration
        config = new ConfigReader();

        // Configure RestAssured base settings
        RestAssured.baseURI = config.getBaseUrl();
        RestAssured.basePath = config.getBasePath();
        RestAssured.port = config.getPort();

        // Add Allure filter for reporting
        RestAssured.filters(new AllureRestAssured());

        // Setup ExtentReports
        ExtentManager.createTest("API Test Suite", "Testing REST APIs");

        System.out.println("✅ Base configuration completed");
        System.out.println("🌐 Base URL: " + RestAssured.baseURI);
    }
    

    @BeforeMethod
    public void setupRequest() {
        // Create request specification
        requestSpec = RestAssured.given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .relaxedHTTPSValidation();

        // Create response specification with timeout
        responseSpec = RestAssured.expect()
            .time(org.hamcrest.Matchers.lessThan(config.getRequestTimeout()));
    }

    /**
     * Get base request specification
     */
    protected RequestSpecification getRequest() {
        return requestSpec;
    }

    /**
     * Get response specification
     */
    protected ResponseSpecification getResponse() {
        return responseSpec;
    }
}