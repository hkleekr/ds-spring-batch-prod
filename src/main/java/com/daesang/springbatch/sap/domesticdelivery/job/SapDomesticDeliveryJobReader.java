package com.daesang.springbatch.sap.domesticdelivery.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.util.CommonUtils;
import com.daesang.springbatch.sap.domesticdelivery.domain.SapDomesticDeliveryDto;
import com.daesang.springbatch.sap.domesticdelivery.domain.SapDomesticDeliveryItemDto;
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
 * fileName         : SapDomesticDeliveryJobReader
 * author           : inayoon
 * date             : 2022-11-07
 * description      : 배송관리 - 내수 배송정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-07       inayoon             최초생성
 * 2022-11-22       inayoon             CHUNK_SIZE_M 변경
 */
@Slf4j
@RequiredArgsConstructor
public class SapDomesticDeliveryJobReader implements ItemReader<SapDomesticDeliveryDto> {

    private final ModelMapper modelMapper;

    private List<SapDomesticDeliveryDto> sapDomesticDeliveryList;

    private int nextIndex;

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public SapDomesticDeliveryDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (sapDomesticDeliveryDataIsNotInitialized()){
            sapDomesticDeliveryList = fetchSapDomesticDeliveryDataFromAPI();
        }

        SapDomesticDeliveryDto mappingDto = null;

        if (nextIndex < sapDomesticDeliveryList.size()) {
            mappingDto = sapDomesticDeliveryList.get(nextIndex);

            nextIndex++;

        }else {
            nextIndex = 0;
            sapDomesticDeliveryList = null;
        }

        return mappingDto;
    }

    private boolean sapDomesticDeliveryDataIsNotInitialized(){
        return this.sapDomesticDeliveryList == null;
    }

    /**
     * Get SAP data (rfc function : ZSD_CRMSI_DN)
     * @return
     */
    private List<SapDomesticDeliveryDto> fetchSapDomesticDeliveryDataFromAPI() throws JsonProcessingException, org.json.simple.parser.ParseException, JCoException {
        List<SapDomesticDeliveryDto> sapDomesticDeliverys = new ArrayList<SapDomesticDeliveryDto>();
        List<SapDomesticDeliveryItemDto> sapDomesticDeliveryItems = new ArrayList<SapDomesticDeliveryItemDto>();

        JCoDestination destination = JCoDestinationManager.getDestination("ABAP_AS_WITH_POOL");
        // STFC_CONNECTION function call
        JCoFunction function = destination.getRepository().getFunction("ZSD_CRMSI_DN");

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
            throw new RuntimeException("ZSD_CRMSI_DN not found in SAP.");

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

                SapDomesticDeliveryDto sapDomesticDelivery = mapper.convertValue(map, SapDomesticDeliveryDto.class);
                sapDomesticDeliverys.add(sapDomesticDelivery);

                SapDomesticDeliveryItemDto sapDomesticDeliveryItem = mapper.convertValue(map, SapDomesticDeliveryItemDto.class);
                sapDomesticDeliveryItems.add(sapDomesticDeliveryItem);
            }
        }

        if (sapDomesticDeliverys.size() > 0) {
            sapDomesticDeliverys = sapDomesticDeliverys.stream()
                    .filter(CommonUtils.distinctByKey(m -> m.getDoDoc()))
                    .map(sapDomesticDelivery -> modelMapper.map(sapDomesticDelivery, SapDomesticDeliveryDto.class))
                    .collect(Collectors.toList());

            for (SapDomesticDeliveryDto sddDto : sapDomesticDeliverys) {
                for(SapDomesticDeliveryItemDto sddiDto : sapDomesticDeliveryItems) {
                    if(sddDto.getDoDoc().equals(sddiDto.getDoDoc())) {
                        sddDto.getDeliveryItem().add(sddiDto);
                    }
                }
            }

            log.info("DomesticDelivery - {} 건", sapDomesticDeliverys.size());
            log.info("DomesticDeliveryItem - {} 건", sapDomesticDeliveryItems.size());

            InterfaceInfo interfaceInfo = new InterfaceInfo();
            if (sapDomesticDeliverys.size() % Constant.CHUNK_SIZE_M == 0) {
                interfaceInfo.setTotalPage(sapDomesticDeliverys.size()/ Constant.CHUNK_SIZE_M);
            }else {
                interfaceInfo.setTotalPage(sapDomesticDeliverys.size()/ Constant.CHUNK_SIZE_M + 1);
            }
            interfaceInfo.setChunkSize(Constant.CHUNK_SIZE_M);
            interfaceInfo.setTotalCount(sapDomesticDeliverys.size());

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("IF", interfaceInfo);
            jobExecution.getExecutionContext().put("IF", interfaceInfo);

        } else {
            log.info("DomesticDelivery - 업데이트된 데이터가 없습니다.");
        }

        return sapDomesticDeliverys;
    }
}