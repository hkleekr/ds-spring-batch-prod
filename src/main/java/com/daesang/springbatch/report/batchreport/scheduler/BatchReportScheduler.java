package com.daesang.springbatch.report.batchreport.scheduler;

import com.daesang.springbatch.report.batchreport.job.BatchReportJobConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * fileName			: BatchReportScheduler
 * author			: 권용성사원
 * date				: 2023-01-26
 * description      : 월별 배치 리포트 잡 스케쥴러
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2023-01-26			권용성사원             최초생성
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BatchReportScheduler {
    private final JobLauncher jobLauncher;
    private final BatchReportJobConfiguration batchReportJobConfiguration;

    /**
     * 일별 전체 JOB 로그, 전일 로그 데이터 기준
     * Schedule Time - AM00:00
     * @throws Exception
     */
    @Scheduled(cron = "0 0 0 * * *")
    private void runDailyReportJob() {
        JobExecution execution;
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        try {
            execution = jobLauncher.run(batchReportJobConfiguration.dailyBatchReportJob(), new JobParameters(confMap));
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

}
