package com.daesang.springbatch.sap.domesticdelivery.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * fileName         : SapDomesticDeliveryDto
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
public class SapDomesticDeliveryDto {
    // 납품문서번호(코드)
    @JsonAlias(value = "VBEL2")
    private String doDoc;

    // 거래처코드
    @JsonAlias(value = "KUNNR")
    private String accountCode;

    // 주문번호
    @JsonAlias(value = "VBELN")
    private String orderNumber;

    // 납품처명
    @JsonAlias(value = "KUNWT")
    private String deliveryToAccount;

    // 납품처코드
    @JsonAlias(value = "KUNWE")
    private String deliveryToCode;

    // 납품일
    @JsonAlias(value = "VDATU")
    private String deliveryDate;

    // 영업사원코드
//    @JsonAlias(value = "")
//    private String deliveryOwnerUser;

    @JsonProperty(value = "deliveryItem")
    private List<SapDomesticDeliveryItemDto> deliveryItem = new ArrayList<SapDomesticDeliveryItemDto>();
}