package com.daesang.springbatch.dwrs.scemployee.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.enumerate.UrlEnum;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.RestApiService;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.dwrs.scemployee.domain.DwrsScEmployeeMappingDto;
import com.daesang.springbatch.dwrs.scemployee.domain.DwrsScEmployeeRestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
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
public class DwrsScEmployeeJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final WebClientService webClientService;
    @Value("${request.scg}")
    private String REQUEST_HOST;

    private final RestApiService<DwrsScEmployeeRestDto> restApiService;

    @Bean
    public Job activateScEmployeeJob() {

        return jobBuilderFactory.get("activateScEmployeeJob")
                .incrementer(new RunIdIncrementer())
                .flow(activateScEmployeeStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    public Step activateScEmployeeStep() {
        return stepBuilderFactory.get("activateScEmployeeStep")
                .<DwrsScEmployeeMappingDto, DwrsScEmployeeMappingDto>chunk(Constant.CHUNK_SIZE_L)
                .reader(dwrsScEmployeeitemReader())
                //.processor(itemProcessor())
                .writer(dwrsScEmployeeitemWriter())
                .build();
    }
    @Bean
    public ItemReader<DwrsScEmployeeMappingDto> dwrsScEmployeeitemReader(){
        return new DwrsScEmployeeJobReader(UrlEnum.DWRS_53.getLegacyUrl(), restApiService);
    }

    @Bean
    public ItemWriter<DwrsScEmployeeMappingDto> dwrsScEmployeeitemWriter() {
        return new DwrsScEmployeeJobWriter(webClientService, REQUEST_HOST);
    }


}


