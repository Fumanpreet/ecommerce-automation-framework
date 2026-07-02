package ui;

import base.BaseUiTest;
import config.ConfigManager;
import dataproviders.LoginDataProvider;
import dataproviders.UserDataProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import models.testdata.LoginTestData;
import models.testdata.UserTestData;
import org.testng.annotations.*;
import pages.AccountPage;
import pages.LoginPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static constants.Endpoints.ACCOUNT_PAGE_URL;
import static constants.Endpoints.LOGIN_PAGE_URL;

public class LoginTest extends BaseUiTest {
    protected LoginPage loginPage;
    protected AccountPage accountPage;

    @BeforeMethod
    public void setup(){
        loginPage = new LoginPage(getPage());
        accountPage = new AccountPage(getPage());
        launchBrowserPage(LOGIN_PAGE_URL);
    }

    @Test
    @Epic("UI Testing")
    @Feature("Login")
    @Story("Login Form Validation")
    @Description("Verify valid user can login and logout.")
    public void testValidLoginAndLogout(){

        loginPage.login(ConfigManager.getDefaultEmail(), ConfigManager.getDefaultPassword());
        assertThat(getPage()).hasURL(ConfigManager.getBaseUrl() + ACCOUNT_PAGE_URL);
        getPage().waitForTimeout(3000);
        accountPage.logout();
        assertThat(getPage()).hasURL(ConfigManager.getBaseUrl() + LOGIN_PAGE_URL);

    }

    @Test(priority = 1, dataProvider = "failureLoginData", dataProviderClass = LoginDataProvider.class)
    @Epic("UI Testing")
    @Feature("Login")
    @Story("Login Form Validation")
    @Description("Verify invalid user cannot login.")
    public void testInvalidEmailLogin(LoginTestData user){

        loginPage.login(user.getEmail(), user.getPassword());
        assertThat(loginPage.getErrorMessage(user.getErrorType())).hasText(user.getExpectedResult());
    }
}
