package com.earlyadopter.backend.dto.document.product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Set;

@Getter
@Setter
@ToString
@Document(indexName = "mall-index")
public class MALL_INDEX {

    @Id
    private String mallId;                  // 상품몰 ID
    private String mallNm;                  // 상품몰 이름
    private Set<BRAND_INDEX> brandIndex;    // 상품몰 보유 브랜드 목록
}
