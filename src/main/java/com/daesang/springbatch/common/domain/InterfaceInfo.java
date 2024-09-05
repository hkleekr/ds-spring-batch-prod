package com.daesang.springbatch.common.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * fileName			: InterfaceInfo
 * author			: 최종민차장
 * date				: 2022-10-31
 * descrition       : SalesForce와 Intereface시 약속 된 Defalut Param 정보
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-10-31			최종민차장             최초생성
 */
@Getter
@Setter
public class InterfaceInfo implements Serializable {
    private int chunkSize;  //data 전달 size
    private int totalCount;
    private int totalPage;
}
