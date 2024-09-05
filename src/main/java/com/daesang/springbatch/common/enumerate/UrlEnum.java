package com.daesang.springbatch.common.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * fileName			: UrlEnum
 * author			: 최종민차장
 * date				: 2022-10-28
 * descrition       : 업무별 전송 url 정보
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-10-28			최종민차장             최초생성
 */
@Getter
@RequiredArgsConstructor
public enum UrlEnum {

    DWRS_18("IF_SF_SC_0018", "/services/apexrest/api/rest/sc/sf-0018", "http://dwrs.daesang.com/dwrs/crm/getIF_SF_SC_0018.do"),
    DWRS_53("IF_SF_SC_0053", "/services/apexrest/api/rest/sc/sf-0053", "http://dwrs.daesang.com/dwrs/crm/getIF_SF_SC_0053.do"),
    DWRS_91("IF_SF_SC_0091", "/services/apexrest/api/rest/sc/sf-0091", "http://dwrs.daesang.com/dwrs/crm/getIF_SF_SC_0091.do"), // deprecated
    //채권 연체 현황
    SAP_78("IF_SA_SF_0078", "/services/apexrest/api/rest/sa/sf-0078", ""),
    //수출 Account 여신
    SAP_113("IF_SA_SF_0113", "/services/apexrest/api/rest/sa/sf-0113", ""),
    //무상 주문 정보
    SAP_115("IF_SA_SF_0115", "/services/apexrest/api/rest/sa/sf-0115", ""),
    //주문관리 - 수출
    SAP_85("IF_SA_SF_0085", "/services/apexrest/api/rest/sa/sf-0085", ""),
    //배송관리 - 수출 배송정보
    SAP_86("IF_SA_SF_0086", "/services/apexrest/api/rest/sa/sf-0086", ""),
    // 내수 주문 관련 시험성적서 조회
    SAP_97("IF_SA_SF_0097", "/services/apexrest/api/rest/sa/sf-0097", ""),
    //배송관리 - 내수 납품정보(LE)
    SAP_118("IF_SA_SF_0118", "/services/apexrest/api/rest/sa/sf-0118", ""),
    //주문관리 - 내수 주문정보
    SAP_19("IF_SA_SF_0019", "/services/apexrest/api/rest/sa/sf-0019", ""),
    //배송관리 - 내수
    SAP_20("IF_SA_SF_0020", "/services/apexrest/api/rest/sa/sf-0020", ""),
    //고객정보 - Account 마스터 정보
    MDM_03("IF_MD_SF_0003", "/services/apexrest/api/rest/md/sf-0003", ""),
    MDM_116("IF_MD_SF_0116", "/services/apexrest/api/rest/md/sf-0116", ""),
    MDM_99("IF_MD_SF_0099", "/services/apexrest/api/rest/md/sf-0099", ""),
    //제품정보 - 제품 마스터 정보
    MDM_02("IF_MD_SF_0002", "/services/apexrest/api/rest/md/sf-0002", ""),
    HR_60("IF_HR_SF_0060", "/services/apexrest/api/rest/hr/sf-0060", ""),
    HR_59("IF_HR_SF_0059", "/services/apexrest/api/rest/hr/sf-0059", ""),
    HR_137("IF_HR_SF_0137", "/services/apexrest/api/rest/hr/sf-0137", ""),
    // 배송정보 - 내수 배송 정보
    TMS_119("IF_MD_SF_0119", "/services/apexrest/api/rest/tm/sf-0119", ""),
    MDM_134("IF_MD_SF_0134", "/services/apexrest/api/rest/md/sf-0134", ""),
    // (소재) Account 내수 연체 정보
    SAP_133("IF_SA_SF_0133", "/services/apexrest/api/rest/sa/sf-0133", ""),
    ;

    private final String name;
    private final String salesforceUrl;
    private final String legacyUrl;
}
