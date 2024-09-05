package com.daesang.springbatch.mdm.meals.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * fileName         : meals
 * author           : 김수진과장
 * date             : 2022-11-02
 * descrition       : 고객정보 - 급식거래처 조회
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-02       김수진과장             최초생성
 * 2022-11-08       김수진과장             컬럼추가
 * 2022-11-08       김수진과장             컬럼추가
 */

@Getter
@Entity
@NoArgsConstructor
@Table(name="VW_CRM_MEALS",schema = "IFUSER")
public class MdmMeals {
    @Id
    @Column(name = "CODEID")
    private String codeid;

    @Column(name = "SCH46")
    private String sch46;

    @Column(name = "SCH43")
    private String sch43;

    @Column(name = "SCH02")
    private String sch02;

    @Column(name = "SCH26")
    private String sch26;

    @Column(name = "SCH44")
    private String sch44;

    @Column(name = "SCH45")
    private String sch45;

    @Column(name = "SCH15")
    private String sch15;

    @Column(name = "SCH15_NM")
    private String sch15Nm;

    @Column(name = "SCH25")
    private String sch25;

    @Column(name = "ADDRESS_ADDRDESC1")
    private String addressAddrdesc1;

    @Column(name = "ADDRESS_ADDRDESC2")
    private String addressAddrdesc2;

    @Column(name = "ADDRESS_ADDRZIP")
    private String addressAddrzip;

    @Column(name = "TELNO")
    private String telNo;

    @Column(name = "PERNR_MEALS")
    private String pernrMeals;

    @Column(name = "PERNR_MEALS_NM")
    private String pernrMealsNm;

//    @Column(name = "VKGRP_VH")
//    private String vkgrpVh;

    @Column(name = "SCH05")
    private String sch05;

    @Column(name = "SCH06")
    private String sch06;

    @Column(name = "SCH07")
    private String sch07;

    @Column(name = "SCH10")
    private String sch10;

    @Column(name = "SCH16")
    private String sch16;

    @Column(name = "SCH17")
    private String sch17;

    @Column(name = "SCH17_NM")
    private String sch17Nm;

    @Column(name = "SCH09")
    private String sch09;

    @Column(name = "SCH09_NM")
    private String sch09Nm;

    @Column(name = "SCH18")
    private String sch18;

    @Column(name = "SCH19")
    private String sch19;

    @Column(name = "SCH19_NM")
    private String sch19Nm;

    @Column(name = "SCH20")
    private String sch20;

    @Column(name = "SCH21")
    private String sch21;

    @Column(name = "SCH22")
    private String sch22;

    @Column(name = "SCH22_NM")
    private String sch22Nm;

    @Column(name = "SCH23")
    private String sch23;

    @Column(name = "SCH24")
    private String sch24;

    @Column(name = "SCH24_NM")
    private String sch24Nm;

    @Column(name = "CREATIONDTIME")
    private String creationdtime;

    @Column(name = "LASTMODIFICATIONDTIME")
    private String lastmodificationdtime;

}
