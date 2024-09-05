package com.daesang.springbatch.hr.employee.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.hr.employee.domain.Employee;
import com.daesang.springbatch.hr.employee.domain.EmployeeDto;
import com.daesang.springbatch.hr.employee.repository.EmployeeRepository;
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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * fileName         : EmployeeJobReader
 * author           : 권용성사원
 * date             : 2022-10-26
 * descrition       : 통합인사 임직원 배치 아이템 Reader
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-26       권용성사원             최초생성
 * 2022-11-07       권용성사원             Chunk 방식 변경
 */
@Slf4j
@RequiredArgsConstructor
public class EmployeeJobReader implements ItemReader<EmployeeDto> {
    private final EmployeeRepository employeeRepository;

    private final ModelMapper modelMapper;

    private List<EmployeeDto> employeeData;
    private int nextIndex;

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public EmployeeDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (employeeIsNotInitialized()){
            employeeData = fetchEmployeeDataFromAPI();
        }

        EmployeeDto mappingDto = null;

        if (nextIndex < employeeData.size()) {
            mappingDto = employeeData.get(nextIndex);

            nextIndex++;

        }else {
            nextIndex = 0;
            employeeData = null;
        }

        return mappingDto;
    }

    private boolean employeeIsNotInitialized(){
        return this.employeeData == null;
    }

    private List<EmployeeDto> fetchEmployeeDataFromAPI() throws JsonProcessingException, org.json.simple.parser.ParseException {
        try {
            // 하루전 업데이트
            LocalDate yesterDay = LocalDate.now().minusDays(1);

//            List<Employee> employeeList = employeeRepository.findAllByCompanyCodeAndUpdateDateAfter("10", yesterDay);
            List<Employee> employeeList = employeeRepository.findAllByUpdateDateAfter(yesterDay);

            employeeData = employeeList.stream()
                    .map(Employee -> modelMapper.map(Employee, EmployeeDto.class))
                    .collect(Collectors.toList());

            if (employeeData.size() > 0) {
                log.info("데이터 조회 성공 - {} 건", employeeData.size());

                InterfaceInfo interfaceInfo = new InterfaceInfo();
                if (employeeData.size() % Constant.CHUNK_SIZE_M == 0){
                    interfaceInfo.setTotalPage(employeeData.size()/Constant.CHUNK_SIZE_M);
                }else {
                    interfaceInfo.setTotalPage(employeeData.size()/Constant.CHUNK_SIZE_M + 1);
                }
                interfaceInfo.setChunkSize(Constant.CHUNK_SIZE_M);
                interfaceInfo.setTotalCount(employeeData.size());

                Map<String, Object> map = new HashMap<>();
                map.put("IF", interfaceInfo);
                jobExecution.getExecutionContext().put("IF", interfaceInfo);

            } else {
                log.info("업데이트된 데이터가 없습니다.");
            }

        } catch (Exception e) {
            log.error("fetchEmployeeDataFromAPI: {}", e.getMessage());
        }

        return employeeData;
    }
}
