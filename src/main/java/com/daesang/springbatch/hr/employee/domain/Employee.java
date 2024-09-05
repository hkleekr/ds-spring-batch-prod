package com.daesang.springbatch.hr.employee.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * fileName         : Employee
 * author           : 권용성사원
 * date             : 2022-10-26
 * descrition       : 통합인사 임직원 배치 Entity
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-26       권용성사원             최초생성
 * 2022-10-28       권용성사원             필드 추가 및 수정
 */
@Table(name = "siuser_tbl_master", schema = "gwdb")
@Entity
@Getter
@NoArgsConstructor
public class Employee {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Integer id;

    @Column(name = "IMUSERID")
    private String imUserId;

    @Column(name = "COMPANYCODE")
    private String companyCode;

    @Id
    @Column(name = "ORGNUMBER")
    private String orgNumber;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NAMEENG")
    private String nameEng;

    @Column(name = "SHORTNAME")
    private String shortName;

    @Column(name = "MAILDOMAIN")
    private String mailDomain;

    @Column(name = "INCIDENCE")
    private String incidence;

    @Column(name = "EXPIREDATE")
    private String expireDate;

    @Column(name = "POSITION1")
    private String position1;

    @Column(name = "POSITIONCODE1")
    private String positionCode1;

    @Column(name = "GRADE1")
    private String grade1;

    @Column(name = "GRADECODE1")
    private String gradeCode1;

    @Column(name = "DEPTCODE1")
    private String deptCode1;

    @Column(name = "COMPANYPHONE")
    private String companyPhone;

    @Column(name = "WORKLOCATION")
    private String workLocation;

    @Column(name = "CREATEDATE")
    private String createDate;

    @Column(name = "UPDATEDATE")
    private LocalDate updateDate;

    @Column(name = "STELL")
    private String stell;

    @Column(name = "STLTX")
    private String stltx;

    @Column(name = "ABSENCE")
    private String absence;

    @Column(name = "USEMAIL")
    private String useEmail;

    @Column(name = "CELLPHONE")
    private String cellphone;

    @Column(name = "CUDFLAG")
    private String cudFlag;

    @Column(name = "MAINJOB")
    private String mainJob;
}
