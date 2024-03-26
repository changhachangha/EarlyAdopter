package com.earlyadopter.backend.service.api;

import com.earlyadopter.backend.component.util.WebDriverUtil;
import com.earlyadopter.backend.dto.document.product.BRAND_INDEX;
import com.earlyadopter.backend.repository.product.BrandIndexRepository;

import java.io.*;

import java.nio.charset.StandardCharsets;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.openqa.selenium.WebDriver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@Service
public class CrawlingService {

    private final BrandIndexRepository brandIndexRepository;
    private static final Logger logger = LoggerFactory.getLogger(CrawlingService.class);
    private static Set<String> linkSet;
    private static Set<BRAND_INDEX> brandList;
    private static Map<String, String> brandMapforDupCheckMap;
    private static WebDriver driver;

    public CrawlingService(BrandIndexRepository brandIndexRepository) {
        this.brandIndexRepository = brandIndexRepository;
    }

    // 동기화 메서드. synchronized 사용
    public synchronized void addNewCategories() {

        logger.info("addNewCategoriesService Start");

        // 중복 저장 방지를 위해 활용할 객체
        Iterable<BRAND_INDEX> iterable = brandIndexRepository.findAll();
        brandMapforDupCheckMap = new HashMap<>();
        for (BRAND_INDEX brandIndex : iterable)
            brandMapforDupCheckMap.put(brandIndex.getBrandNm(), brandIndex.getBrandLogo());

        // Webdriver initialize
        driver = WebDriverUtil.getChromeDriver();
        driver.navigate().to("https://www.musinsa.com/brands?categoryCode=&type=&sortCode=BRAND_RANK&page=1&size=100");
        Document doc = Jsoup.parse(driver.getPageSource());

        // 브랜드 총 페이지 수 가져오기
        int totalPagingNum = Integer.parseInt(doc.select("a.fa.fa-angle-double-right.paging-btn.btn.last").attr("value"));

        // 브랜드 페이지 별 urlPath initialize
        String[] urlPaths = new String[totalPagingNum];
        for (int i = 1; i <= totalPagingNum; i++)
            urlPaths[i - 1] = "https://www.musinsa.com/brands?categoryCode=&type=&sortCode=BRAND_RANK&page=" + i + "&size=100";

        logger.info("total page Num [{}] ", totalPagingNum);
        // 브랜드 별 상세 페이지 링크를 담아두기 위한 Set, 동기화 사용을 위해 synchronizedSet 사용
        linkSet = Collections.synchronizedSet(new HashSet<>());

        // 서버 CPU에서 사용 가능한 processor 수를 가져와 threadPool의 크기로 한다.
        int cpuCore = Runtime.getRuntime().availableProcessors();
        logger.info("Cpu Core Count [{}] ", cpuCore);
        ExecutorService pool = Executors.newFixedThreadPool(cpuCore);

        // CPU Core 수에 맞춰 병렬 스레드 처리
        for (String urlPath : urlPaths) {
            pool.execute(new getElementsOfBrandLink(urlPath));
        }

        // 병렬 작업 처리 완료 후 셧다운. isTerminated()로 thread 종료 후 실행되도록
        pool.shutdown();
        while (true) {
            if (pool.isTerminated()) break;
        }

        logger.info("after executor link");
        logger.info("total brandLink size : [{}] ", linkSet.size());

        // 브랜드 인덱스를 저장하기 위한 변수, 동기화 사용을 위해 synchronizedSet 사용
        brandList = Collections.synchronizedSet(new HashSet<>());

        // HashSet에 저장된 링크의 수 만큼 방문하여 브랜드의 이름, 브랜드 이미지를 가져온다.
        pool = Executors.newFixedThreadPool(cpuCore);
        // 병렬 작업 처리
        for (String urlPath : linkSet) pool.execute(new getElementsOfBrandList(urlPath));

        // 완료 후 threadPool shutdown
        pool.shutdown();
        while (true) {
            if (pool.isTerminated()) break;
        }

        logger.info("brandList [{}] ", brandList);
        // brandList Elasticsearch 에 저장
        Iterable<BRAND_INDEX> brandIndexList = brandIndexRepository.saveAll(brandList);

        // 결과값 csv 파일로 저장
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

        } catch (IOException e) {
            logger.info("Exception occur [{}] ", e.getMessage());
            throw new RuntimeException(e);
        }

        logger.info("CSV file output complete");
        logger.info("addNewCategoriesService End");
        // 드라이버 인스턴스 종료
        driver.quit();
    }

    // 브랜드 별 링크 가져오기
    private record getElementsOfBrandLink(String urlPath) implements Runnable {

        @Override
        public void run() {

            if (urlPath == null) {
                logger.error("urlPath is null");
                return;
            }

            driver.navigate().to(urlPath);
            Document urlDoc = Jsoup.parse(driver.getPageSource());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Thread interrupted: {} ", e.getMessage());
            }

            // dt 태그를 가져온 뒤
            Elements dtElements = urlDoc.select("dt");

            // 태그 가져올 때 까지 thread 1초 대기
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Thread interrupted: {} ", e.getMessage());
            }

            for (Element dt : dtElements) {

                // a 태그에서 브랜드 별 상세 페이지 링크를 가져온다.
                String link = Objects.requireNonNull(dt.selectFirst("a")).attr("href");
                linkSet.add("https://www.musinsa.com" + link);
            }
        }
    }

    // 브랜드 링크 별 브랜드 이름, 브랜드 이미지 가져오기
    private record getElementsOfBrandList(String urlPath) implements Runnable {

        @Override
        public void run() {

            // Document initialize
            if (urlPath == null) {
                logger.error("urlPath is null");
                return;
            }

            driver.navigate().to(urlPath);

            Document urlDoc = Jsoup.parse(driver.getPageSource());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Thread interrupted: {} ", e.getMessage());
            }

            // img 태그 중 brand_logo brandLogo 클래스 명으로 가져온다.
            Element element = urlDoc.selectFirst(".brand_logo.brandLogo img");

            // 태그 가져올 때 까지 thread 1초 대기
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Thread interrupted: {} ", e.getMessage());
            }
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

}
