package com.daesang.springbatch.mdm.accountpartner.domain;

import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * fileName         : MdmAccountPartnerId
 * author           : 김수진과장
 * date             : 2022-11-18
 * descrition       : 고객정보 - Account 파트너 기능 배치
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-18       김수진과장             최초생성
 */

@NoArgsConstructor
public class MdmAccountPartnerId implements Serializable {

    private String mastid;
    private String areaid;
    private String parza;
    private String kunn2;

    public MdmAccountPartnerId(String mastid, String areaid, String parza, String kunn2) {
        this.mastid = mastid;
        this.areaid = areaid;
        this.parza = parza;
        this.kunn2 = kunn2;
    }
}
