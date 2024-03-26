package com.earlyadopter.backend.controller.product;

import com.earlyadopter.backend.dto.document.product.BRAND_INDEX;
import com.earlyadopter.backend.service.api.CrawlingService;
import com.earlyadopter.backend.service.product.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(CrawlingService.class);

    @Autowired
    public ProductController(ProductService productService) { this.productService = productService; }

    @GetMapping("/all")
    public ResponseEntity<Iterable<BRAND_INDEX>> findAllBrand() {

        logger.info("find all brand method start");
        return ResponseEntity.ok(productService.findAllBrand());
    }

    @GetMapping("/{name}")
    public ResponseEntity<BRAND_INDEX> findBrandByName(@PathVariable String name) {

        logger.info("find brand by name method start");
        try {

            return ResponseEntity.ok(productService.findBrandByName(name));

        } catch (Exception e) {

            logger.info(e.getMessage());
            throw new RuntimeException(e);

        }

    }
    @PostMapping()
    public ResponseEntity<Iterable<BRAND_INDEX>> insertBrand(@RequestBody List<BRAND_INDEX> brandIndex) {

        logger.info("insert brand method start");
        return ResponseEntity.ok(productService.addNewBrand(brandIndex));
    }

    @DeleteMapping()
    public ResponseEntity<Iterable<BRAND_INDEX>> deleteBrand(@RequestBody List<BRAND_INDEX> brandIndex) {

        logger.info("delete brand method start");
        return ResponseEntity.ok(productService.deleteBrand(brandIndex));
    }

    @PostMapping("/all")
    public ResponseEntity<BRAND_INDEX> deleteAllBrand() {

        logger.info("delete all brand method start");
//        productService.deleteAllBrand();
        return ResponseEntity.ok().build();
    }
}
