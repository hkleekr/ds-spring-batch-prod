package com.daesang.springbatch.mdm.productSalesInfo.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.mdm.productSalesInfo.domain.MdmProductSalesInfoDto;
import com.daesang.springbatch.mdm.productSalesInfo.repository.MdmProductSalesInfoRepositorySupport;
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
 * fileName         : MdmProductSalesInfoJobConfiguration
 * author           : 권용성사원
 * date             : 2023-01-17
 * description      : 제품정보 - 제품 판매조직 기준 정보 배치 설정
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-01-17       권용성사원             최초생성
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class MdmProductSalesInfoJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final MdmProductSalesInfoRepositorySupport MdmProductSalesInfoRepositorySupport;

    private final ModelMapper modelMapper;

    @Value("${request.scg}")
    private String REQUEST_HOST;

    private final WebClientService webClientService;

    @Bean
    public Job mdmProductSalesInfoJob() {
        return jobBuilderFactory.get("mdmProductSalesInfoJob")
                .incrementer(new RunIdIncrementer())
                .flow(mdmProductSalesInfoJobStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    @JobScope
    public Step mdmProductSalesInfoJobStep() {
        return stepBuilderFactory.get("mdmProductSalesInfoJobStep")
                .<MdmProductSalesInfoDto, MdmProductSalesInfoDto>chunk(Constant.CHUNK_SIZE_S)
                .reader(mdmProductSalesInfoItemReader())
                .writer(mdmProductSalesInfoItemWriter())
                .build();
    }

    @Bean
    public ItemReader<MdmProductSalesInfoDto> mdmProductSalesInfoItemReader() {
        return new MdmProductSalesInfoJobReader(MdmProductSalesInfoRepositorySupport, modelMapper);
    }

    @Bean
    public ItemWriter<MdmProductSalesInfoDto> mdmProductSalesInfoItemWriter() {
        return new MdmProductSalesInfoJobWriter(webClientService, REQUEST_HOST);
    }

}