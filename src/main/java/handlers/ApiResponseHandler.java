package handlers;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

/*
    Allows method chaining
*/
public class ApiResponseHandler {
    private final Response response;

    public ApiResponseHandler(Response response) {
        this.response = response;
    }

    public ApiResponseHandler validateStatusCode(int expectedCode) {
        response.then().statusCode(expectedCode);
        return this;
    }

    public ApiResponseHandler validateHeader(String headerName, String expectedValue) {
        response.then().header(headerName, equalTo(expectedValue));
        return this;
    }

    public ApiResponseHandler logTrace() {
        response.then().log().all();
        return this;
    }

    /**
     * Response converts to Response POJO
     */
    public <T> T extractAs(Class<T> responseClass) {
        return response.as(responseClass);
    }
}
