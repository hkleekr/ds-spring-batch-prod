package com.daesang.springbatch.dwrs.scemployeemaster.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class DwrsScEmployeeMasterMappingDto {

    //사번
    @JsonProperty("PERNR")
    private String SCEmployeeNumber;

    //성명
    @JsonProperty("ENAME")
    private String SCEmployeeName;

//    //이메일
//    @JsonProperty("EMAIL")
//    private String Email;
//
//    //휴대전화
//    @JsonProperty("CELPH")
//    private String Mobile;

    //직급
    @JsonProperty("TITL2")
    private String Position;

    //직무
    @JsonProperty("STELL")
    private String Job;

}
