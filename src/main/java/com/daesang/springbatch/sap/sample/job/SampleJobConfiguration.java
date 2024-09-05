package com.daesang.springbatch.sap.sample.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.sap.sample.domain.SampleDto;
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
 * fileName			: SampleJobConfiguration
 * author			: 최종민차장
 * date				: 2022-11-09
 * descrition       : 무상 주문 정보 batch 조회
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-11-09			최종민차장             최초생성
 */

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SampleJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final WebClientService webClientService;

    @Value("${request.scg}")
    private String REQUEST_HOST;

    @Bean
    public Job sampleJob() {
        return jobBuilderFactory.get("sampleJob")
                .incrementer(new RunIdIncrementer())
                .flow(sampleStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    public Step sampleStep() {
        return stepBuilderFactory.get("sampleStep")
                .<SampleDto, SampleDto>chunk(Constant.CHUNK_SIZE_L)
                .reader(sampleItemReader())
                .writer(sampleItemWriter())
                .build();
    }

    @Bean
    public ItemReader<SampleDto> sampleItemReader() {
        return new SampleJobReader();
    }

    @Bean
    public ItemWriter<SampleDto> sampleItemWriter() {
        return new SampleJobWriter(webClientService, REQUEST_HOST);
    }
}
