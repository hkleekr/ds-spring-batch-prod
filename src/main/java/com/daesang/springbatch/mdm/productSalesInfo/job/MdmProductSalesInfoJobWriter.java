package com.daesang.springbatch.mdm.productSalesInfo.job;

import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.enumerate.UrlEnum;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.mdm.productSalesInfo.domain.MdmProductSalesInfoDto;
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
 * fileName         : MdmProductSalesInfoJobWriter
 * author           : 권용성사원
 * date             : 2023-01-17
 * description      : 제품정보 - 제품 판매조직 기준 정보 배치 WRITER
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-01-17       권용성사원             최초생성
 */
@Slf4j
@RequiredArgsConstructor
public class MdmProductSalesInfoJobWriter implements ItemWriter<MdmProductSalesInfoDto> {

    private final WebClientService webClientService;

    private final String REQUEST_HOST;

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public void write(List<? extends MdmProductSalesInfoDto> items) throws Exception {
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

        webClientService.getGatewayResponse(jsonObject, REQUEST_HOST, UrlEnum.MDM_134);
    }

}