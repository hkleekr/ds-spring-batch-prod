package com.daesang.springbatch.hr.employee.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * fileName         : EmployeeDto
 * author           : 권용성사원
 * date             : 2022-10-26
 * descrition       : 통합인사 임직원 배치 DTO
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-26       권용성사원             최초생성
 * 2022-10-28       권용성사원             필드 추가 및 수정
 */
@Getter
@Setter
@NoArgsConstructor
public class EmployeeDto {
//    @JsonProperty(value = "id")
//    private Integer id;

    @JsonProperty(value = "imUserId")
    private String imUserId;

    @JsonProperty(value = "companyCode")
    private String companyCode;

    @JsonProperty(value = "orgNumber")
    private String orgNumber;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "nameEng")
    private String nameEng;

    @JsonProperty(value = "shortName")
    private String shortName;

    @JsonProperty(value = "mailDomain")
    private String mailDomain;

    @JsonProperty(value = "incidence")
    private String incidence;

    @JsonProperty(value = "expireDate")
    private String expireDate;

    @JsonProperty(value = "position1")
    private String position1;

    @JsonProperty(value = "positionCode1")
    private String positionCode1;

    @JsonProperty(value = "grade1")
    private String grade1;

    @JsonProperty(value = "gradeCode1")
    private String gradeCode1;

    @JsonProperty(value = "deptCode1")
    private String deptCode1;

    @JsonProperty(value = "companyPhone")
    private String companyPhone;

    @JsonProperty(value = "workLocation")
    private String workLocation;

    @JsonProperty(value = "createDate")
    private String createDate;

    @JsonProperty(value = "updateDate")
    private String updateDate;

    @JsonProperty(value = "stell")
    private String stell;

    @JsonProperty(value = "stltx")
    private String stltx;

    @JsonProperty(value = "absence")
    private String absence;

    @JsonProperty(value = "useEmail")
    private String useEmail;

    @JsonProperty(value = "cellphone")
    private String cellphone;

    @JsonProperty(value = "cudFlag")
    private String cudFlag;

    @JsonProperty(value = "mainJob")
    private String mainJob;

}
