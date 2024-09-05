package com.daesang.springbatch.sap.domesticorder.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * fileName         : SapDomesticOrderDto
 * author           : inayoon
 * date             : 2022-11-04
 * description      : 주문관리 - 내수 주문정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-04       inayoon             최초생성
 */
@Getter
@Setter
@NoArgsConstructor
public class SapDomesticOrderDto {
    // 주문유형코드
    @JsonAlias(value = "AUART")
    private String orderType;

    // 주문번호(S/O문서)
    @JsonAlias(value = "VBELN")
    private String orderNumber;

    // 주문유형명
    @JsonAlias(value = "BEZEI")
    private String orderTypeName;

    // 거래처 코드
    @JsonAlias(value = "KUNNR")
    private String accountCode;

    // 주문생성일
    @JsonAlias(value = "ERDAT")
    private String orderStartDate;

    // 영업사원사번(주문담당자 코드)
    @JsonAlias(value = "PERNR")
    private String orderOwnerCode;

    @JsonAlias(value = "orderItem")
    private List<SapDomesticOrderItemDto> orderItem = new ArrayList<SapDomesticOrderItemDto>();
}