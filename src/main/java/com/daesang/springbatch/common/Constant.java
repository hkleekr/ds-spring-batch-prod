package com.daesang.springbatch.common;

/**
 * fileName			: Constant
 * author			: 최종민차장
 * date				: 2022-10-28
 * descrition       : 상수 chunk
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-10-28			최종민차장             최초생성
 */
public class Constant {
    public static int CHUNK_SIZE = 5;
    // 40 < ColumnCount
    public static int CHUNK_SIZE_S = 100;
    // 20 < ColumnCount < 40
    public static int CHUNK_SIZE_M = 500;
    // ColumnCount < 20
    public static int CHUNK_SIZE_L = 1000;
    // RFC Destination 정보 prod
    public static String ABAP_AS_POOLED = "ABAP_AS_WITH_POOL";

    // RFC Destination 정보 dev
    public static String ABAP_MS = "ABAP_MS_WITHOUT_POOL";
}
