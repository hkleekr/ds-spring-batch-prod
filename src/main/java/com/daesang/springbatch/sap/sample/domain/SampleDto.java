package com.daesang.springbatch.sap.sample.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * fileName			: SampleDto
 * author			: 최종민차장
 * date				: 2022-11-09
 * descrition       : 무상 주문 정보 batch 조회
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-11-09			최종민차장             최초생성
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SampleDto {
    //VBELN (주문번호)
    private String orderNumber;
    //STATUS 예외(1:주문수량<>납품수량, 2:주문수량=납품수량)
    @JsonProperty("exception_x")
    private String exception;
    //POSNR (항목번호)
    private String itemNumber;
    //MATNR (규격)
    private String productCode;
    //MAKTX (규격명)
    private String productName;
    //KWMENG (주문수량)
    private String orderQuantity;
    //VRKME (주문 단위)
    private String orderQuantityUnit;
    //AUDAT (주문일)
    private String orderStartDate;
    // (판매처)
    private String salesAccountCode;
    // (납품처)
    private String deliveryAccountCode;
    // (납품요청일)
    private String deliveryRequestDate;
    // (영업사원 번호)
    private String employeeNumber;
    // (영업사원명)
    private String employeeName;
}
