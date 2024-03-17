package com.earlyadopter.backend.dto.document.product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BRAND_INDEX that = (BRAND_INDEX) o;
        return Objects.equals(brandId, that.brandId) && Objects.equals(brandNm, that.brandNm) && Objects.equals(brandLogo, that.brandLogo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandId, brandNm, brandLogo);
    }
}
