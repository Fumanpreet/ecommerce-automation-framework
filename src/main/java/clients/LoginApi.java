package clients;

import builders.RequestBuilder;
import config.ConfigManager;
import constants.Endpoints;
import handlers.ApiResponseHandler;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.request.LoginRequest;
import models.response.LoginSuccessResponse;

/*
    Represent User login operations.
*/
public class LoginApi {

    public static ApiResponseHandler getUserVerified(LoginRequest payload){
        RequestSpecification reqSpecification = new RequestBuilder()
                .setBody(payload)
                .build();

        Response response = ApiClient.sendRequest(
                ConfigManager.getApiBaseUrl(),
                Endpoints.USER_LOGIN,
                reqSpecification,
                Method.POST);

        return new ApiResponseHandler(response);
    }

}
