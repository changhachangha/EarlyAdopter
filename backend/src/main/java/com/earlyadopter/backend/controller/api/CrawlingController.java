package com.earlyadopter.backend.controller.api;

import com.earlyadopter.backend.dto.document.product.BRAND_INDEX;
import com.earlyadopter.backend.service.api.crawling.LgCrawlingService;
import com.earlyadopter.backend.service.api.crawling.MusinsaBrandCrawlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crawling")
public class CrawlingController {

    private final MusinsaBrandCrawlingService musinsaBrandCrawlingService;
    private final LgCrawlingService lgCrawlingService;

    @Autowired
    public CrawlingController(MusinsaBrandCrawlingService musinsaBrandCrawlingService, LgCrawlingService lgCrawlingService) {
        this.musinsaBrandCrawlingService = musinsaBrandCrawlingService;
        this.lgCrawlingService = lgCrawlingService;
    }

    @PostMapping("/musinsaBrand")
    public ResponseEntity<BRAND_INDEX> musinsaBrandCrawling() {

        musinsaBrandCrawlingService.addNewBrands();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/lg")
    public ResponseEntity<BRAND_INDEX> lgBrandCrawling() {

        lgCrawlingService.addNewBrands();
        return ResponseEntity.ok().build();
    }
}
