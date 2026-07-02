package utils;

import models.request.AddressRequest;
import models.request.LoginRequest;
import models.request.RegistrationRequest;
import models.testdata.UserTestData;

public class TestDataMapper {

    // converting ui json data to api payload
    public static RegistrationRequest toRegistrationPayload(UserTestData user){

        AddressRequest addressRequest = AddressRequest.builder()
                .state(user.getAddress().getState())
                .city(user.getAddress().getCity())
                .houseNumber(user.getAddress().getHouseNumber())
                .postalCode(user.getAddress().getPostalCode())
                .country(user.getAddress().getCountry())
                .street(user.getAddress().getStreet())
                .build();

        return RegistrationRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dob(user.getDateOfBirth())
                .phone(user.getPhoneNumber())
                .email(user.getEmail())
                .password(user.getPassword())
                .address(addressRequest).build();
    }

    public static LoginRequest toLoginPayload(UserTestData user){

        return LoginRequest.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

}
