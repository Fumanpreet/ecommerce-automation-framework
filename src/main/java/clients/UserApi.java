package clients;

import builders.RequestBuilder;
import com.beust.ah.A;
import config.ConfigManager;
import constants.Endpoints;
import handlers.ApiResponseHandler;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.request.LoginRequest;
import models.request.RegistrationRequest;
import models.response.LoginSuccessResponse;

import static constants.Endpoints.*;

/*
    Represent User register operations.
*/
public class UserApi {

    public static ApiResponseHandler createUser(RegistrationRequest payload){
        RequestSpecification reqSpecification = new RequestBuilder()
                .setBody(payload)
                .build();

        Response response = ApiClient.sendRequest(ConfigManager.getApiBaseUrl(), CREATE_USER, reqSpecification, Method.POST);

        return new ApiResponseHandler(response);
    }

    public static ApiResponseHandler deleteUser(String id){

        String token = getAdminToken();

        Response response = ApiClient.sendRequest(ConfigManager.getApiBaseUrl(), USER, getRequestSpecs(id, token), Method.DELETE);

        return new ApiResponseHandler(response);
    }

    public static ApiResponseHandler getUser(String email, String password, String id){

        String token = getUserToken(email, password);

        Response response = ApiClient.sendRequest(ConfigManager.getApiBaseUrl(), USER, getRequestSpecs(id, token), Method.GET);

        return new ApiResponseHandler(response);
    }

    public static ApiResponseHandler updateUser(RegistrationRequest payload, String email, String password, String id){

        String token = getUserToken(email, password);

        RequestSpecification reqSpecification = new RequestBuilder()
                .setBody(payload)
                .setPathParam("id", id)
                .setBearerToken(token)
                .build();

        Response response = ApiClient.sendRequest(ConfigManager.getApiBaseUrl(), USER, reqSpecification, Method.PUT);

        return new ApiResponseHandler(response);
    }

    private static String getUserToken(String email, String password){
        LoginRequest payload = new LoginRequest(email, password);

        return LoginApi.getUserVerified(payload).validateStatusCode(200).extractAs(LoginSuccessResponse.class).getAccessToken();
    }

    private static String getAdminToken(){
        return getUserToken(ConfigManager.getAdminEmail(), ConfigManager.getAdminPassword());
    }

    private static RequestSpecification getRequestSpecs(String id, String token){
        return new RequestBuilder()
                .setPathParam("id", id)
                .setBearerToken(token)
                .build();
    }
}
