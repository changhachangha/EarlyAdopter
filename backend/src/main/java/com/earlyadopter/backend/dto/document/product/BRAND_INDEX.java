package com.earlyadopter.backend.dto.document.product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Document(indexName = "brand-index")
public class BRAND_INDEX {
    @Id
    private String brandId;         // 브랜드 ID
    private String brandNm;         // 브랜드 이름
    @Field(type = FieldType.Keyword)
    private String brandLogo;       // 브랜드 로고
    private String urlPath;         // urlPath
    private List<MD_INDEX> mdList;  // 상품 리스트

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BRAND_INDEX that = (BRAND_INDEX) o;
        return Objects.equals(brandId, that.brandId) && Objects.equals(brandNm, that.brandNm) && Objects.equals(brandLogo, that.brandLogo) && Objects.equals(urlPath, that.urlPath) && Objects.equals(mdList, that.mdList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brandId, brandNm, brandLogo, urlPath, mdList);
    }
}
