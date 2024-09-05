package com.daesang.springbatch.mdm.accountpartner.job;

import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.enumerate.UrlEnum;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.mdm.accountpartner.domain.MdmAccountPartnerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * fileName         : MdmAccountPartnerJobWriter
 * author           : 김수진과장
 * date             : 2022-11-10
 * descrition       : 고객정보 - Account 파트너 기능 배치
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-10       김수진과장             최초생성
 */
@Slf4j
@RequiredArgsConstructor
public class MdmAccountPartnerJobWriter implements ItemWriter<MdmAccountPartnerDto> {

    private final WebClientService webClientService;

    private final String REQUEST_HOST;

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public void write(List<? extends MdmAccountPartnerDto> items) throws Exception {
        JSONObject jsonObject = new JSONObject();

        Object anIf = jobExecution.getExecutionContext().get("IF");
        InterfaceInfo interfaceInfo = (InterfaceInfo) anIf;
        jobExecution.getStepExecutions().stream().forEach(stepExecution -> jsonObject.put("currentPage", stepExecution.getCommitCount() + 1));

        jsonObject.put("totalCount", interfaceInfo.getTotalCount());
        jsonObject.put("totalPage", interfaceInfo.getTotalPage());
        jsonObject.put("request", items);

        webClientService.getGatewayResponse(jsonObject, REQUEST_HOST, UrlEnum.MDM_116);
    }
}
