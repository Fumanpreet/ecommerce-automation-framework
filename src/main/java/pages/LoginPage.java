package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import constants.ErrorType;
import io.qameta.allure.Step;

public class LoginPage {
    private final Page page;

    // Page Locators
    private static final String USER_LOGIN_EMAIL = "#email";
    private static final String USER_LOGIN_PASSWORD = "#password";
    private static final String LOGIN_BUTTON = "[data-test='login-submit']";
    // Error element selectors
    private static final String GLOBAL_ERROR_MSG = "[data-test='login-error']";
    private static final String EMAIL_ERROR_MSG = "[data-test='email-error']";
    private static final String PASSWORD_ERROR_MSG = "[data-test='password-error']";


    public LoginPage(Page page){
        this.page = page;
    }

    @Step("User logged in with email: {email}")
    public void login(String email, String password){
        page.fill(USER_LOGIN_EMAIL, email);
        page.fill(USER_LOGIN_PASSWORD, password);
        page.click(LOGIN_BUTTON);
    }

    @Step("Received login error message: {errorType}")
    public Locator getErrorMessage(ErrorType errorType){
        return switch (errorType) {
            case GLOBAL -> page.locator(GLOBAL_ERROR_MSG);
            case EMAIL -> page.locator(EMAIL_ERROR_MSG);
            case PASSWORD -> page.locator(PASSWORD_ERROR_MSG);
            default -> throw new IllegalArgumentException("Unknown error: " + errorType);
        };
    }
}
