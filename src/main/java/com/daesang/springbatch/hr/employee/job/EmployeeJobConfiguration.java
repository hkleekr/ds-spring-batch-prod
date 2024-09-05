package com.daesang.springbatch.hr.employee.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.hr.employee.domain.EmployeeDto;
import com.daesang.springbatch.hr.employee.repository.EmployeeRepository;
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
 * fileName         : EmployeeJobConfiguration
 * author           : 권용성사원
 * date             : 2022-10-26
 * descrition       : 통합인사 임직원 배치 설정
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-26       권용성사원             최초생성
 * 2022-11-07       권용성사원             Chunk 방식 변경
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class EmployeeJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final EmployeeRepository employeeRepository;

    private final ModelMapper modelMapper;

    @Value("${request.scg}")
    private String REQUEST_HOST;

    private final WebClientService webClientService;

    @Bean
    public Job employeeJob() {
        return jobBuilderFactory.get("employeeJob")
                .incrementer(new RunIdIncrementer())
                .flow(employeeJobStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    @JobScope
    public Step employeeJobStep() {
        return stepBuilderFactory.get("employeeJobStep")
                .<EmployeeDto, EmployeeDto>chunk(Constant.CHUNK_SIZE_M)
                .reader(employeeItemReader())
                //.processor(itemProcessor())
                .writer(employeeItemWriter())
                .build();
    }

    @Bean
    public ItemReader<EmployeeDto> employeeItemReader(){
        return new EmployeeJobReader(employeeRepository, modelMapper);
    }

    @Bean
    public ItemWriter<EmployeeDto> employeeItemWriter() {
        return new EmployeeJobWriter(webClientService, REQUEST_HOST);
    }

}
