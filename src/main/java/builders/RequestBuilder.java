package builders;

import clients.ApiClient;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 *  Uses Method Chaining Model
*/
public class RequestBuilder {
    private final RequestSpecBuilder requestSpecBuilder;

    public RequestBuilder() {

        this.requestSpecBuilder = new RequestSpecBuilder();
        this.requestSpecBuilder.setContentType(ContentType.JSON);
    }

    public RequestBuilder setContentType(ContentType contentType){
        this.requestSpecBuilder.setContentType(contentType);
        return this;
    }

    public RequestBuilder addHeader(String key, String value) {

        this.requestSpecBuilder.addHeader(key, value);
        return this;
    }

    public RequestBuilder setBearerToken(String token) {
        return addHeader("Authorization", "bearer " + token);
    }

    public RequestBuilder setPathParam(String parameterName, Object value) {
        this.requestSpecBuilder.addPathParam(parameterName, value);
        return this;
    }

    public RequestBuilder setBody(Object body) {

        this.requestSpecBuilder.setBody(body);
        return this;
    }

    public RequestSpecification build() {

        return this.requestSpecBuilder.build();
    }
}
