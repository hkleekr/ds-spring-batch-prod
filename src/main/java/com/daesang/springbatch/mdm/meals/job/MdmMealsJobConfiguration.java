package com.daesang.springbatch.mdm.meals.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.service.CommonJobListener;
import com.daesang.springbatch.common.service.RestApiService;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.mdm.meals.domain.MdmMealsDto;
import com.daesang.springbatch.mdm.meals.repository.MdmMealsRepository;
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
 * fileName         : MdmMealsJobConfiguration
 * author           : 김수진과장
 * date             : 2022-11-02
 * descrition       :고객정보 - 급식거래처 조회
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-02       김수진과장             최초생성
 * 2022-11-22       김수진과장             청크사이즈변경
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class MdmMealsJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final WebClientService webClientService;
    @Value("${request.scg}")
    private String REQUEST_HOST;
    private final ModelMapper modelMapper;

    private final RestApiService<MdmMealsDto> restApiService;

    private final MdmMealsRepository mdmMealsRepository;

    @Bean
    public Job mdmMealsJob() {
        return jobBuilderFactory.get("mdmMealsJob")
                .incrementer(new RunIdIncrementer())
                .flow(mdmMealsStep())
                .end()
                .listener(new CommonJobListener())
                .build();
    }

    @Bean
    @JobScope
    public Step mdmMealsStep() {
        return stepBuilderFactory.get("mdmMealsStep")
                .<MdmMealsDto, MdmMealsDto>chunk(Constant.CHUNK_SIZE_M)
                .reader(mdmMealsDtoItemReader())
                //.processor(itemProcessor())
                .writer(mdmMealsDtoItemWriter())
                .build();
    }


    public ItemReader<MdmMealsDto> mdmMealsDtoItemReader() {
        return new MdmMealsJobReader(mdmMealsRepository, restApiService, modelMapper);
    }

    public ItemWriter<MdmMealsDto> mdmMealsDtoItemWriter() {
        return new MdmMealsJobWriter(webClientService, REQUEST_HOST);
    }
}
