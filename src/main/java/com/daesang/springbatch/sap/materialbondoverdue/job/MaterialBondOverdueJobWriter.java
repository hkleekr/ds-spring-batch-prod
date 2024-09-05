package com.daesang.springbatch.sap.materialbondoverdue.job;

import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.enumerate.UrlEnum;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.sap.materialbondoverdue.domain.MaterialOverdueDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * fileName			: MaterialBondOverdueJobWriter
 * author			: 최종민차장
 * date				: 2023-02-01
 * descrition       : (소재) Account 내수 연체 정보 조회 ItemWriter
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2023-02-01			최종민차장             최초생성
 */
@Slf4j
@RequiredArgsConstructor
public class MaterialBondOverdueJobWriter implements ItemWriter<MaterialOverdueDto> {

    private final WebClientService webClientService;
    private final String REQUEST_HOST;
    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public void write(List<? extends MaterialOverdueDto> items) throws Exception {
        JSONObject jsonObject = new JSONObject();

        Object anIf = jobExecution.getExecutionContext().get("IF");
        InterfaceInfo interfaceInfo = (InterfaceInfo) anIf;

        jobExecution.getStepExecutions().stream().forEach(stepExecution -> jsonObject.put("currentPage", stepExecution.getCommitCount() + 1));
        jsonObject.put("totalCount", interfaceInfo.getTotalCount());
        jsonObject.put("totalPage", interfaceInfo.getTotalPage());
        jsonObject.put("request", items);

        webClientService.getGatewayResponse(jsonObject, REQUEST_HOST, UrlEnum.SAP_133);
    }

}
