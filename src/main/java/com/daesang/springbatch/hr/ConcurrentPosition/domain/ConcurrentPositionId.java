package com.daesang.springbatch.hr.ConcurrentPosition.domain;

import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * fileName         : OtherDepartmentId
 * author           : 권용성사원
 * date             : 2023-02-16
 * description      : 임직원 겸직 Multi Key ID Class
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-02-16       권용성사원           최초생성
 */
@NoArgsConstructor
public class ConcurrentPositionId implements Serializable {
    private String companyCode;
    private String orgNumber;

    public ConcurrentPositionId(String companyCode, String orgNumber) {
        this.companyCode = companyCode;
        this.orgNumber = orgNumber;
    }
}
