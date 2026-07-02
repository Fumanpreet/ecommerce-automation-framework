package api;

import base.BaseApiTest;
import clients.LoginApi;
import clients.UserApi;
import dataproviders.UserDataProvider;
import factories.UserFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import models.request.RegistrationRequest;
import models.response.ApiErrorResponse;
import models.response.UserSuccessResponse;
import models.testdata.UserTestData;
import org.testng.annotations.Test;
import utils.TestDataMapper;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest extends BaseApiTest {

    /*
        Create a new user and verify
    */
    @Test(dataProvider = "successUserData", dataProviderClass = UserDataProvider.class)
    @Epic("API Testing")
    @Feature("User Registration API")
    @Story("POST /auth/register")
    @Description("Create and Verify valid user can register.")
    public void testValidUserRegistration(UserTestData user) {

        // make user unique and get fields only required by specific api
        RegistrationRequest requestPayload = TestDataMapper.toRegistrationPayload(UserFactory.createUniqueUser(user));
        // create new user
        UserSuccessResponse createResponse = UserApi.createUser(requestPayload).logTrace().validateStatusCode(201).extractAs(UserSuccessResponse.class);

        // Match response fields with payload
        assertThat(createResponse.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(createResponse.getLastName()).isEqualTo(user.getLastName());
        assertThat(createResponse.getEmail()).isEqualTo(user.getEmail());

        // Use auth token to get the user details
        UserSuccessResponse getResponse = UserApi.getUser(user.getEmail(), user.getPassword(), createResponse.getId()).validateStatusCode(200).extractAs(UserSuccessResponse.class);;

        // Match GET response field with payload fields
        assertThat(getResponse.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(getResponse.getLastName()).isEqualTo(user.getLastName());
        assertThat(getResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(getResponse.getId()).isEqualTo(createResponse.getId());
    }


    /*
        duplicate user update and verify
    */
    @Test(priority = 1, dataProvider = "successUserData", dataProviderClass = UserDataProvider.class)
    @Epic("API Testing")
    @Feature("User Registration API")
    @Story("POST /auth/register")
    @Description("Verify single valid user cannot register twice.")
    public void testUserAlreadyRegistered(UserTestData user) {
        RegistrationRequest requestPayload = TestDataMapper.toRegistrationPayload(UserFactory.createUniqueUser(user));;

        // PRE-CONDITION: first make sure user is registered
        UserApi.createUser(requestPayload).logTrace().validateStatusCode(201).extractAs(UserSuccessResponse.class);

        // After creating new user, try creating same user again
        ApiErrorResponse response = UserApi.createUser(requestPayload).logTrace().validateStatusCode(409).extractAs(ApiErrorResponse.class);

        assertThat(response.getEmailError().getFirst()).isEqualTo("A customer with this email address already exists.");
    }


    /*
        delete the user and check it with login
    */
    @Test(priority = 2, dataProvider = "successUserData", dataProviderClass = UserDataProvider.class)
    @Epic("API Testing")
    @Feature("User Delete API")
    @Story("DELETE /users/{id}")
    @Description("Delete and verify a valid user does not exist.")
    public void testUserDeleteAndVerifyViaLogin(UserTestData user) {
        RegistrationRequest requestPayload = TestDataMapper.toRegistrationPayload(UserFactory.createUniqueUser(user));

        // PRE-CONDITION: first make sure user is registered
        UserSuccessResponse regResponse = UserApi.createUser(requestPayload).logTrace().validateStatusCode(201).validateStatusCode(201).extractAs(UserSuccessResponse.class);

        // Delete the User
        UserApi.deleteUser(regResponse.getId()).logTrace().validateStatusCode(204);

    }


    /*
        delete the user and check it with login
    */
    @Test(priority = 3, dataProvider = "successUserData", dataProviderClass = UserDataProvider.class)
    @Epic("API Testing")
    @Feature("User Update API")
    @Story("PUT /users/{id}")
    @Description("Update and verify a valid user has updated.")
    public void testUserUpdateAndVerifyViaLogin(UserTestData user) {
        RegistrationRequest requestPayload = TestDataMapper.toRegistrationPayload(UserFactory.createUniqueUser(user));

        // PRE-CONDITION: first make sure user is registered
        UserSuccessResponse response = UserApi.createUser(requestPayload).validateStatusCode(201).extractAs(UserSuccessResponse.class);

        // Update some user details
        requestPayload.setFirstName(UserFactory.generateUniqueFirstName(user));
        requestPayload.setLastName(UserFactory.generateUniqueLastName(user));
        requestPayload.setEmail(UserFactory.generateUniqueEmail(user));

        UserApi.updateUser(requestPayload, user.getEmail(), user.getPassword(), response.getId()).logTrace().validateStatusCode(200);

        // match the updated payload with GET response
        UserSuccessResponse getResponse = UserApi.getUser(requestPayload.getEmail(), user.getPassword(), response.getId()).validateStatusCode(200).extractAs(UserSuccessResponse.class);

        // Match GET response field with payload fields
        assertThat(getResponse.getFirstName()).isEqualTo(requestPayload.getFirstName());
        assertThat(getResponse.getLastName()).isEqualTo(requestPayload.getLastName());
        assertThat(getResponse.getEmail()).isEqualTo(requestPayload.getEmail());
        assertThat(getResponse.getId()).isEqualTo(response.getId());
    }


    /*
        Invalid Payload
    */
    @Test(priority = 4, dataProvider = "failureUserData", dataProviderClass = UserDataProvider.class)
    @Epic("API Testing")
    @Feature("User Registration API")
    @Story("POST /auth/register")
    @Description("Verify invalid user cannot register.")
    public void testInvalidUserRegistration(UserTestData user) {

        RegistrationRequest requestPayload = TestDataMapper.toRegistrationPayload(user);
        UserApi.createUser(requestPayload).logTrace().validateStatusCode(422);
    }


}


