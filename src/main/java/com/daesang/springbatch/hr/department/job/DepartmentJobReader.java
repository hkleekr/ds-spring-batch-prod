package com.daesang.springbatch.hr.department.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.hr.department.domain.Department;
import com.daesang.springbatch.hr.department.domain.DepartmentDto;
import com.daesang.springbatch.hr.department.repository.DepartmentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * fileName         : DepartmentJobReader
 * author           : 권용성사원
 * date             : 2022-10-26
 * descrition       : 통합인사 부서정보 배치 Reader
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-26       권용성사원             최초생성
 * 2022-11-07       권용성사원             Chunk 방식 변경
 */
@Slf4j
@RequiredArgsConstructor
public class DepartmentJobReader implements ItemReader<DepartmentDto> {

    private final DepartmentRepository departmentRepository;

    private final ModelMapper modelMapper;

    private List<DepartmentDto> departmentData;
    private int nextIndex;

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public DepartmentDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (departmentIsNotInitialized()){
            departmentData = fetchHrDepartmentDataFromAPI();
        }

        DepartmentDto mappingDto = null;

        if (nextIndex < departmentData.size()) {
            mappingDto = departmentData.get(nextIndex);

            nextIndex++;

        }else {
            nextIndex = 0;
            departmentData = null;
        }

        return mappingDto;
    }

    private boolean departmentIsNotInitialized(){
        return this.departmentData == null;
    }

    private List<DepartmentDto> fetchHrDepartmentDataFromAPI() throws JsonProcessingException, org.json.simple.parser.ParseException {
        try {
            List<Department> departmentList = departmentRepository.findAll();

            departmentData = departmentList.stream()
                    .map(Department -> modelMapper.map(Department, DepartmentDto.class))
                    .collect(Collectors.toList());

            if (departmentData.size() > 0) {
                log.info("데이터 조회 성공 - {} 건", departmentData.size());

                InterfaceInfo interfaceInfo = new InterfaceInfo();
                if (departmentData.size() % Constant.CHUNK_SIZE_L == 0){
                    interfaceInfo.setTotalPage(departmentData.size()/Constant.CHUNK_SIZE_L);
                }else {
                    interfaceInfo.setTotalPage(departmentData.size()/Constant.CHUNK_SIZE_L + 1);
                }
                interfaceInfo.setChunkSize(Constant.CHUNK_SIZE_L);
                interfaceInfo.setTotalCount(departmentData.size());

                Map<String, Object> map = new HashMap<>();
                map.put("IF", interfaceInfo);
                jobExecution.getExecutionContext().put("IF", interfaceInfo);

            } else {
                log.info("업데이트된 데이터가 없습니다.");
            }

        } catch (Exception e) {
            log.error("fetchDepartmentDataFromAPI: {}", e.getMessage());
        }

        return departmentData;
    }
}
