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
    protected ThreadLocal<BrowserContext> context = new ThreadLocal<>();
    protected ThreadLocal<Page> page = new ThreadLocal<>();

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
        BrowserContext ctx = browser.newContext();
        // store on thread local
        context.set(ctx);

        // Start tracing in the context
        ctx.tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );
        // Share the context in TestNG locker for next steps (onTestFailure())
        result.setAttribute(TestListener.CONTEXT_KEY, ctx);

        Page pg = ctx.newPage();
        page.set(pg);
    }

    public Page getPage() {
        return page.get();
    }

    public BrowserContext getContext() {
        return context.get();
    }

    protected void launchBrowserPage(String url){
        getPage().navigate(ConfigManager.getBaseUrl() + url, new Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE));
    }

    @AfterMethod
    protected void tearDownMethod(){
        context.remove();
        page.remove();
    }

    @AfterClass
    protected void tearDown() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
