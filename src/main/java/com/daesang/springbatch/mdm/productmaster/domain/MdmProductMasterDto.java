package com.daesang.springbatch.mdm.productmaster.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * fileName         : MdmProductMasterDto
 * author           : inayoon
 * date             : 2022-10-21
 * description      : 제품정보 - 제품 마스터 정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-21       inayoon             최초생성
 * 2022-11-11       inayoon             필드 추가 및 수정
 */
@Getter
@Setter
@NoArgsConstructor
public class MdmProductMasterDto {
    private String mdmCode;
    private String mdmName;
    private String mdmNameEn;
    private String MDMNameAbb;
    private String salesOrganization;
    private String salesOrganizationName;
    private String cmgCode;
    private String cmgName;
    private String cmcode;
    private String cmName;
    private String pmcode;
    private String pmName;
    private String basicUnit;
    private String netWeight;
    private String consumerPrice;
    private String companyOrgCode;
    private String companyOrg;
    private String productionRepresentativePlantCode;
    private String productionRepresentativePlant;
    private String procurementCnt;
    private String barcode;
    private String boxBarcode;
    private String weightUnit;
    private String salesUnit;
    private String expirationDate;
    private String productHierarchyLargeCode;
    private String productHierarchyLarge;
    private String productHierarchyMiddleCode;
    private String productHierarchyMiddle;
    private String productHierarchySmallCode;
    private String productHierarchySmall;
    private String exportOrNotCode;
    private String exportOrNot;
    private String discontinuedInfoDomesticCode;
    private String discontinuedInfoDomesticName;
    private String discontinuedInfoDirectExportCode;
    private String discontinuedInfoDirectExportName;
    private String discontinuedInfoLocalExportCode;
    private String discontinuedInfoLocalExportName;
    private String materialGroupCode;
    private String materialGroup;
    @JsonProperty("currency_x")
    private String currency;
    private String productFamilyCode;
    private String productFamily;
    private String taxTypeCode;
    private String taxType;
    private String storageMethodCode;
    private String storageMethod;
    private String applicationCode;
    private String application;
    private String brandCode;
    private String brand;
    private String sapBarcode;
    private String sapBoxBarcode;
}
