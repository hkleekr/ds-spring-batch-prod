package com.daesang.springbatch.common.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * fileName         : LogReportProperties
 * author           : 권용성사원
 * date             : 2023-03-03
 * description      : 업무구분 - 상세업무
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-03-03       권용성사원             최초생성
 */
@Data
public class LogReportProperties {
    private String[] filterTextArray;
    private String[] serverName;
    private Map<String, String> serviceDetail;
    private String reportFileDirPath;
    private String reportDownDirPath;
    private String fromAddress;
    private List<String> toAddress;
    private List<String> toCc;
    private List<String> toBcc;
}
