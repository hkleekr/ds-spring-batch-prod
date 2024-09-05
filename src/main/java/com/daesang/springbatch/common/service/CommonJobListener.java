package com.daesang.springbatch.common.service;

import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.util.BatchReportUtil;
import com.daesang.springbatch.common.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

/**
 * fileName         : CommonJobListener
 * author           : 권용성사원
 * date             : 2022-12-01
 * descrition       : 로그 공통 JOB LISTENER
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-12-01       권용성사원             최초생성
 */
@Component
@Slf4j
public class CommonJobListener extends JobExecutionListenerSupport {

    @Override
    public void afterJob(JobExecution jobExecution) {
        Object anIf = jobExecution.getExecutionContext().get("IF");
        InterfaceInfo interfaceInfo = (InterfaceInfo) anIf;
        Long start = jobExecution.getCreateTime().getTime();
        Long end = jobExecution.getEndTime().getTime();
        BatchReportUtil dailyBatch = BatchReportUtil.getInstance();

        if (jobExecution.getExitStatus().getExitCode().equals("FAILED")) {
            log.error("=============JOB_FINISH_STATUS=============");
            log.error("JOB_NAME:          {}", jobExecution.getJobInstance().getJobName());
            log.error("JOB_STATUS:        {}", jobExecution.getExitStatus().getExitCode());
            if (interfaceInfo != null) log.error("JOB_TOTAL_SIZE:    {}", interfaceInfo.getTotalCount());
            else log.error("JOB_TOTAL_SIZE:    {}", 0);
            log.error("JOB_START_TIME:    {}", CommonUtils.longToStrDateTime(start));
            log.error("JOB_END_TIME:      {}", CommonUtils.longToStrDateTime(end));
            log.error("JOB_DURATION_TIME: {}", CommonUtils.longToStrTime(end - start));
            log.error("===========================================");
            dailyBatch.setState(false
                    , jobExecution.getJobInstance().getJobName()
                    , CommonUtils.longToStrDateTime(start)
            );
        } else {
            log.info("=============JOB_FINISH_STATUS=============");
            log.info("JOB_NAME:          {}", jobExecution.getJobInstance().getJobName());
            log.info("JOB_STATUS:        {}", jobExecution.getExitStatus().getExitCode());
            if (interfaceInfo != null) log.info("JOB_TOTAL_SIZE:    {}", interfaceInfo.getTotalCount());
            else log.info("JOB_TOTAL_SIZE:    {}", 0);
            log.info("JOB_START_TIME:    {}", CommonUtils.longToStrDateTime(start));
            log.info("JOB_END_TIME:      {}", CommonUtils.longToStrDateTime(end));
            log.info("JOB_DURATION_TIME: {}", CommonUtils.longToStrTime(end - start));
            log.info("===========================================");
            dailyBatch.setState(true
                    , jobExecution.getJobInstance().getJobName()
                    , CommonUtils.longToStrDateTime(start)
            );
        }
    }
}
