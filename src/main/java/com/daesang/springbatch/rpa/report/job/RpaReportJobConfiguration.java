package com.daesang.springbatch.rpa.report.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.common.util.MarkAnyUtil;
import com.daesang.springbatch.rpa.report.domain.RpaReportDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * fileName         : RpaReportJobConfiguration
 * author           : inayoon
 * date             : 2023-01-02
 * description      : 내수 주문 관련 시험성적서 파일 전송
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-01-02       inayoon             최초생성
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class RpaReportJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Value("${request.scg}")
    private String REQUEST_HOST;

    private final WebClientService webClientService;

    @Value("${file.rpapath}")
    private String RPA_ABSOLUTE_PATH;

    private final MarkAnyUtil markAnyUtil;

    @Bean
    public Job rpaReportJob() {
        return jobBuilderFactory.get("rpaReportJob")
                .incrementer(new RunIdIncrementer())
                .flow(rpaReportJobJobStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    public Step rpaReportJobJobStep() {
        return stepBuilderFactory.get("rpaReportJobJobStep")
                .<RpaReportDto, RpaReportDto>chunk(Constant.CHUNK_SIZE)
                .reader(rpaReportItemReader())
                .writer(rpaReportItemWriter())
                .build();
    }

    @Bean
    public ItemReader<RpaReportDto> rpaReportItemReader() {
        return new RpaReportJobReader(RPA_ABSOLUTE_PATH, markAnyUtil);
    }

    @Bean
    public ItemWriter<RpaReportDto> rpaReportItemWriter() {
        return new RpaReportJobWriter(webClientService, REQUEST_HOST, RPA_ABSOLUTE_PATH);
    }
}