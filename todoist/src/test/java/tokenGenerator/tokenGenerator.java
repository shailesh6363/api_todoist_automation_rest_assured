package tokenGenerator;
import config.ConfigManager;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

public class tokenGenerator {


    public void tokenGenerator() {
        // Default constructor


       String authorizationToken=
               given()
                       .queryParam("client_id", ConfigManager.clientId)
                       .queryParam("client_secret", ConfigManager.clientSecret)

                       .when()
                          .post(ConfigManager.BASE_URL)
                          .then()
                            .assertThat()
                            .statusCode(200)
                            .extract()
                            .path("access_token");






    }
    public static void main(String[] args) {
        tokenGenerator generator = new tokenGenerator();
        generator.tokenGenerator();
        System.out.println("Token generated successfully.");
    }
}
