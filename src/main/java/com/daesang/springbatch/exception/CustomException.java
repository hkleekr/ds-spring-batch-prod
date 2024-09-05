package com.daesang.springbatch.exception;

import com.daesang.springbatch.common.enumerate.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * fileName			: CustomException
 * author			: 최종민차장
 * date				: 2022-11-23
 * descrition       : Batch Custom Exception
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-11-23			최종민차장             최초생성
 */

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;
}
