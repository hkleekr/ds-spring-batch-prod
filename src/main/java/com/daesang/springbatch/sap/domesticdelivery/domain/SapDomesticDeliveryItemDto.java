package com.daesang.springbatch.sap.domesticdelivery.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * fileName         : SapDomesticDeliveryItemDto
 * author           : inayoon
 * date             : 2022-11-07
 * description      : 배송관리 - 내수 배송정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-07       inayoon             최초생성
 */
@Getter
@Setter
@NoArgsConstructor
public class SapDomesticDeliveryItemDto {
    // 납품번호 코드
    @JsonAlias(value = "VBEL2")
    private String doDoc;

    // 납품품목 코드
    @JsonAlias(value = "POSN2")
    private String itemNo;

    // 주문번호
    @JsonAlias(value = "VBELN")
    private String orderNumber;

    // 판매문서항목(항목번호)
    @JsonAlias(value = "POSNR")
    private String orderItemNo;

    // 자재명(규격명)
    @JsonAlias(value = "MAKTX")
    private String product;

    // 자재코드(규격코드)
    @JsonAlias(value = "MATNR")
    private String productCode;

    // 출고확정
    @JsonAlias(value = "WBSTA")
    private String shipmentConfirmation;

    // 납품수량
    @JsonAlias(value = "LFIMG")
    private String deliveryQuantity;

    // 납품단위
    @JsonAlias(value = "VRKM2")
    private String deliveryQuantityUnit;

    // 플랜트 코드
    @JsonAlias(value = "WERKS")
    private String plant;

    // 플랜트명
    @JsonAlias(value = "WERKT")
    private String plantName;

    // 빌링상태
    @JsonAlias(value = "FKSTA")
    private String billingStatus;
}