package com.earlyadopter.backend.dto.document.product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@Document(indexName = "md-index")
public class MD_INDEX {
    @Id
    private String mdId;            // 상품 ID
    private String mdNm;            // 상품명
    private String mdNo;            // 품목명
    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Date cellDate;          // 출고일
    @Field(type = FieldType.Keyword)
    private String imgLink;         // 상품 이미지(링크)
    private String category;        // 카테고리
    private Integer mdPrice;        // 가격
    private String brandId;         // 브랜드 ID
    private String brandNm;         // 브랜드 이름

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MD_INDEX mdIndex = (MD_INDEX) o;
        return Objects.equals(mdId, mdIndex.mdId) && Objects.equals(mdNm, mdIndex.mdNm) && Objects.equals(mdNo, mdIndex.mdNo) && Objects.equals(cellDate, mdIndex.cellDate) && Objects.equals(imgLink, mdIndex.imgLink) && Objects.equals(category, mdIndex.category) && Objects.equals(mdPrice, mdIndex.mdPrice) && Objects.equals(brandId, mdIndex.brandId) && Objects.equals(brandNm, mdIndex.brandNm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mdId, mdNm, mdNo, cellDate, imgLink, category, mdPrice, brandId, brandNm);
    }
}
