package com.daesang.springbatch.sap.domesticorder.job;

import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.enumerate.UrlEnum;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.sap.domesticorder.domain.SapDomesticOrderDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Map;

/**
 * fileName         : SapDomesticOrderJobWriter
 * author           : inayoon
 * date             : 2022-11-04
 * description      : 주문관리 - 내수 주문정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-04       inayoon             최초생성
 */
@Slf4j
@RequiredArgsConstructor
public class SapDomesticOrderJobWriter implements ItemWriter<SapDomesticOrderDto> {

    private final WebClientService webClientService;

    private final String REQUEST_HOST;

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public void write(List<? extends SapDomesticOrderDto> items) throws Exception {
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

        Map<String, JobParameter> parameters = jobExecution.getJobParameters().getParameters();
        jsonObject.put("ifTerm", parameters.get("ifTerm").getValue());

        webClientService.getGatewayResponse(jsonObject, REQUEST_HOST, UrlEnum.SAP_19);
    }

}