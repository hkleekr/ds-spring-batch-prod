package com.daesang.springbatch.sap.domestictransport.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * fileName         : SapDomesticTransportDto
 * author           : inayoon
 * date             : 2022-11-17
 * description      : 배송관리 - 내수 납품정보(LE)
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-17       inayoon             최초생성
 */
@Getter
@Setter
@NoArgsConstructor
public class SapDomesticTransportDto {
    //DeliveryItem
    // 주문번호
    @JsonAlias(value = "VBELN")
    private String orderNumber;

    // 판매문서항목(주문품목번호)
    @JsonAlias(value = "POSNR")
    private String orderItemNo;

    // 납품품목(번호)
    @JsonAlias(value = "POSN2")
    private String itemNo;

    //Delivery
    // 납품문서번호(코드)
    @JsonAlias(value = "VBEL2")
    private String doDoc;

    //Transport
    // SAP 운송문서
    @JsonAlias(value = "TKNUM")
    private String transportDoc;

    // 특별처리지시자
    @JsonAlias(value = "SDABW")
    private String specialProcessing;
}