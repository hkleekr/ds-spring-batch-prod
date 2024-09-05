package com.daesang.springbatch.sap.exportorder.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * fileName         : SapExportOrderDto
 * author           : inayoon
 * date             : 2022-11-08
 * description      : 주문관리 - 수출 주문정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-08       inayoon             최초생성
 * 2022-12-16       inayoon             필드 추가
 */
@Getter
@Setter
@NoArgsConstructor
public class SapExportOrderDto {
    // 주문번호(S/O문서)
    @JsonAlias(value = "VBELN")
    private String orderNumber;

    // 주문유형 코드
    @JsonAlias(value = "AUART")
    private String orderType;

    // 주문유형명
    @JsonAlias(value = "BEZEI")
    private String orderTypeName;

    // 영업사원사번(주문담당자 코드)
    @JsonAlias(value = "PERNR")
    private String orderOwnerCode;

    // 영업사원명(주문담당자명)
    @JsonAlias(value = "ENAME")
    private String orderOwnerUser;

    // 주문생성일(주문일)
    @JsonAlias(value = "ERDAT")
    private String orderStartDate;

    // 거래처 코드
    @JsonAlias(value = "KUNNR")
    private String accountCode;

    // 거래처명
    @JsonAlias(value = "NAME1")
    private String accountName;

    // 무역계약유형 코드
    @JsonAlias(value = "TCTYP")
    private String tradeContractCode;

    // 무역계약유형명
//    @JsonAlias(value = "TCTYX")
//    private String tradeContract;

    // 무역계약문서 상의 Text1 항목
    @JsonAlias(value = "ZTEXT1")
    private String tradeContract;

    // DEAL NO
    @JsonAlias(value = "TKONN_EX")
    private String dealNo;

    // 인도조건
    @JsonAlias(value = "INCO1")
    private String incoterms;

    // 도착항(Booking)
    @JsonAlias(value = "ALPTX")
    private String portofArrivalBooking;

    // Invoice No
    @JsonAlias(value = "BOKNO")
    private String invoiceCode;

    // 결제조건(지급조건)
    @JsonAlias(value = "ZTERM")
    private String paymentTerms;

    // 결제조건명
    @JsonAlias(value = "TERMDES_SD")
    private String paymentTermsName;

    // Booking 여부
    @JsonAlias(value = "BKSTA")
    private String isBooking;

    // 수출LC 상태
    @JsonAlias(value = "FDSTA")
    private String exportLC;

    // Billing
    @JsonAlias(value = "FKSTA")
    private String billing;

    // 선수금 상태
    @JsonAlias(value = "SFSTA")
    private String advancePayment;

    // 입금요청(NEGO) 상태
    @JsonAlias(value = "NGKSTA")
    private String depositReqNEGO;

    // 입금정리 상태
    @JsonAlias(value = "NGRSTA")
    private String depositClearance;

    // 차입금정리 상태
    @JsonAlias(value = "OASTA")
    private String borrowingsClearance;

    // Master B/L(AWB) 번호
    @JsonAlias(value = "MBLNO")
    private String masterBL;

    // House B/L
    @JsonAlias(value = "HBLNO")
    private String houseBL;

    // 판매처 코드
//    @JsonAlias(value = "KUNNR")
//    private String vendorCode;

    // 판매처명
//    @JsonAlias(value = "NAME1")
//    private String vendorName;

    // 해외 2차 납품처 코드
    @JsonAlias(value = "RKUNW")
    private String secSupplierAccountCode;

    // 해외 2차 납품처명
    @JsonAlias(value = "RKUNX")
    private String secSupplierAccountName;

    // 입금의뢰문서번호(입금관리번호)
    @JsonAlias(value = "NEGNO")
    private String depositRequestNo;

    // 환입금번호(환어음매입번호/은행부여)
    @JsonAlias(value = "NGBEN")
    private String referenceNo;

    @JsonProperty(value = "orderItem")
    private List<SapExportOrderItemDto> orderItem = new ArrayList<SapExportOrderItemDto>();
}