package pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class AccountPage {
    private final Page page;

    private final String accountMenuDropdown;
    private final String logoutButton;

    public AccountPage(Page page){
        this.page = page;
        this.accountMenuDropdown = "#menu";
        this.logoutButton = "[data-test=\"nav-sign-out\"]";
    }

    @Step("User Logged out")
    public void logout(){
        page.locator(this.accountMenuDropdown).click();
        page.locator(this.logoutButton).click();
    }

}
