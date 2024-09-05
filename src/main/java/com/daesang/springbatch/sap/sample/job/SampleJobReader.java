package com.daesang.springbatch.sap.sample.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.sap.sample.domain.SampleDto;
import com.sap.conn.jco.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * fileName			: SampleJobReader
 * author			: 최종민차장
 * date				: 2022-11-09
 * descrition       : 무상 주문 정보 Itemreader
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-11-09			최종민차장             최초생성
 */

@Slf4j
@RequiredArgsConstructor
public class SampleJobReader implements ItemReader<SampleDto> {

    private List<SampleDto> sampleDtoList;
    private JobExecution jobExecution;
    private int nextIndex;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    private boolean sampleDataIsNotInitialized(){
        return this.sampleDtoList == null;
    }

    @Override
    public SampleDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (sampleDataIsNotInitialized()) {
            sampleDtoList = fetchSampleDataFromSAP();
        }

        SampleDto sampleDto = null;

        if (nextIndex < sampleDtoList.size()) {
            sampleDto = sampleDtoList.get(nextIndex);
            nextIndex++;
        }
        else{
            nextIndex = 0;
            sampleDtoList = null;
        }

        return sampleDto;
    }

    /**
     * Get SAP data (rfc function : ZFI_GET_ZFITAR41)
     * @return
     */
    private List<SampleDto> fetchSampleDataFromSAP() throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination(Constant.ABAP_AS_POOLED);

        //STFC_CONNECTION function call
        JCoFunction function = destination.getRepository().getFunction("ZSD_CRMSI_ZF01");

        Map<String, JobParameter> parameters = jobExecution.getJobParameters().getParameters();
        String fromDate = String.valueOf(parameters.get("fromDate"));
        String toDate = String.valueOf(parameters.get("toDate"));

        /**
         * Input param setting
         * FROMDATE     CHAR    8   조회시작일시
         * TODATE       CHAR    8   조회종료일시
         */
        function.getImportParameterList().setValue("FROMDATE", fromDate);
        function.getImportParameterList().setValue("TODATE", toDate);

        //function not found Exception
        if (function == null)
            throw new RuntimeException("ZFI_GET_ZFITAR41 function not found in SAP.");

        function.execute(destination);

        //message 받는 Exception 필요할 듯 함
//        if (!"Y".equals(function.getExportParameterList().getString("RETCODE")))
//            throw new RuntimeException(function.getExportParameterList().getString("E_MESS"));

        //sap table list 조회
        JCoTable t_out = function.getTableParameterList().getTable("T_OUT");

        List<SampleDto> sampleDtoList = new ArrayList<>();
        for(int i=0; i<t_out.getNumRows(); i++) {
            t_out.setRow(i);
            SampleDto sampleDto = new SampleDto();
            sampleDto.setException(t_out.getField("STATUS").getString());
            sampleDto.setOrderNumber(t_out.getField("VBELN").getString());
            sampleDto.setItemNumber(t_out.getField("POSNR").getString());
            sampleDto.setProductCode(t_out.getField("MATNR").getString());
            sampleDto.setProductName(t_out.getField("MAKTX").getString());
            sampleDto.setOrderQuantity(t_out.getField("KWMENG").getString());
            sampleDto.setOrderQuantityUnit(t_out.getField("VRKME").getString());
            sampleDto.setOrderStartDate(t_out.getField("AUDAT").getString());
            sampleDto.setSalesAccountCode(t_out.getField("KUNNR").getString());
            sampleDto.setDeliveryAccountCode(t_out.getField("KUNWE").getString());
            sampleDto.setDeliveryRequestDate(t_out.getField("VDATU").getString());
            sampleDto.setEmployeeNumber(t_out.getField("PERNR").getString());
            sampleDto.setEmployeeName(t_out.getField("ENAME").getString());
            sampleDtoList.add(sampleDto);
        }

        log.info("Sample - {} 건", sampleDtoList.size());

        InterfaceInfo interfaceInfo = new InterfaceInfo();
        if (sampleDtoList.size() % Constant.CHUNK_SIZE_L == 0){
            interfaceInfo.setTotalPage(sampleDtoList.size()/Constant.CHUNK_SIZE_L);
        }else {
            interfaceInfo.setTotalPage(sampleDtoList.size()/Constant.CHUNK_SIZE_L + 1);
        }
        interfaceInfo.setChunkSize(Constant.CHUNK_SIZE_L);
        interfaceInfo.setTotalCount(sampleDtoList.size());

        jobExecution.getExecutionContext().put("IF", interfaceInfo);

        return sampleDtoList;
    }
}
