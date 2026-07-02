package models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiErrorResponse {

    @JsonProperty("email")
    private List<String> emailError;

    @JsonProperty("password")
    private List<String> passwordError;

    @JsonProperty("error")
    private String error;       // 422

    @JsonProperty("message")
    private String message;     // 401, 401
}

