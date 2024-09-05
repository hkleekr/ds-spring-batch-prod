package com.daesang.springbatch.sap.exportloan.job;

import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.enumerate.UrlEnum;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.sap.exportloan.domain.SapExportLoanDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * fileName         : SapExportLoanJobWriter
 * author           : 김수진과장
 * date             : 2022-11-09
 * descrition       : 수출 Account 여신 itemWriter
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-09       김수진과장             최초생성
 * 2022-11-10       최종민차장             서비스 수정
 */

@Slf4j
@RequiredArgsConstructor
public class SapExportLoanJobWriter implements ItemWriter<SapExportLoanDto> {

    private final WebClientService webClientService;
    private final String REQUEST_HOST;
    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public void write(List<? extends SapExportLoanDto> items) throws Exception {
        JSONObject jsonObject = new JSONObject();

        Object anIf = jobExecution.getExecutionContext().get("IF");
        InterfaceInfo interfaceInfo = (InterfaceInfo) anIf;

        jobExecution.getStepExecutions().stream().forEach(stepExecution -> jsonObject.put("currentPage", stepExecution.getCommitCount() + 1));
        jsonObject.put("totalCount", interfaceInfo.getTotalCount());
        jsonObject.put("totalPage", interfaceInfo.getTotalPage());
        jsonObject.put("request", items);

        webClientService.getGatewayResponse(jsonObject, REQUEST_HOST, UrlEnum.SAP_113);
    }
}