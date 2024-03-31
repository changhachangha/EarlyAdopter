package com.earlyadopter.backend.service.product;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import com.earlyadopter.backend.dto.document.product.BRAND_INDEX;
import com.earlyadopter.backend.repository.product.BrandIndexRepository;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final BrandIndexRepository brandIndexRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public ProductService(BrandIndexRepository brandIndexRepository, ElasticsearchOperations elasticsearchOperations) {
        this.brandIndexRepository = brandIndexRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public int totalPageNumberOfBrandIndex() {

        long totalIndex = brandIndexRepository.count();
        logger.info("totalIndexCount : [{}] ", totalIndex);

        return (int) (totalIndex % 200 == 0 ? totalIndex / 200 : totalIndex / 200 + 1);

    }

    public Iterable<BRAND_INDEX> findAllBrandWithPageable(int pageNo) {

        logger.info("find all brand service method start");

        PageRequest pageRequest = PageRequest.of(pageNo, 200);

        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.matchAll(new MatchAllQuery.Builder().build()))
                .withPageable(pageRequest)
                .build();

        SearchHits<BRAND_INDEX> searchHits = elasticsearchOperations.search(query, BRAND_INDEX.class);

        List<BRAND_INDEX> resultSet = new ArrayList<>();

        for (val searchHit : searchHits) {

            BRAND_INDEX brandIndex = searchHit.getContent();

            brandIndex.setBrandId(brandIndex.getBrandId());
            brandIndex.setBrandNm(brandIndex.getBrandNm());
            brandIndex.setBrandLogo(brandIndex.getBrandLogo());

            resultSet.add(brandIndex);
        }

        logger.info("resultSet Size [{}] ", resultSet.size());
        return resultSet;
    }

    public Iterable<BRAND_INDEX> findAllBrand() {
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
