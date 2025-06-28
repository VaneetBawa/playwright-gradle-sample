package tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SampleTest {
    static Playwright playwright;
    static Browser browser;
    static BrowserContext context;
    static Page page;

    @BeforeAll
    static void setup() throws Exception {
        playwright = Playwright.create();

        String capabilitiesJson = """
        {
          "browserName": "Chrome",
          "browserVersion": "latest",
          "LT:Options": {
            "platform": "Windows 11",
            "build": "Playwright Java Gradle Build",
            "name": "Sample Test",
            "user": "",
            "accessKey": "",
            "network": true,
            "video": true,
            "console": true,
            "playwrightClientVersion": "1.44.0"
          }
        }
        """.formatted(System.getProperty("LT_USERNAME"), System.getProperty("LT_ACCESS_KEY"));

        String encodedCaps = URLEncoder.encode(capabilitiesJson, StandardCharsets.UTF_8);
        String cdpUrl = "wss://cdp.lambdatest.com/playwright?capabilities=" + encodedCaps;

        browser = playwright.chromium().connect(cdpUrl);
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    void testLambdaTestHomepage() {
        page.navigate("https://www.lambdatest.com/");
        Assertions.assertTrue(page.title().contains("LambdaTest"));
    }

    @AfterAll
    static void teardown() {
        if (page != null) page.close();
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
