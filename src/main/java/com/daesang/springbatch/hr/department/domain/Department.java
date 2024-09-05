package com.daesang.springbatch.hr.department.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * fileName         : Department
 * author           : 권용성사원
 * date             : 2022-10-26
 * descrition       : 통합인사 부서정보 배치 Entity
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-26       권용성사원             최초생성
 * 2022-10-28       권용성사원             필드 추가 및 수정
 */
@Table(name ="siuser_tbl_dept_master", schema = "gwdb")
@Entity
@Getter
@NoArgsConstructor
public class Department {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "id")
    //private Integer id;

    @Id
    @Column(name = "DEPTCODE")
    private String deptCode;

    @Column(name = "COMPANYCODE")
    private String companyCode;

    @Column(name = "NAME_1")
    private String name1;

    @Column(name = "NAME_2")
    private String name2;

    @Column(name = "NAME_3")
    private String name3;

    @Column(name = "NAME_4")
    private String name4;

    @Column(name = "NAME_5")
    private String name5;

    @Column(name = "NAME_6")
    private String name6;

    @Column(name = "NAME_7")
    private String name7;

    @Column(name = "NAME_8")
    private String name8;

    @Column(name = "MYCODE")
    private String myCode;

    @Column(name = "SHORTNAME")
    private String shortName;

    @Column(name = "PARENTCODE")
    private String parentCode;

}
