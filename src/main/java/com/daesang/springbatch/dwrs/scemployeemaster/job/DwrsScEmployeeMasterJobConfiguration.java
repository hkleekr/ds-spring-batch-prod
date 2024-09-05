package com.daesang.springbatch.dwrs.scemployeemaster.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.enumerate.UrlEnum;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.RestApiService;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.dwrs.scemployeemaster.domain.DwrsScEmployeeMasterMappingDto;
import com.daesang.springbatch.dwrs.scemployeemaster.domain.DwrsScEmployeeMasterRestDto;
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
public class DwrsScEmployeeMasterJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final WebClientService webClientService;
    @Value("${request.scg}")
    private String REQUEST_HOST;
    private final RestApiService<DwrsScEmployeeMasterRestDto> restApiService;

    @Bean
    public Job activateScEmployeeMasterJob() {

        return jobBuilderFactory.get("activateScEmployeeMasterJob")
                .incrementer(new RunIdIncrementer())
                .flow(activateScEmployeeMasterStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    public Step activateScEmployeeMasterStep() {
        return stepBuilderFactory.get("activateScEmployeeMasterStep")
                .<DwrsScEmployeeMasterMappingDto, DwrsScEmployeeMasterMappingDto>chunk(Constant.CHUNK_SIZE)
                .reader(dwrsScEmployeeMasteritemReader())
                .writer(dwrsScEmployeeMasteritemWriter())
                .build();
    }
    @Bean
    public ItemReader<DwrsScEmployeeMasterMappingDto> dwrsScEmployeeMasteritemReader(){
        return new DwrsScEmployeeMasterJobReader(REQUEST_HOST + UrlEnum.DWRS_91.getLegacyUrl(), restApiService);

    }

    @Bean
    public ItemWriter<DwrsScEmployeeMasterMappingDto> dwrsScEmployeeMasteritemWriter() {
        return new DwrsScEmployeeMasterJobWriter(webClientService, REQUEST_HOST);
    }


}


