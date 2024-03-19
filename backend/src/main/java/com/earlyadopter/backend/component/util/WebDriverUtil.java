package com.earlyadopter.backend.component.util;

import com.earlyadopter.backend.service.api.CrawlingService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class WebDriverUtil {
    private static final Logger logger = LoggerFactory.getLogger(CrawlingService.class);
    public static WebDriver getChromeDriver(String urlPath) {

        logger.info("getChromeDriver method start ");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--log-level=3");

        logger.info("After ChromeOptions initialize and set build option ");
        WebDriver driver = new ChromeDriver(options);

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10000));
        driver.get(urlPath);

        return driver;

    }

}
