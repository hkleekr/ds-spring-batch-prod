package com.daesang.springbatch.mdm.meals.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.service.RestApiService;
import com.daesang.springbatch.mdm.meals.domain.MdmMeals;
import com.daesang.springbatch.mdm.meals.domain.MdmMealsDto;
import com.daesang.springbatch.mdm.meals.repository.MdmMealsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * fileName         : MdmMealsJobReader
 * author           : 김수진과장
 * date             : 2022-11-02
 * descrition       : 고객정보 - 급식거래처 조회
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-02       김수진과장             최초생성
 * 2022-11-14       김수진과장             마지막 수정일을 기준으로 조회 기능 추가
 */
@Slf4j
@RequiredArgsConstructor
public class MdmMealsJobReader implements ItemReader<MdmMealsDto> {

    private final MdmMealsRepository mdmMealsRepository;
    private List<MdmMealsDto> mdmMeals;

    private final RestApiService<MdmMealsDto> restApiService;
    private final ModelMapper modelMapper;
    private int nextIndex;
    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public MdmMealsDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if (mdmMealsDataIsNotInitialized()) {
            mdmMeals = fetchMdmMealsData();
        }

        MdmMealsDto mealsDto = null;

        if (nextIndex < mdmMeals.size()) {
            mealsDto = mdmMeals.get(nextIndex);
            nextIndex++;
        }  else {
            nextIndex = 0;
            mdmMeals = null;
        }
        return mealsDto;
    }

    private boolean mdmMealsDataIsNotInitialized(){
        return this.mdmMeals == null;
    }

    private List<MdmMealsDto> fetchMdmMealsData() throws JsonProcessingException, org.json.simple.parser.ParseException {

        try {
            Map<String, JobParameter> parameters = jobExecution.getJobParameters().getParameters();
            String fromDate = String.valueOf(parameters.get("fromDate"));
            String toDate = String.valueOf(parameters.get("toDate"));
            log.info(fromDate);
            log.info(toDate);
            List<MdmMeals> mdmMealsList = mdmMealsRepository.findByLastmodificationdtimeBetween(fromDate,toDate);

            mdmMeals = mdmMealsList.stream()
                                   .map(meals -> modelMapper.map(meals, MdmMealsDto.class))
                                   .collect(Collectors.toList());

            if (mdmMealsList.size() > 0) {
                log.info("데이터 조회 성공 - {} 건", mdmMealsList.size());

                InterfaceInfo interfaceInfo = new InterfaceInfo();

                if (mdmMealsList.size() % Constant.CHUNK_SIZE_M == 0) {
                    interfaceInfo.setTotalPage(mdmMealsList.size() / Constant.CHUNK_SIZE_M);
                } else {
                    interfaceInfo.setTotalPage(mdmMealsList.size() / Constant.CHUNK_SIZE_M + 1);
                }

                interfaceInfo.setChunkSize(Constant.CHUNK_SIZE_M);
                interfaceInfo.setTotalCount(mdmMealsList.size());

                jobExecution.getExecutionContext().put("IF", interfaceInfo);

            } else {
                log.info("업데이트된 데이터가 없습니다.");
            }

        } catch (Exception e) {
            log.error("fetchMdmMealsData: {}", e.getMessage());
        }

        return mdmMeals;
    }

}
