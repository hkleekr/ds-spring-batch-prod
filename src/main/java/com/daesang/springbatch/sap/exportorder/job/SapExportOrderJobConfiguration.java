package com.daesang.springbatch.sap.exportorder.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.sap.exportorder.domain.SapExportOrderDto;
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
 * fileName         : SapExportOrderJobConfiguration
 * author           : inayoon
 * date             : 2022-11-08
 * description      : 주문관리 - 수출 주문정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-08       inayoon             최초생성
 * 2022-11-22       inayoon             CHUNK_SIZE_S 변경
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class SapExportOrderJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ModelMapper modelMapper;
    @Value("${request.scg}")
    private String REQUEST_HOST;
    private final WebClientService webClientService;

    @Bean
    public Job sapExportOrderJob() {
        return jobBuilderFactory.get("sapExportOrderJob")
                .incrementer(new RunIdIncrementer())
                .flow(sapExportOrderJobStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    public Step sapExportOrderJobStep() {
        return stepBuilderFactory.get("sapExportOrderJobStep")
                .<SapExportOrderDto, SapExportOrderDto>chunk(Constant.CHUNK_SIZE_S)
                .reader(sapExportOrderItemReader())
                .writer(sapExportOrderItemWriter())
                .build();
    }

    @Bean
    public ItemReader<SapExportOrderDto> sapExportOrderItemReader() {
        return new SapExportOrderJobReader(modelMapper);
    }

    @Bean
    public ItemWriter<SapExportOrderDto> sapExportOrderItemWriter() {
        return new SapExportOrderJobWriter(webClientService, REQUEST_HOST);
    }

}