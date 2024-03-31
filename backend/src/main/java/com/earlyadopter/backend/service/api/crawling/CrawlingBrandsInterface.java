package com.earlyadopter.backend.service.api.crawling;

import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;


public interface CrawlingBrandsInterface {

    Logger logger = LoggerFactory.getLogger(MusinsaCrawlingService.class);

    void addNewBrands();

    class ElementExtractor implements Runnable {

        private final String urlPath;
        private final ThreadLocal<WebDriver> driverThreadLocal;
        private final String cssQuery;
        @Getter
        private Elements elements;

        public ElementExtractor(String urlPath, ThreadLocal<WebDriver> driverThreadLocal, String cssQuery) {
            this.urlPath = urlPath;
            this.driverThreadLocal = driverThreadLocal;
            this.cssQuery = cssQuery;
        }

        @Override
        public void run() {

            WebDriver driver = driverThreadLocal.get();

            if (urlPath == null) {
                logger.error("urlPath is null");
                return;
            }

            driver.navigate().to(urlPath);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

            Document urlDoc;

            synchronized (this) {
                urlDoc = Jsoup.parse(driver.getPageSource());
            }

            synchronized (this) {
                elements = urlDoc.select(cssQuery);
            }

        }

    }
}
