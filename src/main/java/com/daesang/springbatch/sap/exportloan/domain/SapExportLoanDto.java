package com.daesang.springbatch.sap.exportloan.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * fileName         : SapExportLoan
 * author           : 김수진과장
 * date             : 2022-11-09
 * descrition       : 수출 Account 여신 Dto
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-09       김수진과장             최초생성
 * 2022-11-10       최종민차장             서비스 수정
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SapExportLoanDto {
    //거래처 코드 (KUNNR)
    private String mdmCode;
    //거래처 명 (NAME1)
    private String mdmName;
    //기초여신금액 외화 (CRTOT2)
    private String basicCreditAmountForeign;
    //전표 (WAERK)
    private String statement;
    //환율 (KURSK)
    @JsonProperty("currency_x")
    private String currency;
    //기초여신금액 원화 (CRTOT)
    private String basicCreditAmountWon;
    //총 오더금액 원화 (ORDAT)
    private String totalOrderAmountWon;
    //총 대금청구액 원화 (BILAT)
    private String totalBillingAmountWon;
    //총 미결채권액 원화 (OPNAT)
    private String totalDebentureAmountWon;
    //총 미결주문액 원화 (ORNAT)
    private String totalPendingOrderAmountWon;
    //여유한도금액 원화 (REAMT)
    private String marginMaxAmountWon;
    //여유한도금액 외화 (REAMT2)
    private String marginLimitAmountForeign;
    //여신관리영역 (KKBER)
    private String loanManagement;
}
