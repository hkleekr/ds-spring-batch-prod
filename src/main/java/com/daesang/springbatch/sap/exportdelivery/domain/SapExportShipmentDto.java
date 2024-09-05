package com.daesang.springbatch.sap.exportdelivery.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * fileName         : SapExportShipmentDto
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
public class SapExportShipmentDto {
    // ShipmentItem
    // 납품번호 코드
    @JsonAlias(value = "VBEL2")
    private String doDoc;

    // 컨테이너 번호
    @JsonAlias(value = "ZCONO")
    private String contNo;

    // 컨테이너 종류
    @JsonAlias(value = "CNTY1")
    private String contType;

    // 컨테이너 수
    @JsonAlias(value = "CNTN1")
    private String contNum;

    // 컨테이너 종류#2
    @JsonAlias(value = "CNTY2")
    private String contType2;

    // 컨테이너 수#2
    @JsonAlias(value = "CNTN2")
    private String contNum2;

    // Freight
    @JsonAlias(value = "Freight")
    private String freight;

    // 운송료(컨테이너)
    @JsonAlias(value = "EXPCO")
    private String shippingFeeContainer;

    // Other Charge(기타비용)
    @JsonAlias(value = "EXPOT")
    private String otherCharge;


    // Shipment
    // Master B/L
    @JsonAlias(value = "MBLNO")
    private String masterBL;

    // House B/L
    @JsonAlias(value = "HBLNO")
    private String houseBL;

    // 거래처코드
    @JsonAlias(value = "KUNNR")
    private String accountCode;

    // 주문번호(S/O문서)
    @JsonAlias(value = "VBELN")
    private String orderNumber;

    // 인도조건
    @JsonAlias(value = "INCO1")
    private String incoterms;

    // 판매처 코드
//    @JsonAlias(value = "KUNNR")
//    private String vendorAccountCode;

    // S/D(Normal)
    @JsonAlias(value = "TDSTA")
    private String shippingDocNormal;

    // Insurance Y/N
    @JsonAlias(value = "IPSTA")
    private String insurance;

    // 통관
    @JsonAlias(value = "CCSTA")
    private String customsClearance;

    // 면장
    @JsonAlias(value = "CPSTA")
    private String exportDeclarationCertificate;

    // 포워더명
    @JsonAlias(value = "LIFNR_TXT")
    private String forwarderName;

    // 선적일
    @JsonAlias(value = "ONBDT")
    private String shipmentDate;

    // 선사명
    @JsonAlias(value = "SHCNM")
    private String shippingCompany;

    // 선사코드
    @JsonAlias(value = "SHCCD")
    private String shipCode;

    // 선기명
    @JsonAlias(value = "CARTX")
    private String shipName;

    // 선적항 명
    @JsonAlias(value = "SHPTX")
    private String portOfLoading;

    // 도착항 명
    @JsonAlias(value = "ALPTX")
    private String portOfDischanrge;

    // CY장치장명
    @JsonAlias(value = "BDANM")
    private String cyName;

    // Initial ETD(출발항)
    @JsonAlias(value = "ETDDT")
    private String initialETD;

    // Updated ETD(출발항) - Tracing일자기준
//    @JsonAlias(value = "")
    private String updatedETD;

    // Initial ETA(도착항)
    @JsonAlias(value = "ETADT")
    private String initialETA;

    // Updated ETA(도착항) - Tracing일자기준
    @JsonAlias(value = "REETA")
    private String updatedETA;

    // Tracing 일자
    @JsonAlias(value = "BASDT")
    private String tracingDate;

    // 선사 Tracking 웹링크(세부정보)
    @JsonAlias(value = "ZINFO")
    private String shippingTrackingWebLink;

    // Delay 사유
    @JsonAlias(value = "GAPTXT")
    private String delayReason;

    // Initial ATD(출발항)
//    @JsonAlias(value = "")
//    private String atd;

    // Initial ATA(도착항)
    @JsonAlias(value = "ATADT")
    private String ata;

    // POL 환적항
    @JsonAlias(value = "PTDSX")
    private String pol;

    // 인도조건
//    @JsonAlias(value = "INCO1")
//    private String termsOfDelivery;
}