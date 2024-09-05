package com.daesang.springbatch.hr.ConcurrentPosition.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.hr.ConcurrentPosition.domain.ConcurrentPositionDto;
import com.daesang.springbatch.hr.ConcurrentPosition.repository.ConcurrentPositionRepositorySupport;
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
 * fileName         : OtherConcurrentPositionJobConfiguration
 * author           : 권용성사원
 * date             : 2023-02-16
 * description      : 임직원 겸직 인터페이스 배치 잡 설정
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-02-16       권용성사원           최초생성
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class ConcurrentPositionJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final ConcurrentPositionRepositorySupport repository;

    private final ModelMapper modelMapper;

    @Value("${request.scg}")
    private String REQUEST_HOST;

    private final WebClientService webClientService;

    @Bean
    public Job ConcurrentPositionJob() {
        return jobBuilderFactory.get("ConcurrentPositionJob")
                .incrementer(new RunIdIncrementer())
                .flow(ConcurrentPositionJobStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    @JobScope
    public Step ConcurrentPositionJobStep() {
        return stepBuilderFactory.get("ConcurrentPositionJobStep")
                .<ConcurrentPositionDto, ConcurrentPositionDto>chunk(Constant.CHUNK_SIZE_L)
                .reader(concurrentPositionJobReader())
                .writer(concurrentPositionJobWriter())
                .build();
    }

    @Bean
    public ItemReader<ConcurrentPositionDto> concurrentPositionJobReader(){
        return new ConcurrentPositionJobReader(repository, modelMapper);
    }

    @Bean
    public ItemWriter<ConcurrentPositionDto> concurrentPositionJobWriter() {
        return new ConcurrentPositionJobWriter(webClientService, REQUEST_HOST);
    }

}
