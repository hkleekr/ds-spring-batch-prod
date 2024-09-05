package com.daesang.springbatch.mdm.accountpartner.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * fileName         : MdmAccountPartnerDto
 * author           : 김수진과장
 * date             : 2022-11-10
 * descrition       : 고객정보 - Account 파트너 기능 배치
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-10       김수진과장             최초생성
 */

@Getter
@Setter
@NoArgsConstructor
public class MdmAccountPartnerDto {
    // MDM 고객사 코드
    @JsonProperty("MDMCode")
    private String mastid;

    // MDM 고객 명
    @JsonProperty("MDMName")
    private String name1;

    // AREA 코드
    @JsonProperty("AreaCode")
    private String areaid;

    // AREA 명칭
    @JsonProperty("AreaName")
    private String areaname;

    @JsonProperty("PartnerFunctionCode")
    // 파트너 기능 Function 코드
    private String parvw;

    @JsonProperty("PartnerFunctionName")
    // 파트너 기능 Function 명
    private String parvwNm;

    @JsonProperty("PartMDMCode")
    // 파트너 고객사 코드
    private String kunn2;

    @JsonProperty("PartMDMName")
    // 파트너 고객사 명
    private String kunn2Nm;

    // 순번
    @JsonProperty("Seq")
    private String parza;

    @JsonProperty("SalesEmployeeNumber")
    // 영업사원번호(파트너기능)
    private String pernr;

    @JsonProperty("SalesEmployeeName")
    // 영업사원명 (파트너기능)
    private String pernrNm;

}
