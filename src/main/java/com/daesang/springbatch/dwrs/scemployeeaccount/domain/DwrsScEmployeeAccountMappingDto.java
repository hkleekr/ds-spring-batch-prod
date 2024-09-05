package com.daesang.springbatch.dwrs.scemployeeaccount.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class DwrsScEmployeeAccountMappingDto {

    //거래처코드
    @JsonAlias("CUST_ID")
    private String MDMCode;

    //사번
    @JsonAlias("PERNR")
    private String SCEmployeeNumber;

    //생성일
    @JsonAlias("REG_DT")
    private String CreateDate;

    //수정일
    @JsonAlias("UPDATE_DT")
    private String ModifyDate;

    //직무
    @JsonAlias("JOBGRM")
    private String Job;

//    //삭제여부
//    @JsonAlias("DEL_YN")
//    private String IsDelete;
}
