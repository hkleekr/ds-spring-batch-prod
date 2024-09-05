package com.daesang.springbatch.sap.domesticorder.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.sap.domesticorder.domain.SapDomesticOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
 * fileName         : SapDomesticOrderJobReader
 * author           : inayoon
 * date             : 2022-11-04
 * description      : 주문관리 - 내수 주문정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-04       inayoon             최초생성
 * 2022-11-22       inayoon             CHUNK_SIZE_M 변경
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class SapDomesticOrderJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final ModelMapper modelMapper;

    @Value("${request.scg}")
    private String REQUEST_HOST;

    private final WebClientService webClientService;

    @Bean
    public Job sapDomesticOrderJob() {
        return jobBuilderFactory.get("sapDomesticOrderJob")
                .incrementer(new RunIdIncrementer())
                .flow(sapDomesticOrderJobStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    public Step sapDomesticOrderJobStep() {
        return stepBuilderFactory.get("sapDomesticOrderJobStep")
                .<SapDomesticOrderDto, SapDomesticOrderDto>chunk(Constant.CHUNK_SIZE_M)
                .reader(sapDomesticOrderItemReader())
                .writer(sapDomesticOrderItemWriter())
                .build();
    }

    @Bean
    public ItemReader<SapDomesticOrderDto> sapDomesticOrderItemReader() {
        return new SapDomesticOrderJobReader(modelMapper);
    }

    @Bean
    public ItemWriter<SapDomesticOrderDto> sapDomesticOrderItemWriter() {
        return new SapDomesticOrderJobWriter(webClientService, REQUEST_HOST);
    }

}