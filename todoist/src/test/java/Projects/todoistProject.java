package Projects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import config.ConfigManager;
import constants.Endpoints;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import pojo.todoist_project.todoist_project_pojo;
import pojo.todoist_project.todoist_project_pojo_update;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class todoistProject {
    SoftAssert softAssert=new SoftAssert();
    String projectId;
    static RequestSpecification setCommonSpec(){
        RequestSpecBuilder specBuilder=new RequestSpecBuilder();
        specBuilder.setBaseUri(ConfigManager.BASE_URL);
        specBuilder.setBasePath(Endpoints.projectEndpoint);
        specBuilder.setContentType(ContentType.JSON);
        specBuilder.addHeader("Authorization","Bearer "+ConfigManager.api_token);
        return specBuilder.build();
    }

    @Test
    @Description("TC-01-Create a New Project with Name Only")
    @Owner("Shailesh Waghole")
    @Feature("Project CRUD")
    public void createProject(){

        HashMap<String, Object> requestBody = new HashMap<>();
        Allure.label("Name","Creating Requst Body");
        requestBody.put("name", "API Automation Project");
        Response response =

                given()
                .spec(setCommonSpec())
                .body(requestBody)
                .log().all()
                .when()
                .post()
                .then()
                .log().all()
                .assertThat()
                .statusCode(200) // use 201 if API returns Created
                .extract()
                .response();

        todoist_project_pojo project = response.as(todoist_project_pojo.class);
        projectId = project.getId();
        softAssert.assertEquals(project.getName(), "API Automation Project");
        softAssert.assertNotNull(project.getId());
        softAssert.assertEquals(project.getColor(),"charcoal");
        softAssert.assertEquals(project.getRole(),"CREATOR");
        softAssert.assertAll();
        response.body().prettyPrint();
    }
    @Test(description = "TC-02-Create a new Project with Name Only And Delete", priority = 1)

    public void createProjectWithNameOnly() {
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "API Automation Project");
        Response response = given()
                .spec(setCommonSpec())
                .body(requestBody)
                .log().all()
                .when()
                .post()
                .then()
                .log().all()
                .assertThat()
                .statusCode(200) // use 201 if API returns Created
                .extract()
                .response();

        todoist_project_pojo project = response.as(todoist_project_pojo.class);
        softAssert.assertEquals(project.getName(), "API Automation Project");
        softAssert.assertNotNull(project.getId());
        softAssert.assertEquals(project.getColor(),"charcoal");
        softAssert.assertEquals(project.getRole(),"CREATOR");
        softAssert.assertAll();


        Response deleteResponse=
                given()
                        .spec(setCommonSpec())
                        .when()
                        .delete("/"+project.getId())
                        .then()
                        .assertThat()
                        .statusCode(204)
                        .extract()
                        .response();
        deleteResponse.body().prettyPrint();

    }

    @Test(description = "TC-03-Create a new Project with No project Name",priority = 4)
    public void createProjectWithNoProjectName() {
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "");
        requestBody.put("color", "charcoal");
        Response response=given()
                .spec(setCommonSpec())
                .body(requestBody)
                .log().all()
                .when()
                .post()
                .then()
                .log().all()
                .assertThat()
                .statusCode(400)
                .extract()
                .response();


        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getString("error"), equalTo("Name must be provided for the project creation"));
        response.body().prettyPrint();

    }

    @Test(description = "TC-04-Create a new Project with Whitespace-only name",priority = 5)
    public void createProjectWithWhitespaceOnlyName() {
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", " ");
        requestBody.put("color", "charcoal");
        Response response=given()
                .spec(setCommonSpec())
                .body(requestBody)
                .log().all()
                .when()
                .post()
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonPath = response.jsonPath();
        String id = jsonPath.getString("id");
        Response deleteResponse=
                given()
                        .spec(setCommonSpec())
                        .when()
                        .delete("/"+id)
                        .then()
                        .assertThat()
                        .statusCode(204)
                        .extract()
                        .response();
        response.body().prettyPrint();



    }

    @Test(description = "TC-05-Create a new project with Missing Authorization header",priority = 6)
    public void createProjectWithMissingAuthorizationHeader() {
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "API Automation Project");

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
        response.body().prettyPrint();


    }

    @Test(description = "TC-06-Create a new project with Invalid Token",priority = 7)
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

        response.prettyPrint();
    }

    @Test(description = "TC-07-Verify that fetching all projects with a valid token returns 200 OK",priority = 8)
    public void fetchAllProjects(){
        Response response=given()
                .spec(setCommonSpec())
                .log().all()
                .when()
                .get()
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response();

        response.body().prettyPrint();
    }

    @Test(description = "TC-08-Verify that request with an invalid token returns 401 Unauthorized",priority = 9)
    public void fetchAllProjectsWithInvalidToken(){
        RestAssured.baseURI = ConfigManager.BASE_URL;
        Response response=given()
                .header("Authorization","Bearer "+1234)
                .log().all()
                .when()
                .get(Endpoints.projectEndpoint)
                .then()
                .assertThat().statusCode(401)
                .extract()
                .response();


        response.body().prettyPrint();
        JsonPath jsonPath = response.jsonPath();
        softAssert.assertEquals(jsonPath.getString("error"), "Unauthorized");
        softAssert.assertEquals(jsonPath.getInt("error_code"), 477);
        softAssert.assertEquals(jsonPath.getInt("http_code"), 401);
        softAssert.assertAll();
    }
    @Test(description = "TC-09-Verify that when no projects exist API returns an empty array",priority = 10)
    public void fetchAllProjectsWhenNoProject(){
        Response response=given()
                .spec(setCommonSpec())
                .log().all()
                .when()
                .get()
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response();

        response.body().prettyPrint();
    }

    @Test(description = "TC-10-Verify that fetching a valid project ID returns 200 OK and correct project data",dependsOnMethods = "createProject",priority = 11)
    public void fetchProjectById(){

        Response response=given()
                .spec(setCommonSpec())
                .log().all()
                .when()
                .get("/"+projectId)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response();

        response.body().prettyPrint();
        todoist_project_pojo project=response.as(todoist_project_pojo.class);
        softAssert.assertEquals(project.getId(),project.getId());
        softAssert.assertEquals(project.getName(),project.getName());
        Response deleteResponse=
                given()
                        .spec(setCommonSpec())
                        .when()
                        .delete("/"+projectId)
                        .then()
                        .assertThat()
                        .statusCode(204)
                        .extract()
                        .response();
        deleteResponse.body().prettyPrint();

    }

    @Test(description = "TC-11-Verify that missing Authorization header returns 401 Unauthorized",priority = 12)
    public void missingAuthorizationHeader(){

        RestAssured.baseURI = ConfigManager.BASE_URL;
        Response response=given()
                .header("Content-Type","application/json")
                .when()
                .get(Endpoints.projectEndpoint)
                .then()
                .assertThat().statusCode(401)
                .extract()
                .response();


        response.body().prettyPrint();
        JsonPath jsonPath = response.jsonPath();
        softAssert.assertEquals(jsonPath.getString("error"), "Unauthorized");
        softAssert.assertEquals(jsonPath.getInt("error_code"), 477);
        softAssert.assertEquals(jsonPath.getInt("http_code"), 401);
        softAssert.assertAll();

    }

    @Test(description = "TC-12-Verify handling of invalid format project_id (non-numeric)",priority = 13)
    public void invalidFormatProjectId(){
        Response response=given()
                .spec(setCommonSpec())
                .log().all()
                .when()
                .get("/"+"RestAssured")
                .then()
                .assertThat().statusCode(400)
                .extract()
                .response();

        response.body().prettyPrint();
        JsonPath jsonPath = response.jsonPath();
        softAssert.assertEquals(jsonPath.getString("error"), "Invalid argument value");
        softAssert.assertEquals(jsonPath.getInt("error_code"), 20);
        softAssert.assertEquals(jsonPath.getString("error_extra.argument"),"project_id");
        softAssert.assertAll();
    }


    @Test(description = "TC-13-Verify that updating a valid project with all fields returns 200 OK",dependsOnMethods = "createProject",priority = 14)
    public void updateProject(){
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", " Updated API Automation Project");
        requestBody.put("color", "orange");
        requestBody.put("is_favorite", true);
        requestBody.put("description", "Updated Via API");
        Response response=given()
                .spec(setCommonSpec())
                .body(requestBody)
                .log().all()
                .when()
                .post("/"+projectId)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response();

        response.body().prettyPrint();

        todoist_project_pojo_update updatedProject=response.as(todoist_project_pojo_update.class);
        JsonPath jsonPath = response.jsonPath();
        softAssert.assertEquals(updatedProject.getName(),jsonPath.getString("name"));
        softAssert.assertEquals(updatedProject.getColor(),jsonPath.getString("color"));
        softAssert.assertEquals(updatedProject.getDescription(),jsonPath.getString("description"));
        softAssert.assertAll();

        Response deleteResponse=
                given()
                        .spec(setCommonSpec())
                        .when()
                        .delete("/"+projectId)
                        .then()
                        .assertThat()
                        .statusCode(204)
                        .extract()
                        .response();
        deleteResponse.body().prettyPrint();

    }

    @Test(description = "TC-14-Verify that updating only the name field works",dependsOnMethods = "createProject",priority = 15)
    public void updateProjectName(){
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", " Updated API Automation Project");

        Response response=given()
                .spec(setCommonSpec())
                .body(requestBody)
                .when()
                .post("/"+projectId)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response();


        response.body().prettyPrint();
        todoist_project_pojo_update updatedProject=response.as(todoist_project_pojo_update.class);
        JsonPath jsonPath = response.jsonPath();
        softAssert.assertEquals(updatedProject.getName(),jsonPath.getString("name"));
        softAssert.assertAll();
        Response deleteResponse=
                given()
                        .spec(setCommonSpec())
                        .when()
                        .delete("/"+projectId)
                        .then()
                        .assertThat()
                        .statusCode(204)
                        .extract()
                        .response();
        deleteResponse.body().prettyPrint();
    }
    @Test(description = "TC-15-Verify that updating only the description field works",dependsOnMethods = "createProject",priority =15)
    public void updateProjectDescription(){
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("description", " Updated Description Via API");

        Response response=given()
                .spec(setCommonSpec())
                .body(requestBody)
                .when()
                .post("/"+projectId)
                .then().assertThat().statusCode(200)
                .extract()
                .response();




        response.body().prettyPrint();
        todoist_project_pojo_update updatedProject=response.as(todoist_project_pojo_update.class);
        JsonPath jsonPath = response.jsonPath();
        softAssert.assertEquals(updatedProject.getDescription(),jsonPath.getString("description"));
        softAssert.assertAll();
        Response deleteResponse=
                given()
                        .spec(setCommonSpec())
                        .when()
                        .delete("/"+projectId)
                        .then()
                        .assertThat()
                        .statusCode(204)
                        .extract()
                        .response();
        deleteResponse.body().prettyPrint();

    }

    @Test(description = "TC-16-Verify that updating with invalid color value returns 400",dependsOnMethods = "createProject",priority = 17)
    public void updateProjectColor(){
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("color", "pinkish_red");

        Response response=given()
                .spec(setCommonSpec())
                .body(requestBody)
                .when()
                .post("/"+projectId)
                .then()
                .assertThat().statusCode(400)
                .extract()
                .response();

        response.body().prettyPrint();
        JsonPath jsonPath = response.jsonPath();
        softAssert.assertEquals(jsonPath.getString("error"), "Invalid argument value");
        softAssert.assertEquals(jsonPath.getInt("error_code"), 20);
        softAssert.assertEquals(jsonPath.getString("error_extra.argument"),"color");
        softAssert.assertAll();
        Response deleteResponse=
                given()
                        .spec(setCommonSpec())
                        .when()
                        .delete("/"+projectId)
                        .then()
                        .assertThat()
                        .statusCode(204)
                        .extract()
                        .response();
        deleteResponse.body().prettyPrint();

    }

    }






