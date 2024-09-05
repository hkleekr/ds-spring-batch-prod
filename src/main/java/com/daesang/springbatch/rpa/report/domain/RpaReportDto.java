package com.daesang.springbatch.rpa.report.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * fileName         : RpaReportDto
 * author           : inayoon
 * date             : 2023-01-02
 * description      : 내수 주문 관련 시험성적서 파일 전송
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-01-02       inayoon             최초생성
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class RpaReportDto {
    private String file;        //binary file string
    private String fileName;    //파일명
}