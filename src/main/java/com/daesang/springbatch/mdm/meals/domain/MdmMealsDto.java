package com.daesang.springbatch.mdm.meals.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

/**
 * fileName         : mealsDto
 * author           : 김수진과장
 * date             : 2022-11-02
 * descrition       : 고객정보 - 급식거래처 조회 DTO
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-02       김수진과장             최초생성
 * 2022-11-08       김수진과장             컬럼, jsonProperty 추가
 * 2022-11-18       김수진과장             jsonProperty명 수정
 */

@Getter
@Setter
@NoArgsConstructor
public class MdmMealsDto {

    //급식거래처코드
    @JsonProperty("SchoolCode")
    private String codeid;

    // 급식거래처명
    @JsonProperty("SchoolAccountName")
    private String sch02;

    // 단가그룹
    @JsonProperty("PriceGroup")
    private String sch46;

    // 정보공시코드
    @JsonProperty("InformationCode")
    private String sch43;

    // eat등록기관명
    @JsonProperty("EatRegistInstitution")
    private String sch26;

    // 표준학교코드
    @JsonProperty("StandardSchoolCode")
    private String sch44;

    // 나이스등록학교명
    @JsonProperty("NiceSchoolName")
    private String sch45;

    // 기관구분
    @JsonProperty("InstitutionType")
    private String sch15;

    // 기관구분명
    @JsonProperty("InstitutionTypeName")
    private String sch15Nm;

    // 담당 기관 사업자번호
    @JsonProperty("InstitutionRegistrationNumber")
    private String sch25;

    // 주소
    @JsonProperty("AccountAddress")
    private String addressAddrdesc1;

    // 상세주소
    @JsonProperty("AccountAddressDetail")
    private String addressAddrdesc2;

    // 전화번호
    @JsonProperty("Mobile1")
    private String telNo;

    // 우편번호
    @JsonProperty("AccountAddressPostalCode")
    private String addressAddrzip;

    // 홍보영양사 사번
    @JsonProperty("PromotionDietitianCode")
    private String pernrMeals;

    // 홍보영양사 사번명
    @JsonProperty("PromotionDietitianName")
    private String pernrMealsNm;

    // 기관 영영사 명
    @JsonProperty("InstitutionDietitianName")
    private String sch05;

    // 기관 영영사 전화번호
    @JsonProperty("InstitutionDietitianMobile")
    private String sch06;

    // 담당 기관 영양사, 원장 메일 정보
    @JsonProperty("InstitutionDietitianEmail")
    private String sch07;

    // 식수 인원
    @JsonProperty("EatNumber")
    private String sch10;

    // 급식비
    @JsonProperty("LunchMoney")
    private String sch16;

    // 배식형태
    @JsonProperty("DistributeTypeCode")
    private String sch17;

    // 배식형태명
    @JsonProperty("DistributeTypeName")
    private String sch17Nm;

    // 급식형태코드
    @JsonProperty("SchoolMealTypeCode")
    private String sch09;

    // 급식형태명
    @JsonProperty("SchoolMealTypeName")
    private String sch09Nm;

    // 오븐기 유무
    @JsonProperty("OvenStatus")
    private String sch18;

    // 입찰 방법
    @JsonProperty("BiddingMethodCode")
    private String sch19;

    // 입찰 방법 명
    @JsonProperty("BiddingMethodName")
    private String sch19Nm;

    // 입찰 기간
    @JsonProperty("BiddingPeriod")
    private String sch20;

    // 조식유무
    @JsonProperty("BreakfastStatus")
    private String sch21;

    // 조식운영형태
    @JsonProperty("BreakfastOperationCode")
    private String sch22;

    // 조식운영형태 명
    @JsonProperty("BreakfastOperationName")
    private String sch22Nm;

    // 석식 유무
    @JsonProperty("DinnerStatus")
    private String sch23;

    // 석식운영형태
    @JsonProperty("DinnerOperationCode")
    private String sch24;

    // 석식운영형태명
    @JsonProperty("DinnerOperationName")
    private String sch24Nm;

    // 생성일자
    private String creationdtime;

    // 최종수정일자
    private String lastmodificationdtime;

}

