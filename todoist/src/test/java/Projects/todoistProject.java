package Projects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import config.ConfigManager;
import constants.Endpoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import pojo.todoist_project.todoist_project_pojo;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@JsonIgnoreProperties(ignoreUnknown = true)
public class todoistProject {



    @Test(description = "Creat a new Project with Name Only",priority = 1)
    public void createProjectWithNameOnly() {
        HashMap <String,Object> requestBody=new HashMap<>();
        requestBody.put("name", "API Automation Project");
        RestAssured.baseURI = ConfigManager.BASE_URL;
        Response response=
                (Response) given()

                        .header("Authorization", "Bearer " + ConfigManager.api_token)
                        .header("Content-Type", "application/json")
                        .body(requestBody)
                        .log().all()
                        .when()
                        .post(Endpoints.projectEndpoinst)
                        .then()
                        .log().all()
                        .assertThat()
                        .statusCode(200)
                        .extract()
                        .response();

        todoist_project_pojo project = response.as(todoist_project_pojo.class);
        assertThat(project.getColor(), equalTo("charcoal"));
        assertThat(project.getName(), equalTo("API Automation Project"));
        assertThat(project.getRole(), equalTo("CREATOR"));
            }

}
