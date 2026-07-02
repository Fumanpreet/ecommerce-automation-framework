package base;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import config.ConfigManager;
import listeners.TestListener;
import lombok.Getter;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.IOException;
import java.nio.file.Paths;


public class BaseUiTest {
    private Playwright playwright;
    private Browser browser;
    protected BrowserContext context;
    @Getter
    protected Page page;

    @BeforeClass
    protected void setUp(){
        playwright = Playwright.create();

        String browserName = ConfigManager.getProperty("browser");
        String headless = ConfigManager.getProperty("headless");

        boolean isHeadless = Boolean.parseBoolean(headless);

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(isHeadless)
                .setSlowMo(100);

        System.out.println("Running on browser: " + browserName);
        System.out.println("Headless mode: " + isHeadless);

        switch (browserName.toLowerCase()) {
            case "chrome":
                browser = playwright.chromium().launch(launchOptions);
                break;

            case "firefox":
                browser = playwright.firefox().launch(launchOptions);
                break;

            case "webkit":
                browser = playwright.webkit().launch(launchOptions);
                break;

            default:
                throw new IllegalArgumentException("Invalid browser name: " + browserName);
        }
    }

    @BeforeMethod
    protected void initializeTestSession(ITestResult result){
        context = browser.newContext();

        // Start tracing in the context
        context.tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );
        // Share the context in TestNG locker for next steps (onTestFailure())
        result.setAttribute(TestListener.CONTEXT_KEY, context);

        page = context.newPage();
    }

    protected void launchBrowserPage(String url){
        page.navigate(ConfigManager.getBaseUrl() + url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE));
    }

    @AfterClass
    protected void tearDown() {
        browser.close();
        playwright.close();
    }
}
