package utils;

import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;

public class AllureUtils {

    // BYPASSES ANNOTATIONS: Safely injects the image bytes directly into Allure
    public static void attachScreenshot(byte[] screenshot) {
        if (screenshot != null && screenshot.length > 0) {
            Allure.addAttachment("Failure Screenshot", "image/png",
                    new ByteArrayInputStream(screenshot), ".png");
        }
    }

    public static void attachTrace(String tracePath) {
        try (FileInputStream fis = new FileInputStream(tracePath)) {
            Allure.addAttachment("Playwright Execution Trace Log", "application/zip",
                    fis, ".zip");
        } catch (Exception e) {
            System.err.println("Failed to attach trace: " + e.getMessage());
        }
    }

    public static void attachStackTrace(Throwable e) {
        if (e != null) {
            Allure.addAttachment("Error Stacktrace", "text/plain", e.toString());
        }
    }
}
