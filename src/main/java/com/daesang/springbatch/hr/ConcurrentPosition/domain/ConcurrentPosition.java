package com.daesang.springbatch.hr.ConcurrentPosition.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * fileName         : OtherDepartment
 * author           : 권용성사원
 * date             : 2023-02-16
 * description      : 임직원 겸직 인터페이스 Entity
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-02-16       권용성사원           최초생성
 */
@Table(name ="siuser_tbl_other_job", schema = "gwdb")
@Entity
@Getter
@IdClass(ConcurrentPositionId.class)
@NoArgsConstructor
public class ConcurrentPosition {
    // 회사 코드
    @Id
    @Column(name = "COMPANYCODE")
    private String companyCode;
    // 사번
    @Id
    @Column(name = "ORGNUMBER")
    private String orgNumber;
    // 사원 명
    @Column(name = "USER_NAME")
    private String userName;
    // 타입 
    @Column(name = "JOB_TYPE")
    private String jobType;
    // 겸직 부서 1 코드
    @Column(name = "DEPTCODE1")
    private String deptCode1;
    // 겸직 부서 2
    @Column(name = "DEPTCODE2")
    private String deptCode2;
    // 겸직 부서 3
    @Column(name = "DEPTCODE3")
    private String deptCode3;
    // 직책 1 명칭
    @Column(name = "POSITION1")
    private String position1;
    // 직책 2 명칭
    @Column(name = "POSITION2")
    private String position2;
    // 직책 3 명칭
    @Column(name = "POSITION3")
    private String position3;
    // 생성일
    @Column(name = "CREATE_DATE")
    private LocalDate createDate;
}
