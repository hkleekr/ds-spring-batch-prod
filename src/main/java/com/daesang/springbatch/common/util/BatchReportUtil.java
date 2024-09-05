package com.daesang.springbatch.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * fileName         : DailyJobLogState
 * author           : 권용성사원
 * date             : 2023-01-26
 * description      : 업무구분 - 상세업무 월별 Log 설정
 *                    로그 공통 JOB LISTENER 에서 호출
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-01-26       권용성사원             최초생성
 */
public class BatchReportUtil {
    private static BatchReportUtil instance = new BatchReportUtil();
    private final String delimiter = ", ";

    /**
     * Job 실행 Count 정보 초기화
     */
    private BatchReportUtil() {
        this.state = new HashMap<>();
        this.state.put("TOTAL_COUNT", "0");
        this.state.put("JOB_COMPLETED_COUNT", "0");
        this.state.put("JOB_FAILED_COUNT", "0");
        this.state.put("FAILED_JOBS", "");
    }
    private Map<String, String> state;
    public static BatchReportUtil getInstance() {
        return instance;
    }

    /**
     * Job 실행 Count 정보 Reset
     */
    public void resetState() {
        this.state.put("TOTAL_COUNT", "0");
        this.state.put("JOB_COMPLETED_COUNT", "0");
        this.state.put("JOB_FAILED_COUNT", "0");
        this.state.put("FAILED_JOBS", "");
    }

    /**
     * Job 실행 Count 정보 조회
     * @return
     */
    public Map<String, String> getState(){
        return this.state;
    }

    /**
     * Job 실행 정보 Set
     * @param jobState
     * @param jobName
     * @param jobStartTime
     */
    public void setState(boolean jobState, String jobName, String jobStartTime){
        countTotal();
        if(jobState) {
            countComplete();
        } else {
            countFail();
            writeFail(jobName, jobStartTime);
        }
    }

    private void countTotal(){
        int count = Integer.parseInt(this.state.get("TOTAL_COUNT"));
        this.state.put("TOTAL_COUNT", String.valueOf(++ count));
    }

    private void countComplete(){
        int count = Integer.parseInt(this.state.get("JOB_COMPLETED_COUNT"));
        this.state.put("JOB_COMPLETED_COUNT", String.valueOf(++ count));
    }

    private void countFail(){
        int count = Integer.parseInt(this.state.get("JOB_FAILED_COUNT"));
        this.state.put("JOB_FAILED_COUNT", String.valueOf(++ count));
    }

    /**
     * Job 실행 실패 시 write
     * jobName(실행시간)
     * @param jobName
     * @param jobStartTime
     */
    private void writeFail(String jobName, String jobStartTime){
        StringBuilder failBuilder = new StringBuilder(this.state.get("FAILED_JOBS"));
        failBuilder.append(failBuilder.toString().equals("") ? jobName : delimiter + jobName);
        this.state.put("FAILED_JOBS", failBuilder.toString()+"("+jobStartTime+")");
    }

}
