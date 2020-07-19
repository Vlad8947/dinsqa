package com.vlad.dinsqa;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class UserTest {

    private ExtentReports extentReports;
    private ExtentTest extentTest;

    @BeforeClass
    public void beforeAll() {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("./reports/user_test.html");
        extentReports = new ExtentReports();
        extentReports.attachReporter(htmlReporter);
    }

    @AfterClass
    public void afterAll() {
        extentReports.flush();
    }

    @AfterMethod
    public void afterEach(ITestResult result) {
        int status = result.getStatus();
        if (status == ITestResult.FAILURE) {
            extentTest.fail(result.getThrowable().getMessage());
        }
    }

    @Test
    public void getUsers_thenStatus200() {
        extentTest = extentReports.createTest("getUsers_whenStatus200");

        ValidatableResponse response = when().get("/users").then().statusCode(200);
        extentTest.pass("Status code");

        response.and().extract().body().jsonPath().getList(".", User.class);
        extentTest.pass("Cast json to user list");
    }

    @Test
    public void addUser_thenStatus201() {
        extentTest = extentReports.createTest("addUser_whenStatus201");
        User actualUser = new User(0, "Vlad", "Gonch");

        ValidatableResponse response = given().contentType("application/json").body(actualUser)
                .when().post("/users")
                .then().statusCode(HttpStatus.SC_CREATED);
        extentTest.pass("Status code");

        User expectedUser = response.and().extract().body().jsonPath().getObject(".", User.class);
        Assert.assertEquals(actualUser, expectedUser);
        extentTest.pass("Cast json to User");
    }

    @Test
    public void addUserWithoutFields_thenStatus400() {
        extentTest = extentReports.createTest("addUserWithoutFields_thenStatus400");

        given().contentType("application/json").body("{}")
                .when().post("/users")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
        extentTest.pass("Status code");
    }

    @Test
    public void getUserByNonExistId_thenStatus404() {
        extentTest = extentReports.createTest("getUserByNonExistId_thenStatus404");

        int nonExistId = 0;
        given().pathParam("id", nonExistId)
                .when().get("/users/{id}")
                .then().statusCode(HttpStatus.SC_NOT_FOUND);
        extentTest.pass("Status code");
    }

}
