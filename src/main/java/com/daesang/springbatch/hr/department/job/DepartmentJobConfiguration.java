package com.daesang.springbatch.hr.department.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.hr.department.domain.DepartmentDto;
import com.daesang.springbatch.hr.department.repository.DepartmentRepository;
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
 * fileName         : DepartmentJobConfiguration
 * author           : 권용성사원
 * date             : 2022-10-26
 * descrition       : 통합인사 부서정보 배치 설정
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-26       권용성사원             최초생성
 * 2022-11-07       권용성사원             Chunk 방식 변경
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class DepartmentJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final DepartmentRepository departmentRepository;

    private final ModelMapper modelMapper;

    @Value("${request.scg}")
    private String REQUEST_HOST;

    private final WebClientService webClientService;

    @Bean
    public Job departmentJob() {
        return jobBuilderFactory.get("departmentJob")
                .incrementer(new RunIdIncrementer())
                .flow(departmentJobStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    @JobScope
    public Step departmentJobStep() {
        return stepBuilderFactory.get("DepartmentJobStep")
                .<DepartmentDto, DepartmentDto>chunk(Constant.CHUNK_SIZE_L)
                .reader(departmentItemReader())
                //.processor(itemProcessor())
                .writer(departmentItemWriter())
                .build();
    }

    @Bean
    public ItemReader<DepartmentDto> departmentItemReader(){
        return new DepartmentJobReader(departmentRepository, modelMapper);
    }

    @Bean
    public ItemWriter<DepartmentDto> departmentItemWriter() {
        return new DepartmentJobWriter(webClientService, REQUEST_HOST);
    }

}
