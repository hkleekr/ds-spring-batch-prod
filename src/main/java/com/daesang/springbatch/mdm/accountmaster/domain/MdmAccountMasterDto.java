package com.daesang.springbatch.mdm.accountmaster.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * fileName         : MdmAccountMasterDto
 * author           : inayoon
 * date             : 2022-10-19
 * description      : 고객정보 - Account 마스터 정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-19       inayoon             최초생성
 * 2022-11-02       inayoon             필드 추가 및 수정
 * 2023-02-09       권용성사원            필드 추가 및 수정
 */
@Getter
@Setter
@NoArgsConstructor
public class MdmAccountMasterDto {
    private String mdmCode;
    private String mdmName;
    private String areaCode;
    private String areaName;
    private String representativeName;
    private String mobile1;
    private String mobile2;
    private String registrationNumber;
    private String address;
    private String addressDetail;
    private String zipCode;
    private String country;
    private String countryName;
    private String ediLinkInfo;
    private String salesOrganization;
    private String salesOrganizationName;
    private String headOffice;
    private String headOfficeName;
    private String salesDepartment;
    private String salesDepartmentName;
    private String branchTeam;
    private String branchTeamName;
    private String billingHold;
    private String billingHoldName;
    private String orderHold;
    private String orderHoldName;
    private String deliveryHold;
    private String deliveryHoldName;
    @JsonProperty("currency_x")
    private String currency;
    private String deliveryCondition;
    private String accAssignmentGroup;
    private String accAssignmentGroupName;
    private String distributionLarge;
    private String distributionLargeName;
    private String distributionMiddle;
    private String distributionMiddleName;
    private String distributionSmall;
    private String distributionSmallName;
    private String priceListType;
    private String priceListTypeName;
    private String generalPlant;
    private String generalPlantName;
    private String refrigerationPlant;
    private String refrigerationPlantName;
    private String freezePlant;
    private String freezePlantName;
    private String distributionChannel;
    private String distributionChannelName;
    private String lv1Hierarchy;
    private String lv1HierarchyName;
    private String lv2Hierarchy;
    private String lv2HierarchyName;
    private String lv3Hierarchy;
    private String lv3HierarchyName;
    private String accGroup;
    private String accGroupName;
    private String authorityGroup;
    private String authorityGroupName;
    private String salesEmployee;
    private String taxBillEmail;
    private String PGCode;
}