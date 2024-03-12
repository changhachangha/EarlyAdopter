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

@Getter
@Setter
@ToString
@Document(indexName = "md-index")
public class MD_INDEX {
    @Id
    private String mdId;
    private String mdNm;
    private String mdNo;
    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Date cellDate;
    @Field(type = FieldType.Keyword)
    private String imgLink;
    private String category;
    private Integer mdPrice;
    private String brandId;
    private String brandNm;
}
