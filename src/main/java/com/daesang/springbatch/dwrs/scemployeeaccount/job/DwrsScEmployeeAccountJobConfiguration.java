package com.daesang.springbatch.dwrs.scemployeeaccount.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.enumerate.UrlEnum;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.RestApiService;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.dwrs.scemployeeaccount.domain.DwrsScEmployeeAccountRestDto;
import com.daesang.springbatch.dwrs.scemployeeaccount.domain.DwrsScEmployeeAccountMappingDto;
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

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DwrsScEmployeeAccountJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final WebClientService webClientService;
    @Value("${request.scg}")
    private String REQUEST_HOST;
    private final RestApiService<DwrsScEmployeeAccountRestDto> restApiService;

    @Bean
    public Job activateScEmployeeAccountJob() {

        return jobBuilderFactory.get("activateScEmployeeAccountJob")
                .incrementer(new RunIdIncrementer())
                .flow(activateScEmployeeAccountStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    public Step activateScEmployeeAccountStep() {
        return stepBuilderFactory.get("activateProductStep")
                .<DwrsScEmployeeAccountMappingDto, DwrsScEmployeeAccountMappingDto>chunk(Constant.CHUNK_SIZE_L)
                .reader(itemReader())
                //.processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }
    @Bean
    public ItemReader<DwrsScEmployeeAccountMappingDto> itemReader(){
        return new DwrsScEmployeeAccountJobReader(UrlEnum.DWRS_18.getLegacyUrl(), restApiService);

    }

    @Bean
    public ItemWriter<DwrsScEmployeeAccountMappingDto> itemWriter() {
        return new DwrsScEmployeeAccountJobWriter(webClientService, REQUEST_HOST);
    }


}


