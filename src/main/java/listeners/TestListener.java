package listeners;

import base.BaseUiTest;
import com.microsoft.playwright.*;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.AllureUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestListener implements ITestListener {
    public static final String CONTEXT_KEY = "playwrightContext";

    @Override
    public void onTestFailure(ITestResult result) {
        BaseUiTest test = (BaseUiTest) result.getInstance();

        // Read the context straight out of the TestNG locker!
        BrowserContext context = (BrowserContext) result.getAttribute(CONTEXT_KEY);


        // 1. CAPTURE AND ATTACH SCREENSHOT FIRST (Completely separate from trace)
        if (test != null && test.getPage() != null) {
            try {
                byte[] screenshot = test.getPage().screenshot(new Page.ScreenshotOptions().setFullPage(true));
                // attack to the allure reporting
                AllureUtils.attachScreenshot(screenshot);
            } catch (Exception e) {
                System.err.println("[ALLURE ERROR]: Failed to snap page screenshot: " + e.getMessage());
            }
        }

        // 2. CAPTURE AND ATTACH PLAYWRIGHT TRACES
        if (context != null) {
            String tracePath = "target/allure-results/traces/" + result.getName() + "_failure.zip";
            Path path = Paths.get(tracePath);

            try {
                // FIX: Force system directory creation so Playwright doesn't crash
                Files.createDirectories(path.getParent());

                context.tracing().stop(new Tracing.StopOptions().setPath(path));

                if (Files.exists(path)) {
                    // attack to allure
                    AllureUtils.attachTrace(tracePath);
                    Files.deleteIfExists(path);
                }
            } catch (Exception e) {
                System.err.println("[ALLURE ERROR]: Failed to attach Playwright trace: " + e.getMessage());
            } finally {
                // Ensure the context safely closes down at the absolute end
                try {
                    context.close();
                } catch (Exception ignored) {
                }
            }
        }

        // 3. ATTACH THE STACK TRACE
        try {
            Throwable error = result.getThrowable();
            if (error != null) {
                // attack to allure
                AllureUtils.attachStackTrace(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSuccess(ITestResult result){
        BrowserContext context = (BrowserContext) result.getAttribute(CONTEXT_KEY);

        if (context != null) {
            try {
                context.tracing().stop(new Tracing.StopOptions().setPath(null));
                context.close();
            } catch (Exception ignored) {}
        }
    }
}
