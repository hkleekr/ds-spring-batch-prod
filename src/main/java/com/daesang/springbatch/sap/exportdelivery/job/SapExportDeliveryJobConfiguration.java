package com.daesang.springbatch.sap.exportdelivery.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.sap.exportdelivery.domain.SapExportDeliveryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * fileName         : SapExportDeliveryJobConfiguration
 * author           : inayoon
 * date             : 2022-11-09
 * description      : 배송관리 - 수출 배송정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-09       inayoon             최초생성
 * 2022-11-22       inayoon             CHUNK_SIZE_S 변경
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class SapExportDeliveryJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final ModelMapper modelMapper;

    @Value("${request.scg}")
    private String REQUEST_HOST;

    private final WebClientService webClientService;

    @Bean
    public Job sapExportDeliveryJob() {
        return jobBuilderFactory.get("sapExportDeliveryJob")
                .incrementer(new RunIdIncrementer())
                .flow(sapExportDeliveryJobStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    @JobScope
    public Step sapExportDeliveryJobStep() {
        return stepBuilderFactory.get("sapExportDeliveryJobStep")
                .<SapExportDeliveryDto, SapExportDeliveryDto>chunk(Constant.CHUNK_SIZE_S)
                .reader(sapExportDeliveryItemReader())
                .writer(sapExportDeliveryItemWriter())
                .build();
    }

    @Bean
    public ItemReader<SapExportDeliveryDto> sapExportDeliveryItemReader() {
        return new SapExportDeliveryJobReader(modelMapper);
    }

    @Bean
    public ItemWriter<SapExportDeliveryDto> sapExportDeliveryItemWriter() {
        return new SapExportDeliveryJobWriter(webClientService, REQUEST_HOST);
    }

}