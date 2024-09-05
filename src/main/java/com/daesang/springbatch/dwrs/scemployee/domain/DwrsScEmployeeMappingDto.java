package com.daesang.springbatch.dwrs.scemployee.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class DwrsScEmployeeMappingDto {

    //사번
    @JsonAlias("PERNR")
    private String SCEmployeeNumber;

    //거래처코드
    @JsonAlias("CUST_ID")
    private String MDMCode;

    //행사계획그룹코드
    @JsonAlias("SC_EVENT_PLAN_SEQ")
    private String PromotionGroupCode;

    //행사품목코드
    @JsonAlias("PRODUCT_GROUP_CD")
    private String PromotionCategoryCode;

    //근무일
    @JsonAlias("CNT")
    private String PerformanceWorkDate;


}
