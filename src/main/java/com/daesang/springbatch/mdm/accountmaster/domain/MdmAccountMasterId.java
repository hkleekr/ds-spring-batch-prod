package com.daesang.springbatch.mdm.accountmaster.domain;

import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * fileName         : MdmAccountMasterId
 * author           : 김수진과장
 * date             : 2022-11-24
 * description      :
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-24       김수진과장             최초생성
 */

@NoArgsConstructor
public class MdmAccountMasterId implements Serializable {

    public MdmAccountMasterId(String mdmCode, String areaCode) {
        this.mdmCode = mdmCode;
        this.areaCode = areaCode;
    }

    private String mdmCode;
    private String areaCode;
}
