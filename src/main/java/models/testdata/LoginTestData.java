package models.testdata;

import constants.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginTestData {
    private String email;
    private String password;
    private String expectedResult;
    private ErrorType errorType;
}
