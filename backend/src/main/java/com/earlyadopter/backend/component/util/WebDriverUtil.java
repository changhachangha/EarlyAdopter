package com.earlyadopter.backend.component.util;

import com.earlyadopter.backend.service.api.CrawlingService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebDriverUtil {
    private static final Logger logger = LoggerFactory.getLogger(CrawlingService.class);
    public static WebDriver getChromeDriver(String urlPath) {

        logger.info("getChromeDriver method start ");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        logger.info("After ChromeOptions initialize and set build option ");
        WebDriver driver = new ChromeDriver(options);
        driver.get(urlPath);

        try {Thread.sleep(1000);} catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return driver;

    }

}
