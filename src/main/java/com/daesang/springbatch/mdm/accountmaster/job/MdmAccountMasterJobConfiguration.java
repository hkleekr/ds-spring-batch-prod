package com.daesang.springbatch.mdm.accountmaster.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.mdm.accountmaster.domain.MdmAccountMasterDto;
import com.daesang.springbatch.mdm.accountmaster.repository.MdmAccountMasterRepository;
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
 * fileName         : MdmAccountMaster
 * author           : inayoon
 * date             : 2022-10-19
 * description      : 고객정보 - Account 마스터 정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-19       inayoon             최초생성
 * 2022-11-02       inayoon             Chunk 방식 변경
 * 2022-11-22       inayoon             Chunk_SIZE_S 변경
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class MdmAccountMasterJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final MdmAccountMasterRepository mdmAccountMasterRepository;

    private final ModelMapper modelMapper;

    @Value("${request.scg}")
    private String REQUEST_HOST;

    private final WebClientService webClientService;

    @Bean
    public Job mdmAccountMasterJob() {
        return jobBuilderFactory.get("mdmAccountMasterJob")
                .incrementer(new RunIdIncrementer())
                .flow(mdmAccountMasterJobStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    @JobScope
    public Step mdmAccountMasterJobStep() {
        return stepBuilderFactory.get("mdmAccountMasterJobStep")
                .<MdmAccountMasterDto, MdmAccountMasterDto>chunk(Constant.CHUNK_SIZE_S)
                .reader(mdmAccountMasteritemReader())
                .writer(mdmAccountMasteritemWriter())
                .build();
    }

    @Bean
    public ItemReader<MdmAccountMasterDto> mdmAccountMasteritemReader() {
        return new MdmAccountMasterJobReader(mdmAccountMasterRepository, modelMapper);
    }

    @Bean
    public ItemWriter<MdmAccountMasterDto> mdmAccountMasteritemWriter() {
        return new MdmAccountMasterJobWriter(webClientService, REQUEST_HOST);
    }

}