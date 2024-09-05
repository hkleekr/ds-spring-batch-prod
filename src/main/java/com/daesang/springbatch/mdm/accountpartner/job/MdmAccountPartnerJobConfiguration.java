package com.daesang.springbatch.mdm.accountpartner.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.RestApiService;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.mdm.accountpartner.domain.MdmAccountPartnerDto;
import com.daesang.springbatch.mdm.accountpartner.repository.MdmAccountPartnerRepositorySupport;
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
 * fileName         : MdmAccountPartnerJobConfiguration
 * author           : 김수진과장
 * date             : 2022-11-10
 * descrition       :
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-10       김수진과장             최초생성
 * 2022-11-22       김수진과장             청크사이즈변경
 */

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MdmAccountPartnerJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final WebClientService webClientService;
    @Value("${request.scg}")
    private String REQUEST_HOST;
    private final ModelMapper modelMapper;
    private final RestApiService<MdmAccountPartnerDto> restApiService;
    private final MdmAccountPartnerRepositorySupport mdmAccountPartnerRepositorySupport;

    @Bean
    public Job mdmAccountPartnerJob() {
        return jobBuilderFactory.get("mdmAccountPartnerJob")
                .incrementer(new RunIdIncrementer())
                .flow(mdmAccountPartnerStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    @JobScope
    public Step mdmAccountPartnerStep() {
        return stepBuilderFactory.get("mdmAccountPartnerStep")
                .<MdmAccountPartnerDto, MdmAccountPartnerDto>chunk(Constant.CHUNK_SIZE_L)
                .reader(mdmAccountPartnerDtoItemReader())
                //.processor(itemProcessor())
                .writer(mdmAccountPartnerDtoItemWriter())
                .build();
    }

    public ItemReader<MdmAccountPartnerDto> mdmAccountPartnerDtoItemReader() {
        return new MdmAccountPartnerJobReader(mdmAccountPartnerRepositorySupport,restApiService,modelMapper);
    }

    public ItemWriter<MdmAccountPartnerDto> mdmAccountPartnerDtoItemWriter() {
        return new MdmAccountPartnerJobWriter(webClientService,REQUEST_HOST);
    }
}
