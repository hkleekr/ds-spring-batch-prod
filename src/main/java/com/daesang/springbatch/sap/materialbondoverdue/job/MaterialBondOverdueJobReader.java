package com.daesang.springbatch.sap.materialbondoverdue.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.util.CommonUtils;
import com.daesang.springbatch.sap.materialbondoverdue.domain.MaterialOverdueDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * fileName			: MaterialBondOverdueJobReader
 * author			: 최종민차장
 * date				: 2023-02-01
 * descrition       : (소재) Account 내수 연체 정보 조회 ItemReader
 * =======================================================
 * DATE					AUTHOR					NOTE
 * -------------------------------------------------------
 * 2023-02-01			최종민차장             최초생성
 */
@Slf4j
@RequiredArgsConstructor
public class MaterialBondOverdueJobReader implements ItemReader<MaterialOverdueDto> {

    private List<MaterialOverdueDto> materialOverdueDtoList;
    private JobExecution jobExecution;
    private int nextIndex;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    private boolean materialBondOverdueDataIsNotInitialized(){
        return this.materialOverdueDtoList == null;
    }

    @Override
    public MaterialOverdueDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (materialBondOverdueDataIsNotInitialized()) {
            materialOverdueDtoList = fetchMaterialBondOverdueDataFromSAP();
        }

        MaterialOverdueDto materialOverdueDto = null;

        if (nextIndex < materialOverdueDtoList.size()) {
            materialOverdueDto = materialOverdueDtoList.get(nextIndex);
            nextIndex++;
        }
        else{
            nextIndex = 0;
            materialOverdueDtoList = null;
        }

        return materialOverdueDto;
    }

    /**
     * Get SAP data (rfc function : ZFI_GET_ZFITAR41)
     * @return
     */
    private List<MaterialOverdueDto> fetchMaterialBondOverdueDataFromSAP() throws JCoException {
        List<MaterialOverdueDto> materialOverdueDtoList = new ArrayList<>();

        List<MaterialOverdueDto> data1 = getData("1");
        materialOverdueDtoList.addAll(data1);

        List<MaterialOverdueDto> data2 = getData("2");
        materialOverdueDtoList.addAll(data2);

        log.info("MaterialOverdue - {} 건", materialOverdueDtoList.size());

        InterfaceInfo interfaceInfo = new InterfaceInfo();
        if (materialOverdueDtoList.size() % Constant.CHUNK_SIZE_L == 0){
            interfaceInfo.setTotalPage(materialOverdueDtoList.size()/Constant.CHUNK_SIZE_L);
        }else {
            interfaceInfo.setTotalPage(materialOverdueDtoList.size()/Constant.CHUNK_SIZE_L + 1);
        }
        interfaceInfo.setChunkSize(Constant.CHUNK_SIZE_L);
        interfaceInfo.setTotalCount(materialOverdueDtoList.size());

        jobExecution.getExecutionContext().put("IF", interfaceInfo);

        return materialOverdueDtoList;
    }

    private List<MaterialOverdueDto> getData(String type) throws JCoException {
        JCoDestination destination = JCoDestinationManager.getDestination(Constant.ABAP_AS_POOLED);

        //STFC_CONNECTION function call
        JCoFunction function = destination.getRepository().getFunction("ZFI_GET_ZFIRAR02_IF");

        //function not found Exception
        if (function == null)
            throw new RuntimeException("ZFI_GET_ZFITAR41 function not found in SAP.");
        /**
         * Input param setting
         * I_BUKRS		CHAR	4	회사코드 ( EX : 1000 )
         * I_SPMON		NUMC	6	조회기간 ( EX : 202207 )
         * I_BZIRK		CHAR	6	사업총괄 ( EX : 800000 )
         * I_CHKDT		CHAR	1	일자타입 - 1-전기일,2-증빙일
         */
        /*************************************************************************************
         *
         ************************************************************************************/
        Map<String, JobParameter> parameters = jobExecution.getJobParameters().getParameters();
        String month = String.valueOf(parameters.get("month"));
        function.getImportParameterList().setValue("I_BUKRS", "1000");
        function.getImportParameterList().setValue("I_SPMON", month);
        function.getImportParameterList().setValue("I_BZIRK", "800000");
        function.getImportParameterList().setValue("I_CHKDT", type);

        function.execute(destination);

        //message 받는 Exception 필요할 듯 함
        if (!"0".equals(function.getExportParameterList().getString("E_SUBRC")))
            throw new RuntimeException(function.getExportParameterList().getString("E_MESS"));

        //sap table list 조회
        JCoTable t_out = function.getTableParameterList().getTable("T_ZFISRAR02");

        List<MaterialOverdueDto> materialOverdueDtoList = new ArrayList<>();
        for(int i=0; i<t_out.getNumRows(); i++) {
            t_out.setRow(i);
            MaterialOverdueDto materialOverdueDto = new MaterialOverdueDto();

            materialOverdueDto.setGeneralBusiness(t_out.getField("BZIRK").getString());              //사업총괄
            materialOverdueDto.setGeneralBusinessDetails(t_out.getField("BZTXT").getString());       //사업총괄 이름
            materialOverdueDto.setMdmCode(String.valueOf(t_out.getField("KUNNR").getInt()));         //고객 번호
            materialOverdueDto.setAccountName(t_out.getField("NAME1").getString());                  //고객이름
            materialOverdueDto.setBusinessCode(t_out.getField("VKBUR").getString());                 //사업부
            materialOverdueDto.setBusinessDetails(t_out.getField("BEZEI_VB").getString());           //사업부이름
            materialOverdueDto.setTeamCode(t_out.getField("VKGRP").getString());                     //지점/팀
            materialOverdueDto.setTeamDetails(t_out.getField("BEZEI_VG").getString());               //지점/팀 이름
            materialOverdueDto.setPaymentConditionCode(t_out.getField("ZTERM").getString());         //지급 조건 키
            materialOverdueDto.setPaymentConditionDetails(t_out.getField("VTEXT").getString());      //지급조건내역
            materialOverdueDto.setAccountReceivable3Month(t_out.getField("SALES1_4").getString());   //외상매출금-조회월(-3개월)
            materialOverdueDto.setAccountReceivable2Month(t_out.getField("SALES1_3").getString());   //외상매출금-조회월(-2개월)
            materialOverdueDto.setAccountReceivable1Month(t_out.getField("SALES1_2").getString());   //외상매출금-조회월(-1개월)
            materialOverdueDto.setAccountReceivable(t_out.getField("SALES1_1").getString());         //외상매출금-조회월
            materialOverdueDto.setBondBalance(t_out.getField("HSL01").getString());                  //채권잔액
            materialOverdueDto.setOverdueAmount(t_out.getField("DMBTR").getString());                //연체금액
            materialOverdueDto.setOverdueAmount5Days(t_out.getField("DMBTR_05").getString());        //연체금액(D+5)
            materialOverdueDto.setOverdueAmountToday(t_out.getField("DMBTR_2").getString());         //연체금액(현재일자)
            materialOverdueDto.setType(type);

            materialOverdueDtoList.add(materialOverdueDto);
        }
        return materialOverdueDtoList;
    }
}
