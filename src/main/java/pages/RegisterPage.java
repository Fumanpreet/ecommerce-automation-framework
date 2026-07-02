package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import models.testdata.AddressTestData;
import constants.ErrorType;
import models.testdata.UserTestData;

public class RegisterPage {
    private final Page page;

    private static final String FIRST_NAME = "#first_name";
    private static final String LAST_NAME = "#last_name";
    private static final String DOB = "#dob";
    private static final String COUNTRY = "#country";
    private static final String PHONE = "#phone";
    private static final String EMAIL = "#email";
    private static final String PASSWORD = "#password";
    private static final String REGISTER_BTN = "[data-test='register-submit']";
    // Error Locators
    private static final String FIRST_NAME_ERROR_MSG = "[data-test='first-name-error']";
    private static final String LAST_NAME_ERROR_MSG = "[data-test='last-name-error']";
    private static final String DOB_ERROR_MSG = "[data-test='dob-error']";
    private static final String PHONE_ERROR_MSG = "[data-test='phone-error']";
    private static final String EMAIL_ERROR_MSG = "[data-test='email-error']";
    private static final String PASSWORD_ERROR_MSG = "[data-test='password-error']";
    private static final String REGISTER_ERROR_MSG = "[data-test='register-error']";

    // Address-specific selectors
    private static final String POSTAL_CODE = "#postal_code";
    private static final String HOUSE_NUMBER = "#house_number";
    private static final String STREET = "#street";
    private static final String CITY = "#city";
    private static final String STATE = "#state";

    public RegisterPage(Page page){
        this.page = page;
    }

    @Step("Register User with email: {email}")
    public void register(UserTestData user){
        fillPersonalDetails(user.getFirstName(), user.getLastName(), user.getDateOfBirth(), user.getPhoneNumber(), user.getEmail(), user.getPassword());
        fillAddressDetails(user.getAddress());

        page.click(REGISTER_BTN);
    }

    @Step("Enter user personal details")
    private void fillPersonalDetails(String firstName, String lastName, String dateOfBirth, String phoneNumber, String email, String password){
        page.fill(FIRST_NAME, firstName);
        page.fill(LAST_NAME, lastName);
        page.fill(DOB, dateOfBirth);
        page.fill(PHONE, phoneNumber);
        page.fill(EMAIL, email);
        page.fill(PASSWORD, password);
    }

    @Step("Enter user address details")
    private void fillAddressDetails(AddressTestData address){
        page.locator(COUNTRY).selectOption(address.getCountry());
        page.fill(POSTAL_CODE, address.getPostalCode());
        page.fill(HOUSE_NUMBER, address.getHouseNumber());
        page.fill(STREET, address.getStreet());
        page.fill(CITY, address.getCity());
        page.fill(STATE, address.getState());
    }

    @Step("Received register error message: {errorType}")
    public Locator getErrorMessage(ErrorType errorType){
        return switch (errorType){
            case GLOBAL -> page.locator(REGISTER_ERROR_MSG);
            case FIRST_NAME -> page.locator(FIRST_NAME_ERROR_MSG);
            case LAST_NAME -> page.locator(LAST_NAME_ERROR_MSG);
            case DOB -> page.locator(DOB_ERROR_MSG);
            case PHONE -> page.locator(PHONE_ERROR_MSG);
            case EMAIL -> page.locator(EMAIL_ERROR_MSG);
            case PASSWORD -> page.locator(PASSWORD_ERROR_MSG);
        };
    }
}
