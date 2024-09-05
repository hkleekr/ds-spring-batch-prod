package com.daesang.springbatch.sap.bondoverdue.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.util.CommonUtils;
import com.daesang.springbatch.sap.bondoverdue.domain.OverdueDto;
import com.sap.conn.jco.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Instanceof;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * fileName			: BondOverdueJobReader
 * author			: 최종민차장
 * date				: 2022-11-07
 * descrition       : 채권 연체 현황 ItemReader
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2022-11-07			최종민차장             최초생성
 */

@Slf4j
@RequiredArgsConstructor
public class BondOverdueJobReader implements ItemReader<OverdueDto> {

    private List<OverdueDto> overdueDtoList;
    private JobExecution jobExecution;
    private int nextIndex;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    private boolean bondOverdueDataIsNotInitialized(){
        return this.overdueDtoList == null;
    }

    @Override
    public OverdueDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (bondOverdueDataIsNotInitialized()) {
            overdueDtoList = fetchBondOverdueDataFromSAP();
        }

        OverdueDto overdueDto = null;

        if (nextIndex < overdueDtoList.size()) {
            overdueDto = overdueDtoList.get(nextIndex);
            nextIndex++;
        }
        else{
            nextIndex = 0;
            overdueDtoList = null;
        }

        return overdueDto;
    }

    /**
     * Get SAP data (rfc function : ZFI_GET_ZFITAR41)
     * @return
     */
    private List<OverdueDto> fetchBondOverdueDataFromSAP() throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination(Constant.ABAP_AS_POOLED);

        //STFC_CONNECTION function call
        JCoFunction function = destination.getRepository().getFunction("ZFI_GET_ZFITAR41");

        /**
         * Input param setting
         * I_BUKRS		CHAR	4	회사코드 ( EX : 1000 )
         */
        function.getImportParameterList().setValue("I_BUKRS", "1000");

        //function not found Exception
        if (function == null)
            throw new RuntimeException("ZFI_GET_ZFITAR41 function not found in SAP.");

        function.execute(destination);

        //message 받는 Exception 필요할 듯 함
        if (!"0".equals(function.getExportParameterList().getString("E_SUBRC")))
            throw new RuntimeException(function.getExportParameterList().getString("E_MESS"));

        //sap table list 조회
        JCoTable t_out = function.getTableParameterList().getTable("OT_ZFISAR41");

        List<OverdueDto> overdueDtoList = new ArrayList<>();
        for(int i=0; i<t_out.getNumRows(); i++) {
            t_out.setRow(i);
            OverdueDto overdueDto = new OverdueDto();

            //mdmCode c.... 넘기지 말것 김병하 부장님 요청
            if ( CommonUtils.isNumeric(t_out.getField("KUNNR").getString()) ) {
                overdueDto.setMdmCode(String.valueOf(t_out.getField("KUNNR").getInt()));
            } else {
                continue;
            }
            //overdueDto.setMdmCode(String.valueOf(Integer.parseInt(t_out.getField("KUNNR").getString())));
            overdueDto.setMdmName(t_out.getField("NAME1").getString());
            overdueDto.setSalesEmployee(String.valueOf(Integer.parseInt(t_out.getField("PERNR").getString())));
            overdueDto.setSalesEmployeeName(t_out.getField("SNAME").getString());
            overdueDto.setPaymentTerms(t_out.getField("VTEXT").getString());
            overdueDto.setPreviousMonthBalance(t_out.getField("PAMT").getString());
            overdueDto.setAccountsReceivable(t_out.getField("AAMT").getString());
            overdueDto.setTotalBondBalance(t_out.getField("QAMT").getString());
            overdueDto.setAfter7DaysBonds(t_out.getField("F_07M").getString());
            //overdueDto.setBefore7DaysBonds(t_out.getField("F_01").getString()); -7일 이내만 사용 김병하 부장님 요청
            overdueDto.setWithin15DaysBonds(t_out.getField("F_08").getString());
            overdueDto.setWithin30DaysBonds(t_out.getField("F_16").getString());
            overdueDto.setOver31DaysBonds(t_out.getField("F_31").getString());
            overdueDto.setBranchTeam(t_out.getField("VKGRPT").getString());

            overdueDtoList.add(overdueDto);
        }

        log.info("Overdue - {} 건", overdueDtoList.size());

        //Salesforce로 data 전송 시 약속 된 Param 정보 set
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        if (overdueDtoList.size() % Constant.CHUNK_SIZE_L == 0){
            interfaceInfo.setTotalPage(overdueDtoList.size()/Constant.CHUNK_SIZE_L);
        }else {
            interfaceInfo.setTotalPage(overdueDtoList.size()/Constant.CHUNK_SIZE_L + 1);
        }
        interfaceInfo.setChunkSize(Constant.CHUNK_SIZE_L);
        interfaceInfo.setTotalCount(overdueDtoList.size());

        //jobExecution에 해당 정보를 set 하여 ItemWriter에서 해당 정보를 꺼내기 위한 처리
        jobExecution.getExecutionContext().put("IF", interfaceInfo);

        return overdueDtoList;
    }

}
