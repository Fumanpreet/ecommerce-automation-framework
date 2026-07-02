package clients;

import config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


// Handle generic HTTP communication.
public class ApiClient {

    public static Response sendRequest(
            String baseUrl,
            String endpoint,
            RequestSpecification reqSpecification,
            Method httpMethod) {

        return RestAssured.given()
                .spec(reqSpecification)
                .baseUri(baseUrl)
                .basePath(endpoint)
                .request(httpMethod);
    }
}