package com.daesang.springbatch.mdm.accountpartner.domain;

import com.daesang.springbatch.mdm.accountmaster.domain.MdmAccountMaster;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


/**
 * fileName         : MdmAccountPartner
 * author           : 김수진과장
 * date             : 2022-11-10
 * descrition       : 고객정보 - Account 파트너 기능 배치
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-10       김수진과장             최초생성
 * 2022-11-10       김수진과장             MdmAccountMast와 조회를 위해 id 복합키 설정
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name="VW_CRM_CUSTOMER_PTNRFN",schema = "IFUSER")
@IdClass(MdmAccountPartnerId.class)
public class MdmAccountPartner {
    @Id
    @Column(name="MASTID")
    private String mastid;

    @Column(name="NAME1")
    private String name1;

    @Id
    @Column(name="AREAID")
    private String areaid;

    @Column(name="AREANAME")
    private String areaname;

    @Column(name="PARVW")
    private String parvw;

    @Column(name="PARVW_NM")
    private String parvwNm;

    @Id
    @Column(name="KUNN2")
    private String kunn2;

    @Column(name="KUNN2_NM")
    private String kunn2Nm;

    @Id
    @Column(name="PARZA")
    private String parza;

    @Column(name="PERNR")
    private String pernr;

    @Column(name="PERNR_NM")
    private String pernrNm;

}
