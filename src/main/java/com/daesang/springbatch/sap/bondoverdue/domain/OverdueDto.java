package com.daesang.springbatch.sap.bondoverdue.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * fileName			: OverdueDto
 * author			: 최종민차장
 * date				: 2022-11-07
 * descrition       : 채권 현황 조회
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-11-07			최종민차장             최초생성
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OverdueDto {
    //mdm 고객코드
    private String mdmCode;
    //mdm 고객명
    private String mdmName;
    //사원번호
    private String salesEmployee;
    //사원명
    private String salesEmployeeName;
    //지급조건내역
    private String paymentTerms;
    //전월채권잔액
    private String previousMonthBalance;
    //외상매출금
    private String accountsReceivable;
    //총채권잔액
    private String totalBondBalance;
    //7일 이후 발생 채권
    private String after7DaysBonds;
    //7일 이내 발생 채권
    private String before7DaysBonds;
    //15일 이내 채권
    private String within15DaysBonds;
    //30일 이내 채권
    private String within30DaysBonds;
    //31일 초과 채권
    private String over31DaysBonds;
    //지점 팀/명
    private String branchTeam;
}
