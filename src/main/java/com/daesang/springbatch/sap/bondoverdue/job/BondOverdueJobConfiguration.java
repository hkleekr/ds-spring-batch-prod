package com.daesang.springbatch.sap.bondoverdue.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.sap.bondoverdue.domain.OverdueDto;
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
 * fileName			: BondOverdueJobConfiguration
 * author			: 최종민차장
 * date				: 2022-11-07
 * descrition       : 채권 연체 정보 batch
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-11-07			최종민차장             최초생성
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class BondOverdueJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final WebClientService webClientService;
    @Value("${request.scg}")
    private String REQUEST_HOST;

    @Bean
    public Job bondOverdueJob() {
        return jobBuilderFactory.get("bondOverdueJob")
                .incrementer(new RunIdIncrementer())
                .flow(bondOverdueStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    public Step bondOverdueStep() {
        return stepBuilderFactory.get("bondOverdueStep")
                .<OverdueDto, OverdueDto>chunk(Constant.CHUNK_SIZE_L)
                .reader(bondOverdueItemReader())
                .writer(bondOverdueItemWriter())
                .build();
    }

    @Bean
    public ItemReader<OverdueDto> bondOverdueItemReader() {
        return new BondOverdueJobReader();
    }

    @Bean
    public ItemWriter<OverdueDto> bondOverdueItemWriter() {
        return new BondOverdueJobWriter(webClientService, REQUEST_HOST);
    }

}
