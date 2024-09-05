package com.daesang.springbatch.sap.bondoverdue.job;

import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.enumerate.UrlEnum;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.sap.bondoverdue.domain.OverdueDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;

import java.util.List;


/**
 * fileName			: BondOverdueJobWriter
 * author			: 최종민차장
 * date				: 2022-11-07
 * descrition       : 채권 연체 현황 ItemWriter
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-11-07			최종민차장             최초생성
 */
@Slf4j
@RequiredArgsConstructor
public class BondOverdueJobWriter implements ItemWriter<OverdueDto> {

    private final WebClientService webClientService;
    private final String REQUEST_HOST;
    private JobExecution jobExecution;

    /**
     * Batch Step에서 사용되는 Parameter 정보를 꺼내오기 위함
     * @param stepExecution
     */
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public void write(List<? extends OverdueDto> items) throws Exception {
        JSONObject jsonObject = new JSONObject();

        //ItemReader를 통해 Setting된 파라미터 정보를 Get
        Object anIf = jobExecution.getExecutionContext().get("IF");
        InterfaceInfo interfaceInfo = (InterfaceInfo) anIf;

        //Chunk단위로 전달 몇 번째 실행 되었는지 확인 가능
        jobExecution.getStepExecutions().stream().forEach(stepExecution -> jsonObject.put("currentPage", stepExecution.getCommitCount() + 1));
        jsonObject.put("totalCount", interfaceInfo.getTotalCount());
        jsonObject.put("totalPage", interfaceInfo.getTotalPage());
        jsonObject.put("request", items);

        //json object로 닮겨진 정보를 RestTemplate을 통해 endpointurl로 전송 (Batch의 Target은 Salesforce)
        webClientService.getGatewayResponse(jsonObject, REQUEST_HOST, UrlEnum.SAP_78);
    }
}
