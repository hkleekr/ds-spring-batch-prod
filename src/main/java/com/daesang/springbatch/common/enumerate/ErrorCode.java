package com.daesang.springbatch.common.enumerate;

/**
 * fileName			: ErrorCode
 * author			: 최종민차장
 * date				: 2022-11-23
 * descrition       : Batch Job Error Custom Message
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-11-23			최종민차장             최초생성
 */
public enum ErrorCode {

    DATA_TRANSFER_ERROR("A001", "SalesForce Data Send Error");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
