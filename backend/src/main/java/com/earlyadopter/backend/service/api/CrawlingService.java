package com.earlyadopter.backend.service.api;

import com.earlyadopter.backend.dto.document.product.BRAND_INDEX;
import com.earlyadopter.backend.repository.product.BrandIndexRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

@Service
public class CrawlingService {

    private final BrandIndexRepository brandIndexRepository;
    private static final Logger logger = LoggerFactory.getLogger(CrawlingService.class);
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver"; //드라이버 ID
    public static final String WEB_DRIVER_PATH = "C:\\DevTool\\chromedriver-win64\\chromedriver.exe"; //드라이버 경로

    public CrawlingService(BrandIndexRepository brandIndexRepository) {
        this.brandIndexRepository = brandIndexRepository;
    }

    public Iterable<BRAND_INDEX> addNewCategories(String urlPath) {

        logger.info("addNewCategories service start ");

        try {
            System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //크롬 설정을 담은 객체 생성
        ChromeOptions options = new ChromeOptions();
        //브라우저가 눈에 보이지 않고 내부적으로 돈다.
        //설정하지 않을 시 실제 크롬 창이 생성되고, 어떤 순서로 진행되는지 확인할 수 있다.
        options.addArguments("headless");

        //위에서 설정한 옵션은 담은 드라이버 객체 생성
        //옵션을 설정하지 않았을 때에는 생략 가능하다.
        //WebDriver객체가 곧 하나의 브라우저 창이라 생각한다.
        WebDriver driver = new ChromeDriver(options);

        //WebDriver을 해당 url로 이동한다.
        driver.get(urlPath);

        //브라우저 이동시 생기는 로드시간을 기다린다.
        //HTTP응답속도보다 자바의 컴파일 속도가 더 빠르기 때문에 임의적으로 1초를 대기한다.
        try {Thread.sleep(1000);} catch (InterruptedException e) {}

        //class="nav" 인 모든 태그를 가진 WebElement리스트를 받아온다.
        //WebElement는 html의 태그를 가지는 클래스이다.
        List<WebElement> brandName = driver.findElements(By.className("p.txt_tit_brand"));
        logger.info("brandName is [{}] ", brandName);
//        for (int i = 0; i < el1.size(); i++) {
//            //nav 클래스의 객체 중 "뉴스"라는 텍스트를 가진 WebElement를 클릭한다.
//            if(el1.get(i).getText().equals("뉴스")) {
//                el1.get(i).click();
//                break;
//            }
//        }

        //1초 대기
        try {Thread.sleep(1000);} catch (InterruptedException e) {}

        try {
            //드라이버가 null이 아니라면
            if(driver != null) {
                //드라이버 연결 종료
                driver.close(); //드라이버 연결 해제

                //프로세스 종료
                driver.quit();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

//        Connection con = Jsoup.connect(urlPath);
//        Document doc = null;
//
//        try {
//
//            doc = con.get();
//
//        } catch (IOException e) {
//
//            logger.info("exception occurred: [{}]", e.getMessage(), e);
//            throw new RuntimeException();
//
//        }
//
//        logger.info("Document is [{}] ", doc);
//
//        Element titleBoxs = doc.selectFirst(".brand_popup .title-box");
//
//        logger.info("BRAND is [{}] ", titleBoxs);
//
//        if (titleBoxs != null) {
//            // Extract the text content inside the span
//            String brandName = titleBoxs.ownText().trim();
//            System.out.println("Brand Name: " + brandName);
//        } else {
//            System.out.println("Brand Name not found.");
//        }
//        return null;

        return null;
    }
}
