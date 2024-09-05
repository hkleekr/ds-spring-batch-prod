package com.daesang.springbatch.hr.department.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * fileName         : DepartmentDto
 * author           : 권용성사원
 * date             : 2022-10-26
 * descrition       : 통합인사 부서정보 배치 DTO
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-26       권용성사원             최초생성
 * 2022-10-28       권용성사원             필드 추가 및 수정
 */
@Getter
@Setter
@NoArgsConstructor
public class DepartmentDto {

    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "companyCode")
    private String companyCode;

    @JsonProperty(value = "name1")
    private String name1;

    @JsonProperty(value = "name2")
    private String name2;

    @JsonProperty(value = "name3")
    private String name3;

    @JsonProperty(value = "name4")
    private String name4;

    @JsonProperty(value = "name5")
    private String name5;

    @JsonProperty(value = "name6")
    private String name6;

    @JsonProperty(value = "name7")
    private String name7;

    @JsonProperty(value = "name8")
    private String name8;

    @JsonProperty(value = "myCode")
    private String myCode;

    @JsonProperty(value = "deptCode")
    private String deptCode;

    @JsonProperty(value = "shortName")
    private String shortName;

    @JsonProperty(value = "parentCode")
    private String parentCode;

}
