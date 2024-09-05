package com.daesang.springbatch.sap.exportloan.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.sap.exportloan.domain.SapExportLoanDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sap.conn.jco.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.List;

/**
 * fileName         : SapExportLoanJobReader
 * author           : 김수진과장
 * date             : 2022-11-09
 * descrition       : 수출 Account 여신 itemReader
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-09       김수진과장             최초생성
 * 2022-11-10       최종민차장             서비스 수정
 */

@Slf4j
@RequiredArgsConstructor
public class SapExportLoanJobReader implements ItemReader<SapExportLoanDto> {
    private List<SapExportLoanDto> sapExportLoanList;
    private JobExecution jobExecution;
    private int nextIndex;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    private boolean sapExportLoanDataIsNotInitialized(){
        return this.sapExportLoanList == null;
    }
    @Override
    public SapExportLoanDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if (sapExportLoanDataIsNotInitialized()) {
            sapExportLoanList = fetchExportLoanDataFromSAP();
        }

        SapExportLoanDto sapExportLoanDto = null;

        if (nextIndex < sapExportLoanList.size()) {
            sapExportLoanDto = sapExportLoanList.get(nextIndex);
            nextIndex++;

        }  else {
            nextIndex = 0;
            sapExportLoanList = null;
        }

        return sapExportLoanDto;
    }

    private List<SapExportLoanDto> fetchExportLoanDataFromSAP() throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination(Constant.ABAP_AS_POOLED);

        //STFC_CONNECTION function call
        JCoFunction function = destination.getRepository().getFunction("ZEX_CRMSI_CREDIT");

        if (function == null)
            throw new RuntimeException("ZEX_CRMSI_CREDIT not found in SAP.");

        function.execute(destination);

        JCoTable t_out = function.getTableParameterList().getTable("T_OUT");

        List<SapExportLoanDto> sapExportLoanDtoList = new ArrayList<>();
        for(int i=0; i<t_out.getNumRows(); i++) {
            t_out.setRow(i);
            SapExportLoanDto sapExportLoanDto = new SapExportLoanDto();
            sapExportLoanDto.setMdmCode(t_out.getField("KUNNR").getString());
            sapExportLoanDto.setMdmName(t_out.getField("NAME1").getString());
            sapExportLoanDto.setBasicCreditAmountForeign(t_out.getField("CRTOT2").getString());
            sapExportLoanDto.setStatement(t_out.getField("WAERK").getString());
            sapExportLoanDto.setCurrency(t_out.getField("KURSK").getString());
            sapExportLoanDto.setBasicCreditAmountWon(t_out.getField("CRTOT").getString());
            sapExportLoanDto.setTotalOrderAmountWon(t_out.getField("ORDAT").getString());
            sapExportLoanDto.setTotalBillingAmountWon(t_out.getField("BILAT").getString());
            sapExportLoanDto.setTotalDebentureAmountWon(t_out.getField("OPNAT").getString());
            sapExportLoanDto.setTotalPendingOrderAmountWon(t_out.getField("ORNAT").getString());
            sapExportLoanDto.setMarginMaxAmountWon(t_out.getField("REAMT").getString());
            sapExportLoanDto.setMarginLimitAmountForeign(t_out.getField("REAMT2").getString());
            sapExportLoanDto.setLoanManagement(t_out.getField("KKBER").getString());
            sapExportLoanDtoList.add(sapExportLoanDto);
        }

        log.info("SapExportLoan - {} 건", sapExportLoanDtoList.size());

        InterfaceInfo interfaceInfo = new InterfaceInfo();
        if (sapExportLoanDtoList.size() % Constant.CHUNK_SIZE_L == 0){
            interfaceInfo.setTotalPage(sapExportLoanDtoList.size()/Constant.CHUNK_SIZE_L);
        }else {
            interfaceInfo.setTotalPage(sapExportLoanDtoList.size()/Constant.CHUNK_SIZE_L + 1);
        }
        interfaceInfo.setChunkSize(Constant.CHUNK_SIZE_L);
        interfaceInfo.setTotalCount(sapExportLoanDtoList.size());

        jobExecution.getExecutionContext().put("IF", interfaceInfo);

        return sapExportLoanDtoList;
    }
}
