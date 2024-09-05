package com.daesang.springbatch.report.logreport.job;

import com.daesang.springbatch.common.domain.LogReportProperties;
import com.daesang.springbatch.report.logreport.service.LogReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * fileName         : LogReportJobConfiguration
 * author           : 권용성사원
 * date             : 2023-01-26
 * description      : 월별 로그 리포트 메일링 배치
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-01-26       권용성사원             최초생성
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class LogReportJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final LogReportService logReportService;
    private final LogReportProperties logReportProperties;

    /**
     * 월별 로그 리포트 메일링 용 배치 Tasklet Job
     */
    @Bean
    public Job LogReportJob() {
        return jobBuilderFactory.get("LogReportJob")
                .incrementer(new RunIdIncrementer())
                .flow(LogReportJobStep())
                .end()
                .build();
    }

    /**
     * 월별 로그 리포트 메일링 용 배치 Tasklet step
     */
    @Bean
    @JobScope
    public Step LogReportJobStep() {
        return stepBuilderFactory.get("LogReportJobStep")
                .tasklet(LogReportJobTasklet())
                .build();
    }

    /**
     * 월별 로그 리포트 메일용 Tasklet
     */
    @Bean
    @StepScope
    public LogReportJobTasklet LogReportJobTasklet() {
        return new LogReportJobTasklet(logReportService, logReportProperties);
    }


}