package com.earlyadopter.backend.dto.document.product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@ToString
@Document(indexName = "brand-index")
public class BRAND_INDEX {
    @Id
    private String brandId;
    private String brandNm;
    @Field(type = FieldType.Keyword)
    private String brandLogo;

}
