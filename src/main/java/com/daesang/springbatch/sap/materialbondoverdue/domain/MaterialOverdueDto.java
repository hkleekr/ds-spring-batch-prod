package com.daesang.springbatch.sap.materialbondoverdue.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * fileName			: MaterialOverdueDto
 * author			: 최종민차장
 * date				: 2023-02-01
 * descrition       : (소재) Account 내수 연체 정보 실시간 조회 응답 DTO
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2023-02-01			최종민차장             최초생성
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MaterialOverdueDto {

    @JsonProperty(value = "BZIRK")
    private String generalBusiness;         //사업총괄

    @JsonProperty(value = "BZTXT")
    private String generalBusinessDetails;  //사업총괄내역

    @JsonProperty(value = "KUNNR")
    private String mdmCode;                 //고객코드

    @JsonProperty(value = "NAME1")
    private String accountName;             //고객명

    @JsonProperty(value = "VKBUR")
    private String businessCode;            //사업부코드

    @JsonProperty(value = "BEZEI_VB")
    private String businessDetails;         //사업부내역

    @JsonProperty(value = "VKGRP")
    private String teamCode;                //지점/팀 코드

    @JsonProperty(value = "BEZEI_VG")
    private String teamDetails;             //지점/팀 내역

    @JsonProperty(value = "ZTERM")
    private String paymentConditionCode;    //결재조건 코드

    @JsonProperty(value = "VTEXT")
    private String paymentConditionDetails; //결재조건 내역

    @JsonProperty(value = "SALES1_4")
    private String accountReceivable3Month; //외상매출금 3개월전

    @JsonProperty(value = "SALES1_3")
    private String accountReceivable2Month; //외상매출금 2개월전

    @JsonProperty(value = "SALES1_2")
    private String accountReceivable1Month; //외상매출금 1개월전

    @JsonProperty(value = "SALES1_1")
    private String accountReceivable;       //외상매출금 당월

    @JsonProperty(value = "HSL01")
    private String bondBalance;             //채권잔액

    @JsonProperty(value = "DMBTR")
    private String overdueAmount;           //연체금액

    @JsonProperty(value = "DMBTR_05")
    private String overdueAmount5Days;      //연체금액(D+5)

    @JsonProperty(value = "DMBTR_2")
    private String overdueAmountToday;      //연체금액(현재일자)

    private String type;
}
