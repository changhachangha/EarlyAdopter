package com.earlyadopter.backend.repository.product;

import com.earlyadopter.backend.dto.document.product.MD_INDEX;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MdIndexRepository extends ElasticsearchRepository<MD_INDEX, String> {
}
