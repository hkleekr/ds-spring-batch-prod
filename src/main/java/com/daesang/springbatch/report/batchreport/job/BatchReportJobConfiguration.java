package com.daesang.springbatch.report.batchreport.job;

import com.daesang.springbatch.common.util.BatchReportUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * fileName         : BatchReportJobConfiguration
 * author           : 권용성사원
 * date             : 2023-01-26
 * description      : 월별 배치 리포트 잡 설정
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-01-26       권용성사원             최초생성
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchReportJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private BatchReportUtil dailyBatch = BatchReportUtil.getInstance();

    @Bean
    public Job dailyBatchReportJob() {
        return jobBuilderFactory.get("dailyBatchReportJob")
                .incrementer(new RunIdIncrementer())
                .flow(dailyBatchReportJobStep())
                .end()
                .build();
    }

    /**
     * 월별 리포트 용 배치 Tasklet step
     * @return
     */
    @Bean
    public Step dailyBatchReportJobStep() {
        return stepBuilderFactory.get("dailyBatchReportJobStep")
                .tasklet((contribution, chunkContext) -> {
                    writeDailyBatchLog(dailyBatch.getState());
                    dailyBatch.resetState();
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    /**
     * 월별 리포트 용 로그 ( 일 배치 로그 누적 )
     * 01:00 ~ 23:00: 현재 일자 표시, 23:01 ~ 00:59: 지난 일자 표시
     * @param dailyBatchState: dailyBatchState 싱글톤(TOTAL_COUNT, JOB_COMPLETED_COUNT, JOB_FAILED_COUNT, FAILED_JOBS)
     */
    private void writeDailyBatchLog(Map<String, String> dailyBatchState) {
        LocalDateTime now = LocalDateTime.now();
        String date;
        if (now.getHour() >= 1 && now.getHour() <= 23)
            date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        else
            date = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        log.info("==========={} Daily Batch Log==========", date);
        for (String key : dailyBatchState.keySet()) {
            log.info("{}:        {}", key, dailyBatchState.get(key));
        }
        log.info("===============================================");
    }
}