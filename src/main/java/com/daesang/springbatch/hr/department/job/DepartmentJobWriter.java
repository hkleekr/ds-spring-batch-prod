package com.daesang.springbatch.hr.department.job;

import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.enumerate.UrlEnum;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.hr.department.domain.DepartmentDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * fileName         : DepartmentJobReader
 * author           : 권용성사원
 * date             : 2022-10-26
 * descrition       : 통합인사 부서정보 배치 Writer
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-26       권용성사원             최초생성
 * 2022-11-07       권용성사원             Chunk 방식 변경
 */
@Slf4j
@RequiredArgsConstructor
public class DepartmentJobWriter implements ItemWriter<DepartmentDto> {

    private final WebClientService webClientService;

    private final String REQUEST_HOST;

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public void write(List<? extends DepartmentDto> items) throws Exception {
        JSONObject jsonObject = new JSONObject();

        JSONParser parser = new JSONParser();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(items);
        JSONArray jsonArray = (JSONArray) parser.parse(jsonString);

        Object anIf = jobExecution.getExecutionContext().get("IF");
        InterfaceInfo interfaceInfo = (InterfaceInfo) anIf;
        jobExecution.getStepExecutions().stream().forEach(stepExecution -> jsonObject.put("currentPage", stepExecution.getCommitCount() + 1));
        jsonObject.put("totalCount", interfaceInfo.getTotalCount());
        jsonObject.put("totalPage", interfaceInfo.getTotalPage());
        jsonObject.put("request", jsonArray);

        webClientService.getGatewayResponse(jsonObject, REQUEST_HOST, UrlEnum.HR_60);
    }

}
