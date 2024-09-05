package com.daesang.springbatch.sap.domestictransport.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.sap.domestictransport.domain.SapDomesticTransportDto;
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
 * fileName         : SapDomesticTransportJobConfiguration
 * author           : inayoon
 * date             : 2022-11-17
 * description      : 배송관리 - 내수 납품정보(LE)
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-17       inayoon             최초생성
 * 2022-11-22       inayoon             CHUNK_SIZE_L 변경
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class SapDomesticTransportJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ModelMapper modelMapper;
    @Value("${request.scg}")
    private String REQUEST_HOST;
    private final WebClientService webClientService;

    @Bean
    public Job sapDomesticTransportJob() {
        return jobBuilderFactory.get("sapDomesticTransportJob")
                .incrementer(new RunIdIncrementer())
                .flow(sapDomesticTransportJobStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    public Step sapDomesticTransportJobStep() {
        return stepBuilderFactory.get("sapDomesticTransportJobStep")
                .<SapDomesticTransportDto, SapDomesticTransportDto>chunk(Constant.CHUNK_SIZE_L)
                .reader(sapDomesticTransportItemReader())
                .writer(sapDomesticTransportItemWriter())
                .build();
    }

    @Bean
    public ItemReader<SapDomesticTransportDto> sapDomesticTransportItemReader() {
        return new SapDomesticTransportJobReader(modelMapper);
    }

    @Bean
    public ItemWriter<SapDomesticTransportDto> sapDomesticTransportItemWriter() {
        return new SapDomesticTransportJobWriter(webClientService, REQUEST_HOST);
    }

}