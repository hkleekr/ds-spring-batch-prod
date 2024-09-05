package com.daesang.springbatch.sap.domesticdelivery.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.sap.domesticdelivery.domain.SapDomesticDeliveryDto;
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
 * fileName         : SapDomesticDeliveryJobConfiguration
 * author           : inayoon
 * date             : 2022-11-07
 * description      : 배송관리 - 내수 배송정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-07       inayoon             최초생성
 * 2022-11-22       inayoon             CHUNK_SIZE_M 변경
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class SapDomesticDeliveryJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final ModelMapper modelMapper;

    @Value("${request.scg}")
    private String REQUEST_HOST;

    private final WebClientService webClientService;

    @Bean
    public Job sapDomesticDeliveryJob() {
        return jobBuilderFactory.get("sapDomesticDeliveryJob")
                .incrementer(new RunIdIncrementer())
                .flow(sapDomesticDeliveryJobStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    public Step sapDomesticDeliveryJobStep() {
        return stepBuilderFactory.get("sapDomesticDeliveryJobStep")
                .<SapDomesticDeliveryDto, SapDomesticDeliveryDto>chunk(Constant.CHUNK_SIZE_M)
                .reader(sapDomesticDeliveryItemReader())
                .writer(sapDomesticDeliveryItemWriter())
                .build();
    }

    @Bean
    public ItemReader<SapDomesticDeliveryDto> sapDomesticDeliveryItemReader() {
        return new SapDomesticDeliveryJobReader(modelMapper);
    }

    @Bean
    public ItemWriter<SapDomesticDeliveryDto> sapDomesticDeliveryItemWriter() {
        return new SapDomesticDeliveryJobWriter(webClientService, REQUEST_HOST);
    }

}