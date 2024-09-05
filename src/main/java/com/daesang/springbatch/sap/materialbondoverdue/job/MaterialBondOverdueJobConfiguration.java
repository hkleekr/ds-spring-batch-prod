package com.daesang.springbatch.sap.materialbondoverdue.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.sap.materialbondoverdue.domain.MaterialOverdueDto;
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
 * fileName			: MaterialBondOverdueJobConfiguration
 * author			: 최종민차장
 * date				: 2023-02-01
 * descrition       : (소재) Account 내수 연체 정보 조회 Batch
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2023-02-01			최종민차장             최초생성
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class MaterialBondOverdueJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final WebClientService webClientService;
    @Value("${request.scg}")
    private String REQUEST_HOST;

    @Bean
    public Job materialBondOverdueJob() {
        return jobBuilderFactory.get("materialBondOverdueJob")
                .incrementer(new RunIdIncrementer())
                .flow(materialBondOverdueStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    public Step materialBondOverdueStep() {
        return stepBuilderFactory.get("materialBondOverdueStep")
                .<MaterialOverdueDto, MaterialOverdueDto>chunk(Constant.CHUNK_SIZE_L)
                .reader(materialBondOverdueItemReader())
                .writer(materialBondOverdueItemWriter())
                .build();
    }

    @Bean
    public ItemReader<MaterialOverdueDto> materialBondOverdueItemReader() {
        return new MaterialBondOverdueJobReader();
    }

    @Bean
    public ItemWriter<MaterialOverdueDto> materialBondOverdueItemWriter() {
        return new MaterialBondOverdueJobWriter(webClientService, REQUEST_HOST);
    }
}
