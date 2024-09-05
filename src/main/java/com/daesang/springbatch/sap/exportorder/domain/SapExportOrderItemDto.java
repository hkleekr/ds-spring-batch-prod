package com.daesang.springbatch.sap.exportorder.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * fileName         : SapExportOrderItemDto
 * author           : inayoon
 * date             : 2022-11-08
 * description      : 주문관리 - 수출 주문정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-08       inayoon             최초생성
 */
@Getter
@Setter
@NoArgsConstructor
public class SapExportOrderItemDto {
    // 주문번호
    @JsonAlias(value = "VBELN")
    private String orderNumber;

    // 판매문서항목(항목번호)
    @JsonAlias(value = "POSNR")
    private String itemNo;

    // 자재코드(규격)
    @JsonAlias(value = "MATNR")
    private String productCode;

    // 자재명(규격명)
    @JsonAlias(value = "MAKTX")
    private String productName;

    // 주문수량
    @JsonAlias(value = "KWMENG")
    private String orderQuantity;

    // 주문수량 단위
    @JsonAlias(value = "VRKME")
    private String orderQuantityUnit;

    // 판매중량(순중량)
    @JsonAlias(value = "NTGEW")
    private String netWeight;

    // 판매중량 단위(순중량 단위)
    @JsonAlias(value = "GEWEI")
    private String netWeightUnit;

    // 주문정가(주문금액)
    @JsonAlias(value = "NETWR")
    private String orderPrice;

    // 통화
    @JsonAlias(value = "WAERK")
    private String orderPriceCurrency;

    // 중량단가(평균단가)
    @JsonAlias(value = "AVGNT")
    private String weightunitprice;
}
