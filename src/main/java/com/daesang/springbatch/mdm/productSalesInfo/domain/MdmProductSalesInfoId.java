package com.daesang.springbatch.mdm.productSalesInfo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * fileName         : MdmProductSalesInfoId
 * author           : 권용성사원
 * date             : 2023-01-17
 * description      : 제품정보 - 제품 판매조직 기준 정보 뷰 멀티 키
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-01-17       권용성사원             최초생성
 */
@NoArgsConstructor
public class MdmProductSalesInfoId implements Serializable {

    private String mdmCode;
    private String areaCode;

    public MdmProductSalesInfoId(String mdmCode, String areaCode) {
        this.mdmCode = mdmCode;
        this.areaCode = areaCode;
    }
}
