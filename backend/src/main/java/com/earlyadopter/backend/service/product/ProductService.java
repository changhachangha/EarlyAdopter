package com.earlyadopter.backend.service.product;

import com.earlyadopter.backend.dto.document.product.BRAND_INDEX;
import com.earlyadopter.backend.repository.product.BrandIndexRepository;
import com.earlyadopter.backend.service.api.CrawlingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(CrawlingService.class);
    private final BrandIndexRepository brandIndexRepository;

    public ProductService(BrandIndexRepository brandIndexRepository) {
        this.brandIndexRepository = brandIndexRepository;
    }

    public Iterable<BRAND_INDEX> findAllBrand() {

        logger.info("find all brand service method start");
        return brandIndexRepository.findAll();
    }
    public BRAND_INDEX findBrandByName(String name) {

        logger.info("find brand by name service method start");
        return brandIndexRepository.findByBrandNm(name);
    }

    public Iterable<BRAND_INDEX> addNewBrand(List<BRAND_INDEX> brandIndex) {

        logger.info("add new brand service method start");
        return brandIndexRepository.saveAll(brandIndex);
    }

    public Iterable<BRAND_INDEX> deleteBrand(List<BRAND_INDEX> brandIndex) {

        logger.info("delete all brand service method start");
        brandIndexRepository.deleteAll(brandIndex);

        return brandIndexRepository.findAll();
    }


    public void deleteAllBrand() {

        logger.info("delete all brand service method start");
        brandIndexRepository.deleteAll();

    }
}
