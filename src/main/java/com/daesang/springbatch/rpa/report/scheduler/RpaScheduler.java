package com.daesang.springbatch.rpa.report.scheduler;

import com.daesang.springbatch.rpa.report.job.RpaReportJobConfiguration;
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
 * fileName			: RpaScheduler
 * author			: 최종민차장
 * date				: 2023-01-03
 * description      : 시험성적서 fileUpload 스케쥴러
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2023-01-03			최종민차장             최초생성
 */
@Slf4j
@RequiredArgsConstructor
//@Component
public class RpaScheduler {
    private final JobLauncher jobLauncher;
    private final RpaReportJobConfiguration rpaReportJobConfiguration;

    /**
     * 내수 주문 관련 시험성적서 파일
     * Schedule Time - AM9:00, AM12:00, PM17:00
     * @throws Exception
     */
    @Scheduled(cron = "0 0 8 * * *")
    @Scheduled(cron = "0 0 12 * * *")
    @Scheduled(cron = "0 0 17 * * *")
    private void runRpaReportJob() {
        JobExecution execution;
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));

        try {
            log.info(">>>>>>>>> start rpaReportJob");
            execution = jobLauncher.run(rpaReportJobConfiguration.rpaReportJob(), new JobParameters(confMap));
            log.info(">>>>>>>>> Job finished with status : " + execution.getStatus());
            log.info(">>>>>>>>> Current Thread: {}", Thread.currentThread().getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

}
