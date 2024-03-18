package com.earlyadopter.backend.service.api;

import com.earlyadopter.backend.component.util.WebDriverUtil;
import com.earlyadopter.backend.dto.document.product.BRAND_INDEX;
import com.earlyadopter.backend.repository.product.BrandIndexRepository;

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
public class CrawlingService {

    private final BrandIndexRepository brandIndexRepository;
    private static final Logger logger = LoggerFactory.getLogger(CrawlingService.class);

    public CrawlingService(BrandIndexRepository brandIndexRepository) {
        this.brandIndexRepository = brandIndexRepository;
    }

    public Iterable<BRAND_INDEX> addNewCategories() {

        logger.info("addNewCategoriesService Start");

        WebDriver driver = WebDriverUtil.getChromeDriver("https://www.musinsa.com/brands?categoryCode=&type=&sortCode=BRAND_RANK&page=1&size=100");
        Document doc = Jsoup.parse(driver.getPageSource());

        int totalPagingNum = Integer.parseInt(doc.select("a.fa.fa-angle-double-right.paging-btn.btn.last").attr("value"));

        String[] urlPaths = new String[totalPagingNum + 1];
        for (int i = 1; i <= totalPagingNum; i++)
            urlPaths[i - 1] = "https://www.musinsa.com/brands?categoryCode=&type=&sortCode=BRAND_RANK&page=" + i + "&size=100";

        Set<String> linkSet = new HashSet<>();

        for (String urlPath : urlPaths) {

            doc = getDocument(driver, urlPath);
            if (doc == null) break;

            Elements dtElements = doc.select("dt");

            for (Element dt : dtElements) {

                String link = Objects.requireNonNull(dt.selectFirst("a")).attr("href");

                logger.info("link [{}] ", link);
                linkSet.add("https://www.musinsa.com" + link);
            }


        }

        logger.info("total BrandName : [{}] ", linkSet.size());

        List<BRAND_INDEX> brandList = new ArrayList<>();

        for (String urlPath : linkSet) {

            doc = getDocument(driver, urlPath);
            if (doc == null) break;
            Element element = doc.selectFirst(".brand_logo.brandLogo img");

            if (element != null) {

                String brandNm = element.attr("alt");
                String imageUrl = "https:" + element.attr("src");

                BRAND_INDEX brandIndex = new BRAND_INDEX();
                brandIndex.setBrandNm(brandNm);
                brandIndex.setBrandLogo(imageUrl);
                brandList.add(brandIndex);

            }
        }

        for (BRAND_INDEX brandIndex : brandList) {

            logger.info("brandNm [{}] ", brandIndex.getBrandNm());
            logger.info("brandLogo [{}] ", brandIndex.getBrandLogo());
        }

        brandIndexRepository.saveAll(brandList);

        try {Thread.sleep(1000);} catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        logger.info("addNewCategoriesService End");
        try {

            if(driver != null) {


                logger.info("Driver close");
                //드라이버 연결 종료s
                driver.close();
                //프로세스 종료
                driver.quit();
            }

        } catch (Exception e) {
            logger.info("Exception [{}]", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }

    private Document getDocument(WebDriver driver, String urlPath) {
        Document doc;
        if(urlPath == null) return null;
        logger.info("urlPath [{}] ", urlPath);
        driver.navigate().to(urlPath);
        try {Thread.sleep(1000);} catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        doc = Jsoup.parse(driver.getPageSource());
        return doc;
    }
}
