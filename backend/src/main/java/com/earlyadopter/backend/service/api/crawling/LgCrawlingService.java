package com.earlyadopter.backend.service.api.crawling;

import com.earlyadopter.backend.component.util.WebDriverUtil;
import com.earlyadopter.backend.dto.document.product.BRAND_INDEX;
import com.earlyadopter.backend.dto.document.product.MALL_INDEX;
import com.earlyadopter.backend.dto.document.product.MD_INDEX;
import com.earlyadopter.backend.repository.product.BrandIndexRepository;
import com.earlyadopter.backend.repository.product.MallIndexRepository;
import com.earlyadopter.backend.repository.product.MdIndexRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class LgCrawlingService implements  CrawlingBrandsInterface {

    private static final Logger logger = LoggerFactory.getLogger(MusinsaBrandCrawlingService.class);
    private final BrandIndexRepository brandIndexRepository;
    private final MallIndexRepository mallIndexRepository;
    private final MdIndexRepository mdIndexRepository;
    private static Set<MD_INDEX> mdSet;
    private static Map<String, MD_INDEX> mdDupCheckList;
    private static String BRAND_ID;
    @Autowired
    public LgCrawlingService(BrandIndexRepository brandIndexRepository, MallIndexRepository mallIndexRepository, MdIndexRepository mdIndexRepository) {
        this.brandIndexRepository = brandIndexRepository;
        this.mallIndexRepository = mallIndexRepository;
        this.mdIndexRepository = mdIndexRepository;
    }

    @Override
    public void addNewBrands() {

        logger.info("addNewCategoriesService Start");

        logger.info("test [{}] ", mallIndexRepository.findByMallNm("LG"));
        // 대상 사이트 없을 경우 초기화
        if (mallIndexRepository.findByMallNm("LG") == null ) {

            MALL_INDEX mallIndex = new MALL_INDEX();
            mallIndex.setMallNm("LG");
            mallIndex.setBrandIndex(new HashSet<>());
            mallIndexRepository.save(mallIndex);

        }

        logger.info("test [{}] ", brandIndexRepository.findByBrandNm("LG"));
        // 대상 브랜드 없을 경우 초기화
        if(brandIndexRepository.findByBrandNm("LG") == null) {

            BRAND_INDEX brandIndex = new BRAND_INDEX();
            brandIndex.setBrandNm("LG");
            brandIndex.setBrandLogo("https://www.lge.co.kr/lg5-common/images/header/lg_logo_new.svg");
            brandIndex.setUrlPath("https://www.lge.co.kr/");
            brandIndex.setMdList(new ArrayList<>());

            brandIndexRepository.save(brandIndex);

            MALL_INDEX mallIndex = mallIndexRepository.findByMallNm("LG");
            mallIndex.getBrandIndex().add(brandIndex);

        }

        BRAND_ID = brandIndexRepository.findByBrandNm("LG").getBrandId();
        logger.info("test [{}] ", brandIndexRepository.findByBrandNm("LG"));

        // 중복 저장 방지를 위해 활용할 객체
        List<MD_INDEX> set = brandIndexRepository.findByBrandNm("LG").getMdList();

        mdDupCheckList = Collections.synchronizedMap(new HashMap<>());
        for (MD_INDEX mdIndex : set) {

            mdDupCheckList.put(mdIndex.getMdNo(), mdIndex);
        }

        // Webdriver initialize
        WebDriver driver = WebDriverUtil.getChromeDriver();
        String url = "https://www.lge.co.kr/tvs";
        mdSet = Collections.synchronizedSet(new HashSet<>());

        int cpuCore = Runtime.getRuntime().availableProcessors();
        int pageNumber = 1;

        ExecutorService pool = Executors.newFixedThreadPool(cpuCore);
        ThreadLocal<WebDriver> driverThreadLocal = ThreadLocal.withInitial(WebDriverUtil::getChromeDriver);

        driver.navigate().to(url);
        while (hasNextPage(driver)) {

            pool.execute(new getElementsOfMD(url, driverThreadLocal));

            nextPage(driver);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }

        // 병렬 작업 처리 완료 후 셧다운. isTerminated()로 thread 종료 후 실행되도록
        pool.shutdown();
        while (true) {
            if (pool.isTerminated()) break;
        }

        for (MD_INDEX mdIndex : mdSet) {

            logger.info("md : [{}] ", mdIndex);
        }

        mdIndexRepository.saveAll(mdSet);

        driver.quit();
    }


    private record getElementsOfMD(String urlPath,
                                          ThreadLocal<WebDriver> driverThreadLocal) implements Runnable {

        @Override
        public void run() {

            logger.info("urlPath [{}] ", urlPath);
            ElementExtractor extractor = new ElementExtractor(urlPath, driverThreadLocal, ".card-list-item[role='listitem']");
            extractor.run();
            Elements elements = extractor.getElements();


            for (Element element : elements) {
                String dataEcProduct = element.getAllElements().attr("data-ec-product");
                JsonObject jsonObject = JsonParser.parseString(dataEcProduct).getAsJsonObject();

                MD_INDEX mdIndex = new MD_INDEX();
                mdIndex.setMdNm(String.valueOf(jsonObject.get("model_name")).replaceAll("\"", ""));
                mdIndex.setMdNo(String.valueOf(jsonObject.get("model_id")).replaceAll("\"", ""));
                mdIndex.setCategory(String.valueOf(jsonObject.get("model_category")).replaceAll("\"", ""));
                mdIndex.setMdPrice(Integer.parseInt(String.valueOf(jsonObject.get("price")).replaceAll(",", "").replaceAll("\"", "")));
                mdIndex.setBrandId(BRAND_ID);
                mdIndex.setBrandNm("LG");

                if (!mdDupCheckList.containsKey(mdIndex.getMdNo())) mdSet.add(mdIndex);
            }
        }
    }

    // 다음 페이지 찾는 메서드
    private static boolean hasNextPage(WebDriver driver) {

        WebElement nextButton = driver.findElement(By.xpath("//*[@id=\"_plpComponent\"]/section/section[2]/section[2]/div/div[4]/a[3]"));

        logger.info("nextButton [{}]", nextButton);
        return !nextButton.getAttribute("class").contains("disabled");
    }

    private static void nextPage(WebDriver driver) {

        WebElement nextButton = driver.findElement(By.xpath("//*[@id=\"_plpComponent\"]/section/section[2]/section[2]/div/div[4]/a[3]"));
        nextButton.click();
    }

    /*


     */
}
