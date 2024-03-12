package com.earlyadopter.backend.repository.product;

import com.earlyadopter.backend.dto.document.product.BRAND_INDEX;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandIndexRepository extends ElasticsearchRepository<BRAND_INDEX, String> {
}
