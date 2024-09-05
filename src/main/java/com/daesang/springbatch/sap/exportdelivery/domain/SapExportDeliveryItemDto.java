package com.daesang.springbatch.sap.exportdelivery.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * fileName         : SapExportDeliveryItemDto
 * author           : inayoon
 * date             : 2022-11-09
 * description      : 배송관리 - 수출 배송정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-09       inayoon             최초생성
 */
@Getter
@Setter
@NoArgsConstructor
public class SapExportDeliveryItemDto {
    // 납품번호 코드
    @JsonAlias(value = "VBEL2")
    private String doDoc;

    // 납품품목 코드(품목번호)
    @JsonAlias(value = "POSN2")
    private String itemNo;

    // 주문번호(S/O문서)
    @JsonAlias(value = "VBELN")
    private String orderNumber;

    // 판매문서항목(항목번호,주문자재)
    @JsonAlias(value = "POSNR")
    private String orderItemNo;

    // 자재명(규격명)
    @JsonAlias(value = "MAKTX")
    private String product;

    // 자재코드(규격코드)
    @JsonAlias(value = "MATNR")
    private String productCode;

    // 납품(출고)수량
    @JsonAlias(value = "LFIMG")
    private String deliveryQuantity;

    // 납품수량 단위
    @JsonAlias(value = "VRKM2")
    private String deliveryQuantityUnit;

    // 플랜트 코드
    @JsonAlias(value = "WERKS")
    private String plant;

    // 작업지 플랜트
    @JsonAlias(value = "WERKT2")
    private String workPlacePlant;

    // 작업의뢰
    @JsonAlias(value = "IWSTA")
    private String workRequest;

    // 환율
    @JsonAlias(value = "TKRATE")
    private String exchangeRate;
}
