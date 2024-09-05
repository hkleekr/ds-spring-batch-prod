package com.daesang.springbatch.dwrs.scemployeeaccount.job;

import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.enumerate.UrlEnum;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.dwrs.scemployeeaccount.domain.DwrsScEmployeeAccountMappingDto;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@RequiredArgsConstructor
public class DwrsScEmployeeAccountJobWriter implements ItemWriter<DwrsScEmployeeAccountMappingDto> {

    private final WebClientService webClientService;

    private final String REQUEST_HOST;

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public void write(List<? extends DwrsScEmployeeAccountMappingDto> items) throws Exception {
        JSONObject jsonObject = new JSONObject();

        Object anIf = jobExecution.getExecutionContext().get("IF");
        InterfaceInfo interfaceInfo = (InterfaceInfo) anIf;
        jobExecution.getStepExecutions().stream().forEach(stepExecution -> jsonObject.put("currentPage", stepExecution.getCommitCount() + 1));

        jsonObject.put("totalCount", interfaceInfo.getTotalCount());
        jsonObject.put("totalPage", interfaceInfo.getTotalPage());
        jsonObject.put("request", items);

        webClientService.getGatewayResponse(jsonObject, REQUEST_HOST, UrlEnum.DWRS_18);
    }
}
