package com.daesang.springbatch.mdm.accountmaster.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * fileName         : MdmAccountMaster
 * author           : inayoon
 * date             : 2022-10-19
 * description      : 고객정보 - Account 마스터 정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-19       inayoon             최초생성
 * 2022-11-02       inayoon             필드 추가 및 수정
 * 2022-11-24       김수진과장            areaCode ID 추가
 * 2023-02-09       권용성사원            필드 추가 및 수정
 */
@Table(name = "VW_CRM_CUSTOMER", schema = "IFUSER")
@Entity
@Getter
@NoArgsConstructor
@IdClass(MdmAccountMasterId.class)
public class MdmAccountMaster {
    // 고객사 코드
    @Id
    @Column(name = "MASTID")
    private String mdmCode;

    // 고객명
    @Column(name = "NAME1")
    private String mdmName;

    // AREA 코드
    @Id
    @Column(name = "AREAID")
    private String areaCode;

    // AREA명
    @Column(name = "AREANAME")
    private String areaName;

    // 대표자명
    @Column(name = "BAHNS")
    private String representativeName;

    // 전화번호1
    @Column(name = "TELF1")
    private String mobile1;

    // 전화번호2
    @Column(name = "TELF2")
    private String mobile2;

    // 사업자등록번호
    @Column(name = "STCD2")
    private String registrationNumber;

    // 고객사 주소
    @Column(name = "ADDRESS_ADDR1")
    private String address;

    // 고객사 상세주소
    @Column(name = "ADDRESS_ADDR2")
    private String addressDetail;

    // 고객사 우편번호
    @Column(name = "ADDRESS_ZIPCODE")
    private String zipCode;

    // 국가 코드
    @Column(name = "LAND1")
    private String country;

    // 국가명
    @Column(name = "LAND1_NM")
    private String countryName;

    // EDI 연동정보
    @Column(name = "ZSABE")
    private String ediLinkInfo;

    // 사업총괄 코드
    @Column(name = "BZIRK")
    private String salesOrganization;

    // 사업총괄명
    @Column(name = "BZIRK_NM")
    private String salesOrganizationName;

    // SL2(본부 코드)
    @Column(name = "KDGRP")
    private String headOffice;

    // SL2(본부명)
    @Column(name = "KDGRP_NM")
    private String headOfficeName;

    // 사업부 코드(영업부 코드)
    @Column(name = "VKBUR")
    private String salesDepartment;

    // 사업부명(영업부명)
    @Column(name = "VKBUR_NM")
    private String salesDepartmentName;

    // 지점/팀 코드
    @Column(name = "VKGRP")
    private String branchTeam;

    // 지점명/팀명
    @Column(name = "VKGRP_NM")
    private String branchTeamName;

    // 대금청구보류(영업영역 별) 코드
    @Column(name = "FAKSD_VV")
    private String billingHold;

    // 대금청구보류(영업영역 별) 명
    @Column(name = "FAKSD_VV_NM")
    private String billingHoldName;

    // 오더보류(영업영역 별) 코드
    @Column(name = "AUFSD_VV")
    private String orderHold;

    // 오더보류(영업영역 별) 명
    @Column(name = "AUFSD_VV_NM")
    private String orderHoldName;

    // 납품보류(영업영역 별) 코드
    @Column(name = "LIFSD_VV")
    private String deliveryHold;

    // 납품보류(영업영역 별) 명
    @Column(name = "LIFSD_VV_NM")
    private String deliveryHoldName;

    // 통화
    @Column(name = "WAERS")
    private String currency;

    // 인도조건
    @Column(name = "INCO1")
    private String deliveryCondition;

    // 계정지정그룹 코드
    @Column(name = "KTGRD")
    private String accAssignmentGroup;

    // 계정지정그룹명
    @Column(name = "KTGRD_NM")
    private String accAssignmentGroupName;

    // 유통형태(대) 코드
    @Column(name = "ZCUST1")
    private String distributionLarge;

    // 유통형태(대)명
    @Column(name = "ZCUST1_NM")
    private String distributionLargeName;

    // 유통형태(중) 코드
    @Column(name = "ZCUST2")
    private String distributionMiddle;

    // 유통형태(중)명
    @Column(name = "ZCUST2_NM")
    private String distributionMiddleName;

    // 유통형태(소) 코드
    @Column(name = "ZCUST3")
    private String distributionSmall;

    // 유통형태(소)명
    @Column(name = "ZCUST3_NM")
    private String distributionSmallName;

    // 가격리스트유형 코드
    @Column(name = "PLTYP")
    private String priceListType;

    // 가격리스트유형명
    @Column(name = "PLTYP_NM")
    private String priceListTypeName;

    // 일반플랜트 코드
    @Column(name = "VWERK")
    private String generalPlant;

    // 일반플랜트명
    @Column(name = "VWERK_NM")
    private String generalPlantName;

    // 냉장플랜트 코드
    @Column(name = "WERKS_F")
    private String refrigerationPlant;

    // 냉장플랜트명
    @Column(name = "WERKS_F_NM")
    private String refrigerationPlantName;

    // 냉동플랜트 코드
    @Column(name = "WERKS_G")
    private String freezePlant;

    // 냉동플랜트명
    @Column(name = "WERKS_G_NM")
    private String freezePlantName;

    // 유통경로 코드
    @Column(name = "KATR4")
    private String distributionChannel;

    // 유통경로명
    @Column(name = "KATR4_NM")
    private String distributionChannelName;

    // Lv1 고객계층구조 코드
    @Column(name = "HKUNNR1")
    private String lv1Hierarchy;

    // Lv1 고객계층구조명
    @Column(name = "HKUNNR1_NM")
    private String lv1HierarchyName;

    // Lv2 고객계층구조 코드
    @Column(name = "HKUNNR2")
    private String lv2Hierarchy;

    // Lv2 고객계층구조명
    @Column(name = "HKUNNR2_NM")
    private String lv2HierarchyName;

    // Lv3 고객계층구조 코드
    @Column(name = "HKUNNR3")
    private String lv3Hierarchy;

    // Lv3 고객계층구조명
    @Column(name = "HKUNNR3_NM")
    private String lv3HierarchyName;

    // 계정그룹코드
    @Column(name = "KTOKD")
    private String accGroup;

    // 계정그룹명
    @Column(name = "KTOKD_NM")
    private String accGroupName;

    // 권한그룹코드
    @Column(name = "BEGRU")
    private String authorityGroup;

    // 권한그룹명
    @Column(name = "BEGRU_NM")
    private String authorityGroupName;

    // 영업사원
    @Column(name = "PERNR")
    private String salesEmployee;

    // 생성일자
    @Column(name = "CREATIONDTIME")
    private String createDate;

    // 최종수정일자
    @Column(name = "LASTMODIFICATIONDTIME")
    private String lastUpdateDate;

    // 거래처 전자세금계산서 이메일
    @Column(name = "INTAD1000")
    private String taxBillEmail;

    // 가격그룹
    @Column(name = "KONDA")
    private String PGCode;
}