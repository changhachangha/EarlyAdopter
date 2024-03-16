package com.earlyadopter.backend.controller.api;

import com.earlyadopter.backend.service.api.CrawlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crawling")
public class CrawlingController {

    private final CrawlingService crawlingService;

    @Autowired
    public CrawlingController(CrawlingService crawlingService) {
        this.crawlingService = crawlingService;
    }

    @GetMapping("/musinsa")
    public void musinsaCrawling() {

        crawlingService.addNewCategories("https://www.musinsa.com/app/");
//        crawlingService.addNewCategories("https://www.musinsa.com/brands/");

    }
}
