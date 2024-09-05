package com.daesang.springbatch.hr.ConcurrentPosition.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * fileName         : OtherDepartmentDto
 * author           : 권용성사원
 * date             : 2023-02-16
 * description      : 임직원 겸직 인터페이스 Dto
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-02-16       권용성사원           최초생성
 */
@Getter
@Setter
@NoArgsConstructor
public class ConcurrentPositionDto {
    @JsonProperty(value = "companyCode")
    private String companyCode;
    @JsonProperty(value = "orgNumber")
    private String orgNumber;
    @JsonProperty(value = "userName")
    private String userName;
    @JsonProperty(value = "jobType")// 1: 겸직, 2: 파견, 3: RC파견
    private String jobType;
    @JsonProperty(value = "deptCode1")
    private String deptCode1;
    @JsonProperty(value = "deptCode2")
    private String deptCode2;
    @JsonProperty(value = "deptCode3")
    private String deptCode3;
    @JsonProperty(value = "position1")
    private String position1;
    @JsonProperty(value = "position2")
    private String position2;
    @JsonProperty(value = "position3")
    private String position3;
    @JsonProperty(value = "createDate")
    private String createDate;
}
