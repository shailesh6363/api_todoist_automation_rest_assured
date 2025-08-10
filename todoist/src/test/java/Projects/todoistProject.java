package Projects;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import config.ConfigManager;
import constants.Endpoints;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.*;
import pojo.todoist_project.todoist_project_pojo;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@JsonIgnoreProperties(ignoreUnknown = true)
public class todoistProject {
    static ExtentReports extent;
    static ExtentTest test;

    @BeforeTest
     void setUp(){
        extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("target/Spark.html");
        extent.attachReporter(spark);
    }

    @AfterTest
     void tearDown(){
        extent.flush();
    }
    @Test(description = "TC-01-Create a new Project with Name Only", priority = 1)
    public void createProjectWithNameOnly() {
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "API Automation Project");
        RestAssured.baseURI = ConfigManager.BASE_URL;

        Response response = given()
                .header("Authorization", "Bearer " + ConfigManager.api_token)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .log().all()
                .when()
                .post(Endpoints.projectEndpoint) // fixed typo
                .then()
                .log().all()
                .assertThat()
                .statusCode(200) // use 201 if API returns Created
                .extract()
                .response();

        todoist_project_pojo project = response.as(todoist_project_pojo.class);
        assertThat(project, notNullValue());
        assertThat(project.getColor(), equalTo("charcoal"));
        assertThat(project.getName(), equalTo("API Automation Project"));
        assertThat(project.getRole(), equalTo("CREATOR"));

    }

    @Test(description = "TC-02-Create a new Project with No project Name")
    public void createProjectWithNoProjectName() {
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "");
        requestBody.put("color", "charcoal");
        RestAssured.baseURI = ConfigManager.BASE_URL;


        Response response=given()
                .header("Authorization","Bearer "+ConfigManager.api_token)
                .header("Content-Type","application/json")
                .body(requestBody)
                .log().all()
                .when()
                .post(Endpoints.projectEndpoint)
                .then()
                .log().all()
                .assertThat()
                .statusCode(400)
                .extract()
                .response();


        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getString("error"), equalTo("Name must be provided for the project creation"));


    }

    @Test(description = "TC-03-Create a new Project with Whitespace-only name")
    public void createProjectWithWhitespaceOnlyName() {
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", " ");
        requestBody.put("color", "charcoal");
        RestAssured.baseURI = ConfigManager.BASE_URL;

        Response response=given()
                .header("Authorization","Bearer "+ConfigManager.api_token)
                .header("Content-Type","application/json")
                .body(requestBody)
                .log().all()
                .when()
                .post(Endpoints.projectEndpoint)
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

    }

    @Test(description = "TC-03-Create a new project with Missing Authorization header")
    public void createProjectWithMissingAuthorizationHeader() {
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "API Automation Project");
        RestAssured.baseURI = ConfigManager.BASE_URL;
        Response response=given()
                .header("Content-Type","application/json")
                .body(requestBody)
                .log().all()
                .when()
                .post(Endpoints.projectEndpoint)
                .then()
                .log().all()
                .assertThat()
                .statusCode(401)
                .extract()
                .response();

        //assert status code
        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getString("error"), equalTo("Unauthorized"));


    }

    @Test(description = "TC-04-Create a new project with Invalid Token")
    public void createProjectWithInvalidToken() {
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "API Automation Project");
        RestAssured.baseURI = ConfigManager.BASE_URL;

        Response response=given()
                .header("Authorization","Bearer 12345")
                .header("Content-Type","application/json")
                .when()
                .post(Endpoints.projectEndpoint)
                .then().assertThat()
                .statusCode(400)
                .extract()
                .response();
    }

    }


