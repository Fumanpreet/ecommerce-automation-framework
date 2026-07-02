package ui;

import base.BaseUiTest;
import config.ConfigManager;
import dataproviders.UserDataProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import models.testdata.UserTestData;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.RegisterPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static constants.Endpoints.LOGIN_PAGE_URL;
import static constants.Endpoints.REGISTER_PAGE_URL;

public class RegisterTest extends BaseUiTest {
    protected RegisterPage registerPage;

    @BeforeMethod
    public void setup(){
        registerPage = new RegisterPage(page);

        launchBrowserPage(REGISTER_PAGE_URL);
    }

    @Test(dataProvider = "successUserData", dataProviderClass = UserDataProvider.class)
    @Epic("UI Testing")
    @Feature("Registration")
    @Story("Registration Form Validation")
    @Description("Verify valid user can register.")
    public void testValidUserRegistration(UserTestData user){
        registerPage.register(user);

        assertThat(page).hasURL(ConfigManager.getBaseUrl() + LOGIN_PAGE_URL);
    }


    @Test(priority = 1, dataProvider = "successUserData", dataProviderClass = UserDataProvider.class)
    @Epic("UI Testing")
    @Feature("Registration")
    @Story("Registration Form Validation")
    @Description("Verify single valid user cannot register twice.")
    public void testUserAlreadyRegistered(UserTestData user){
        registerPage.register(user);

        assertThat(registerPage.getErrorMessage(user.getErrorType())).hasText(user.getExpectedResult());
    }


    @Test(priority = 2, dataProvider = "failureUserData", dataProviderClass = UserDataProvider.class)
    @Epic("UI Testing")
    @Feature("Registration")
    @Story("Registration Form Validation")
    @Description("Verify invalid user cannot register.")
    public void testInvalidUserRegistration(UserTestData user){
        registerPage.register(user);

        assertThat(registerPage.getErrorMessage(user.getErrorType())).hasText(user.getExpectedResult());
    }
}
