package com.daesang.springbatch.mdm.productmaster.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * fileName         : MdmProductMaster
 * author           : inayoon
 * date             : 2022-10-21
 * description      : 제품정보 - 제품 마스터 정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-21       inayoon             최초생성
 * 2022-11-11       inayoon             필드 추가 및 수정
 */
@Table(name = "VW_CRM_MATERIAL", schema = "IFUSER")
@Entity
@Getter
@NoArgsConstructor
public class MdmProductMaster {
    // 자재코드
    @Id
    @Column(name = "MASTID")
    private String mdmCode;

    // 자재명
    @Column(name = "MAKTX")
    private String mdmName;

    // 자재명(영문)
    @Column(name = "MAKTXE")
    private String mdmNameEn;

    // 자재명 약어
    @Column(name = "MAKTXS")
    private String MDMNameAbb;

    // 사업총괄(본부)코드
    @Column(name = "BZIRK")
    private String salesOrganization;

    // 사업총괄(본부)명
    @Column(name = "BZIRK_NM")
    private String salesOrganizationName;

    // CMG코드
    @Column(name = "CMGCD")
    private String cmgCode;

    // CMG명
    @Column(name = "CMGCD_NM")
    private String cmgName;

    // CM코드
    @Column(name = "CMCD")
    private String cmcode;

    // CM명
    @Column(name = "CMCD_NM")
    private String cmName;

    // PM코드
    @Column(name = "PERNR")
    private String pmcode;

    // PM명
    @Column(name = "PERNR_NM")
    private String pmName;

    // 기본 단위
    @Column(name = "MEINS")
    private String basicUnit;

    // 순중량
    @Column(name = "NTGEW")
    private String netWeight;

    // 소비자가
    @Column(name = "PRICE")
    private String consumerPrice;

    // 회사조직 코드
    @Column(name = "CMORG")
    private String companyOrgCode;

    // 회사조직명
    @Column(name = "CMORG_NM")
    private String companyOrg;

    // 생산/대표플랜트 코드
    @Column(name = "FORMT")
    private String productionRepresentativePlantCode;

    // 생산/대표플랜트명
    @Column(name = "FORMT_NM")
    private String productionRepresentativePlant;

    // 입수량
    @Column(name = "BXQTY")
    private String procurementCnt;

    // 바코드
    @Column(name = "EAN11")
    private String barcode;

    // 박스바코드
    @Column(name = "LEAN11")
    private String boxBarcode;

    // 중량단위
    @Column(name = "GEWEI")
    private String weightUnit;

    // 판매단위
    @Column(name = "VRKME10")
    private String salesUnit;

    // 유통기한
    @Column(name = "MHDHB")
    private String expirationDate;

    // 제품계층(대) 코드
    @Column(name = "PRDNH1")
    private String productHierarchyLargeCode;

    // 제품계층(대)명
    @Column(name = "PRDNH1_NM")
    private String productHierarchyLarge;

    // 제품계층(중) 코드
    @Column(name = "PRDNH2")
    private String productHierarchyMiddleCode;

    // 제품계층(중)명
    @Column(name = "PRDNH2_NM")
    private String productHierarchySmall;

    // 제품계층(소) 코드
    @Column(name = "PRDNH3")
    private String productHierarchySmallCode;

    // 제품계층(소)명
    @Column(name = "PRDNH3_NM")
    private String productHierarchyMiddle;

    // 수출여부 코드
    @Column(name = "EXPTYP")
    private String exportOrNotCode;

    // 수출여부명
    @Column(name = "EXPTYP_NM")
    private String exportOrNot;

    // 단종정보>국내 코드
    @Column(name = "VMSTA10")
    private String discontinuedInfoDomesticCode;

    // 단종정보>국내
    @Column(name = "VMSTA10_NM")
    private String discontinuedInfoDomesticName;

    // 단종정보>직수출 코드
    @Column(name = "VMSTA20")
    private String discontinuedInfoDirectExportCode;

    // 단종정보>직수출
    @Column(name = "VMSTA20_NM")
    private String discontinuedInfoDirectExportName;

    // 단종정보>Local수출 코드
    @Column(name = "VMSTA30")
    private String discontinuedInfoLocalExportCode;

    // 단종정보>Local수출
    @Column(name = "VMSTA30_NM")
    private String discontinuedInfoLocalExportName;

    // 자재그룹 코드
    @Column(name = "MATKL")
    private String materialGroupCode;

    // 자재그룹명
    @Column(name = "MATKL_NM")
    private String materialGroup;

    // 통화
    @Column(name = "CRNCY")
    private String currency;

    // 제품군 코드
    @Column(name = "SPART")
    private String productFamilyCode;

    // 제품군명
    @Column(name = "SPART_NM")
    private String productFamily;

    // 과세구분 코드
    @Column(name = "TAXKM")
    private String taxTypeCode;

    // 과세구분명
    @Column(name = "TAXKM_NM")
    private String taxType;

    // 보관방법 코드
    @Column(name = "ZSTATUS10")
    private String storageMethodCode;

    // 보관방법명
    @Column(name = "ZSTATUS10_NM")
    private String storageMethod;

    // 자재용도 코드
    @Column(name = "ZUSAGE10")
    private String applicationCode;

    // 자재용도명
    @Column(name = "ZUSAGE10_NM")
    private String application;

    // 브랜드 코드
    @Column(name = "BRAND")
    private String brandCode;

    // 브랜드명
    @Column(name = "BRAND_NM")
    private String brand;

    // 참조 판매바코드
    @Column(name = "EAN22")
    private String sapBarcode;

    // 참조 물류바코드(박스 바코드)
    @Column(name = "LEAN22")
    private String sapBoxBarcode;

    // 생성일자
    @Column(name = "CREATIONDTIME")
    private String createDate;

    // 최종수정일자
    @Column(name = "LASTMODIFICATIONDTIME")
    private String lastUpdateDate;
}