package com.daesang.springbatch.sap.exportdelivery.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * fileName         : SapExportDeliveryDto
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
public class SapExportDeliveryDto {
    // 납품문서번호(코드)
    @JsonAlias(value = "VBEL2")
    private String doDoc;

    // 거래처코드
    @JsonAlias(value = "KUNNR")
    private String accountCode;

    // 주문번호(S/O문서)
    @JsonAlias(value = "VBELN")
    private String orderNumber;

    // 납품처코드
    @JsonAlias(value = "KUNWE")
    private String deliveryToCode;

    // 납품(D/O) 생성일
    @JsonAlias(value = "ERDAT")
    private String dOCreatedDate;

    // 납품 요청일
    @JsonAlias(value = "VDATU")
    private String deliveryRequestDate;

    // 영업사원명(주문담당자)
    @JsonAlias(value = "ENAME")
    private String deliveryOwnerUser;

    // 영업사원사번(주문담당자 코드)
    @JsonAlias(value = "PERNR")
    private String deliveryOwnerCode;

    @JsonProperty(value = "deliveryItem")
    private List<SapExportDeliveryItemDto> deliveryItem = new ArrayList<SapExportDeliveryItemDto>();

    @JsonProperty(value = "shipment")
    private List<SapExportShipmentDto> shipment = new ArrayList<SapExportShipmentDto>();
}