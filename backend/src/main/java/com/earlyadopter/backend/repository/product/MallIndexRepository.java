package com.earlyadopter.backend.repository.product;

import com.earlyadopter.backend.dto.document.product.BRAND_INDEX;
import com.earlyadopter.backend.dto.document.product.MALL_INDEX;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MallIndexRepository extends ElasticsearchRepository<MALL_INDEX, String> {

    MALL_INDEX findByMallNm(String name);
}
