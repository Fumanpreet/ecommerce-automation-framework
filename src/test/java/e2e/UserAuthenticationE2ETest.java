package e2e;

import base.BaseUiTest;
import clients.ApiClient;
import clients.LoginApi;
import clients.UserApi;
import com.beust.ah.A;
import config.ConfigManager;
import constants.ErrorType;
import dataproviders.UserDataProvider;
import factories.UserFactory;
import handlers.ApiResponseHandler;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import models.request.LoginRequest;
import models.request.RegistrationRequest;
import models.response.ApiErrorResponse;
import models.response.LoginSuccessResponse;
import models.response.UserSuccessResponse;
import models.testdata.UserTestData;
import org.testng.annotations.*;
import pages.AccountPage;
import pages.LoginPage;
import utils.TestDataMapper;

import static constants.Endpoints.ACCOUNT_PAGE_URL;
import static constants.Endpoints.LOGIN_PAGE_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class UserAuthenticationE2ETest extends BaseUiTest {
    protected LoginPage loginPage;
    protected AccountPage accountPage;

    @BeforeMethod
    public void setup(){
        loginPage = new LoginPage(page);
        accountPage = new AccountPage(page);
        launchBrowserPage(LOGIN_PAGE_URL);
    }

    @Test(dataProvider = "successUserData", dataProviderClass = UserDataProvider.class)
    @Epic("End-to-End Testing")
    @Feature("User Onboard Journey")
    @Story("New User Account Lifecycle")
    @Description("A user can register, update, and delete themself at any instance.")
    public void testUserRegisterViaUIAndValidateViaAPI(UserTestData user){
        /*
            Create User via API
        */
        // create unique instance of user
        UserTestData uniqueUser = UserFactory.createUniqueUser(user);
        RegistrationRequest registerRequestPayload = TestDataMapper.toRegistrationPayload(uniqueUser);
        // call to register api with payload
        UserSuccessResponse registerResponse = UserApi.createUser(registerRequestPayload).logTrace().validateStatusCode(201).extractAs(UserSuccessResponse.class);

        assertThat(registerResponse.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(registerResponse.getLastName()).isEqualTo(user.getLastName());
        assertThat(registerResponse.getEmail()).isEqualTo(user.getEmail());


        /*
            Login and Logout via UI
        */
        loginPage.login(user.getEmail(), user.getPassword());
        assertThat(page).hasURL(ConfigManager.getBaseUrl() + ACCOUNT_PAGE_URL);
        page.waitForTimeout(3000);
        accountPage.logout();
        assertThat(page).hasURL(ConfigManager.getBaseUrl() + LOGIN_PAGE_URL);


        /*
           Delete User via API
        */
        UserApi.deleteUser(registerResponse.getId()).logTrace().validateStatusCode(204);

        /*
            Login via UI
        */
        loginPage.login(user.getEmail(), user.getPassword());
        assertThat(loginPage.getErrorMessage(ErrorType.GLOBAL)).hasText("Invalid email or password");

        /*
            Login via API
        */
        LoginRequest loginRequestPayload = TestDataMapper.toLoginPayload(user);
        ApiErrorResponse loginResponse = LoginApi.getUserVerified(loginRequestPayload).validateStatusCode(401).extractAs(ApiErrorResponse.class);
        assertThat(loginResponse.getError()).isEqualTo("Unauthorized");

    }

}
