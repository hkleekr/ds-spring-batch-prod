package com.daesang.springbatch.sap.exportloan.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.sap.exportloan.domain.SapExportLoanDto;
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

/**
 * fileName         : SapExportLoanJobConfiguration
 * author           : 김수진과장
 * date             : 2022-11-09
 * descrition       : 수출 Account 여신 Config
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-09       김수진과장             최초생성
 * 2022-11-10       최종민차장             서비스 수정
 */

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SapExportLoanJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final WebClientService webClientService;
    @Value("${request.scg}")
    private String REQUEST_HOST;

    @Bean
    public Job sapExportLoanJob() {
        return jobBuilderFactory.get("sapExportLoanJob")
                .incrementer(new RunIdIncrementer())
                .flow(sapExportLoanStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    public Step sapExportLoanStep() {
        return stepBuilderFactory.get("sapExportLoanStep")
                .<SapExportLoanDto, SapExportLoanDto>chunk(Constant.CHUNK_SIZE_L)
                .reader(sapExportLoanDtoItemReader())
                .writer(sapExportLoanDtoItemWriter())
                .build();
    }
    @Bean
    public ItemReader<SapExportLoanDto> sapExportLoanDtoItemReader(){
        return new SapExportLoanJobReader();
    }

    @Bean
    public ItemWriter<SapExportLoanDto> sapExportLoanDtoItemWriter() {
        return new SapExportLoanJobWriter(webClientService, REQUEST_HOST);
    }

}
