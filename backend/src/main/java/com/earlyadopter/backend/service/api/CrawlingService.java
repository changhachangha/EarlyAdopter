package com.earlyadopter.backend.service.api;

import com.earlyadopter.backend.component.util.WebDriverUtil;
import com.earlyadopter.backend.dto.document.product.BRAND_INDEX;
import com.earlyadopter.backend.repository.product.BrandIndexRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.openqa.selenium.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@Service
public class CrawlingService extends Thread{

    private final BrandIndexRepository brandIndexRepository;
    private static final Logger logger = LoggerFactory.getLogger(CrawlingService.class);

    public CrawlingService(BrandIndexRepository brandIndexRepository) {
        this.brandIndexRepository = brandIndexRepository;
    }

    public void addNewCategories() {

        logger.info("addNewCategoriesService Start");

        // 중복 저장 방지를 위해 활용할 객체
        Iterable<BRAND_INDEX> iterable = brandIndexRepository.findAll();
        Map<String, String> brandMapforDupCheckMap = new HashMap<>();
        for (BRAND_INDEX brandIndex : iterable) brandMapforDupCheckMap.put(brandIndex.getBrandNm(), brandIndex.getBrandLogo());

        // Webdriver initialize
        WebDriver driver = WebDriverUtil.getChromeDriver("https://www.musinsa.com/brands?categoryCode=&type=&sortCode=BRAND_RANK&page=1&size=100");
        Document doc = Jsoup.parse(driver.getPageSource());

        // 브랜드 총 페이지 수 가져오기
        int totalPagingNum = Integer.parseInt(doc.select("a.fa.fa-angle-double-right.paging-btn.btn.last").attr("value"));

        // 브랜드 페이지 별 urlPath initialize
        String[] urlPaths = new String[totalPagingNum];
        for (int i = 1; i <= totalPagingNum; i++)
            urlPaths[i - 1] = "https://www.musinsa.com/brands?categoryCode=&type=&sortCode=BRAND_RANK&page=" + i + "&size=100";

        // 브랜드 별 상세 페이지 링크를 담아두기 위한 Set
        Set<String> linkSet = new HashSet<>();

        for (String urlPath : urlPaths) {

            // Document initialize
            Document urlDoc = getDocument(driver, urlPath);
            if (urlDoc != null) {

                // dt 태그를 가져온 뒤
                Elements dtElements = urlDoc.select("dt");
                for (Element dt : dtElements) {

                    // a 태그에서 브랜드 별 상세 페이지 링크를 가져온다.
                    String link = Objects.requireNonNull(dt.selectFirst("a")).attr("href");
                    logger.info("link [{}]", link);
                    linkSet.add("https://www.musinsa.com" + link);
                }
            }
        }

        logger.info("after executor link");
        logger.info("linkSet [{}] ", linkSet);
        logger.info("total brandLink size : [{}] ", linkSet.size());

        // 브랜드 인덱스를 저장하기 위한 변수
        Set<BRAND_INDEX> brandList = new HashSet<>();

        for (String urlPath : linkSet) {

            // Document initialize
            Document urlDoc = getDocument(driver, urlPath);

            if (urlDoc != null) {

                // img 태그 중 brand_logo brandLogo 클래스 명으로 가져온다.
                Element element = urlDoc.selectFirst(".brand_logo.brandLogo img");
                if (element != null) {

                    // alt 속성 값이 브랜드 이름이고
                    String brandNm = element.attr("alt");
                    // src 속성 값이 브랜드 이미지 링크
                    String imageUrl = "https:" + element.attr("src");

                    // 기존 Repository 에서 brandNm과 브랜드 로고 링크가 일치하지 않을 경우에만 인덱스 객체 생성
                    if ((!brandMapforDupCheckMap.containsKey(brandNm) || brandMapforDupCheckMap.get(brandNm).equals(imageUrl))) {

                        // 브랜드 인덱스 객체 생성 후
                        BRAND_INDEX brandIndex = new BRAND_INDEX();
                        brandIndex.setBrandNm(brandNm);
                        brandIndex.setBrandLogo(imageUrl);

                        // Set에 저장
                        brandList.add(brandIndex);
                    }
                }
            }
        }

        logger.info("brandList [{}] ", brandList);
        // brandList Elasticsearch 에 저장
        Iterable<BRAND_INDEX> brandIndexList =  brandIndexRepository.saveAll(brandList);

        String fileName = new Date().getTime() + " musinsa_brand_csv_output.csv";

        logger.info("CSV file output start, filename : " + fileName);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {

            for (BRAND_INDEX brandIndex : brandIndexList) {

                writer.write("ID : ");
                writer.write(brandIndex.getBrandId());
                writer.write(", \t BrandNm : ");
                writer.write(brandIndex.getBrandNm());
                writer.write(", \t BrandLogoLink : ");
                writer.write(brandIndex.getBrandLogo());

                writer.newLine();
            }

        }  catch (IOException e) {
            logger.info("Exception occur [{}] ", e.getMessage());
            throw new RuntimeException(e);
        }

        logger.info("CSV file output complete");
        logger.info("addNewCategoriesService End");

        //드라이버 연결 종료s
        driver.close();
        //프로세스 종료
        driver.quit();

        logger.info("Driver close");

    }

    // 다큐먼트 객체 생성용 메서드
    private static Document getDocument(WebDriver driver, String urlPath) {

        // urlPath가 비어있을 경우 null 반환
        if(urlPath == null) return null;

        // 경로 urlPath로 재설정
        driver.navigate().to(urlPath);

        // Jsoup로 파싱 후 반환
        return Jsoup.parse(driver.getPageSource());
    }


}
