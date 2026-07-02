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
public class UserTestData {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String phoneNumber;
    private String email;
    private String password;
    private AddressTestData address;

    private ErrorType errorType;
    private String expectedResult;
}
