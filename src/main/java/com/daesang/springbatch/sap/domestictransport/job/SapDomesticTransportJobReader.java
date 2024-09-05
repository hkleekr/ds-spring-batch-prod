package com.daesang.springbatch.sap.domestictransport.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.util.CommonUtils;
import com.daesang.springbatch.sap.domestictransport.domain.SapDomesticTransportDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.conn.jco.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * fileName         : SapDomesticTransportJobReader
 * author           : inayoon
 * date             : 2022-11-17
 * description      : 배송관리 - 내수 납품정보(LE)
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-17       inayoon             최초생성
 */
@Slf4j
@RequiredArgsConstructor
public class SapDomesticTransportJobReader implements ItemReader<SapDomesticTransportDto> {

    private final ModelMapper modelMapper;
    private List<SapDomesticTransportDto> sapDomesticTransportList;
    private int nextIndex;
    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public SapDomesticTransportDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (sapDomesticTransportDataIsNotInitialized()){
            sapDomesticTransportList = fetchSapDomesticTransportDataFromAPI();
        }

        SapDomesticTransportDto mappingDto = null;

        if (nextIndex < sapDomesticTransportList.size()) {
            mappingDto = sapDomesticTransportList.get(nextIndex);

            nextIndex++;

        }else {
            nextIndex = 0;
            sapDomesticTransportList = null;
        }

        return mappingDto;
    }

    private boolean sapDomesticTransportDataIsNotInitialized(){
        return this.sapDomesticTransportList == null;
    }

    /**
     * Get SAP data (rfc function : Z_LE_CRM_TKNUM)
     * @return
     */
    private List<SapDomesticTransportDto> fetchSapDomesticTransportDataFromAPI() throws JsonProcessingException, org.json.simple.parser.ParseException, JCoException {
        List<SapDomesticTransportDto> sapDomesticTransports = new ArrayList<SapDomesticTransportDto>();

        JCoDestination destination = JCoDestinationManager.getDestination("ABAP_AS_WITH_POOL");
        // STFC_CONNECTION function call
        JCoFunction function = destination.getRepository().getFunction("Z_LE_CRM_TKNUM");

        /**
         * Input param setting
         * FROMDATE		String	8	시작일 ( EX : 20221115 )
         * TODATE		String	8	종료일 ( EX : 20221116 )
         */
        Map<String, JobParameter> parameters = jobExecution.getJobParameters().getParameters();
        String fromDate = String.valueOf(parameters.get("fromDate"));
        String toDate = String.valueOf(parameters.get("toDate"));

        function.getImportParameterList().setValue("FROMDATE", fromDate);
        function.getImportParameterList().setValue("TODATE", toDate);

        // function not found Exception
        if (function == null)
            throw new RuntimeException("Z_LE_CRM_TKNUM not found in SAP.");

        function.execute(destination);

        JCoTable t_out = function.getTableParameterList().getTable("T_OUT");

        for(int i=0; i<t_out.getNumRows(); i++) {
            t_out.setRow(i);
            HashMap<String,Object> map = new HashMap<String,Object>();

            for(int j=0; j<t_out.getNumColumns(); j++) {
                String columnName = t_out.getMetaData().getName(j);
                map.put(columnName, t_out.getString(columnName));
            }

            if(map.size() > 0) {
                ObjectMapper mapper = new ObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                SapDomesticTransportDto sapDomesticTransport = mapper.convertValue(map, SapDomesticTransportDto.class);
                sapDomesticTransports.add(sapDomesticTransport);
            }
        }

        if (sapDomesticTransports.size() > 0) {
            sapDomesticTransports = sapDomesticTransports.stream()
                    .filter(CommonUtils.distinctByKey(m -> m.getDoDoc()))
                    .map(sapDomesticTransport-> modelMapper.map(sapDomesticTransport, SapDomesticTransportDto.class))
                    .collect(Collectors.toList());

            log.info("DomesticTransport - {} 건", sapDomesticTransports.size());

            InterfaceInfo interfaceInfo = new InterfaceInfo();
            if (sapDomesticTransports.size() % Constant.CHUNK_SIZE_L == 0) {
                interfaceInfo.setTotalPage(sapDomesticTransports.size()/ Constant.CHUNK_SIZE_L);
            }else {
                interfaceInfo.setTotalPage(sapDomesticTransports.size()/ Constant.CHUNK_SIZE_L + 1);
            }
            interfaceInfo.setChunkSize(Constant.CHUNK_SIZE_L);
            interfaceInfo.setTotalCount(sapDomesticTransports.size());

            jobExecution.getExecutionContext().put("IF", interfaceInfo);

        } else {
            log.info("DomesticTransport - 업데이트된 데이터가 없습니다.");
        }

        return sapDomesticTransports;
    }
}