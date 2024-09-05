package com.daesang.springbatch.report.logreport.scheduler;

import com.daesang.springbatch.common.util.CommonUtils;
import com.daesang.springbatch.report.logreport.job.LogReportJobConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * fileName			: LogReportScheduler
 * author			: 권용성사원
 * date				: 2023-01-26
 * description      : 월별 로그 리포트 메일링 스케쥴
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2023-01-26			권용성사원             최초생성
 */
@Slf4j
@RequiredArgsConstructor
@Component
@Profile("prod")
public class LogReportScheduler {
    private final JobLauncher jobLauncher;
    private final LogReportJobConfiguration logReportJobConfiguration;

    /**
     * 연동서버 로그 리포트 스케쥴: DIS실 메일 전송
     * 매월 1일 00시 05분
     * @throws IOException
     * @throws MessagingException
     */
    @Scheduled(cron = "0 5 0 1 1/1 *")
    private void runDailyReportJob() {
        JobExecution execution;
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        confMap.put("month", new JobParameter(CommonUtils.getLastMonth(1, "yyyyMM")));
        try {
            execution = jobLauncher.run(logReportJobConfiguration.LogReportJob(), new JobParameters(confMap));
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

}
