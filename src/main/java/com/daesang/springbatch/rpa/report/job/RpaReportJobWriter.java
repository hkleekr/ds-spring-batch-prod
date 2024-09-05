package com.daesang.springbatch.rpa.report.job;

import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.enumerate.UrlEnum;
import com.daesang.springbatch.common.service.WebClientService;
import com.daesang.springbatch.common.util.CommonUtils;
import com.daesang.springbatch.rpa.report.domain.RpaReportDto;
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

import java.io.File;
import java.util.List;

/**
 * fileName         : RpaReportJobWriter
 * author           : inayoon
 * date             : 2023-01-02
 * description      : 내수 주문 관련 시험성적서 파일 전송
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-01-02       inayoon             최초생성
 */
@Slf4j
@RequiredArgsConstructor
public class RpaReportJobWriter implements ItemWriter<RpaReportDto> {

    private JSONObject sfResponse;

    private final WebClientService webClientService;

    private final String REQUEST_HOST;

    private final String RPA_ABSOLUTE_PATH;

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public void write(List<? extends RpaReportDto> items) throws Exception {
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

        sfResponse = webClientService.getGatewayResponse2(jsonObject, REQUEST_HOST, UrlEnum.SAP_97);

        if (sfResponse != null) {
            if (jsonObject.get("currentPage").equals(jsonObject.get("totalPage"))) {
                String rpaAbsolutePath = RPA_ABSOLUTE_PATH+CommonUtils.getToday()+"/";
                deleteRpaFolder(rpaAbsolutePath);
            }
        }
    }


    /**
     * Delete RPA Data
     * @param filePath
     * @return
     */
    public void deleteRpaFolder(String filePath) {
        File rpaFile = new File(filePath);
        try {
            if(!rpaFile.exists()) {
                log.info("RPA Report - 파일이 존재하지 않습니다.");
                return;
            }

            if(rpaFile.isFile()) {
                rpaFile.delete();
            }else {
                File[] fileList = rpaFile.listFiles();
                for(File file : fileList){
                    file.delete();
                }
                log.info("RPA Report - 파일이 삭제되었습니다.");

                if(rpaFile.isDirectory()) {
                    rpaFile.delete();
                    log.info("RPA Report - 폴더가 삭제되었습니다.");
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

}