package com.daesang.springbatch.mdm.productmaster.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.mdm.productmaster.domain.MdmProductMasterDto;
import com.daesang.springbatch.mdm.productmaster.repository.MdmProductMasterRepository;
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
 * fileName         : MdmProductMasterJobConfiguration
 * author           : inayoon
 * date             : 2022-10-21
 * description      : 제품정보 - 제품 마스터 정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-21       inayoon             최초생성
 * 2022-11-22       inayoon             Chunk_SIZE_S 변경
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class MdmProductMasterJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final MdmProductMasterRepository mdmProductMasterRepository;

    private final ModelMapper modelMapper;

    @Value("${request.scg}")
    private String REQUEST_HOST;

    private final WebClientService webClientService;

    @Bean
    public Job mdmProductMasterJob() {
        return jobBuilderFactory.get("mdmProductMasterJob")
                .incrementer(new RunIdIncrementer())
                .flow(mdmProductMasterJobStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    @JobScope
    public Step mdmProductMasterJobStep() {
        return stepBuilderFactory.get("mdmProductMasterJobStep")
                .<MdmProductMasterDto, MdmProductMasterDto>chunk(Constant.CHUNK_SIZE_S)
                .reader(mdmProductMasterItemReader())
                .writer(mdmProductMasterItemWriter())
                .build();
    }

    @Bean
    public ItemReader<MdmProductMasterDto> mdmProductMasterItemReader() {
        return new MdmProductMasterJobReader(mdmProductMasterRepository, modelMapper);
    }

    @Bean
    public ItemWriter<MdmProductMasterDto> mdmProductMasterItemWriter() {
        return new MdmProductMasterJobWriter(webClientService, REQUEST_HOST);
    }

}