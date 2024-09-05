package com.daesang.springbatch.sap.domesticorder.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.util.CommonUtils;
import com.daesang.springbatch.sap.domesticorder.domain.SapDomesticOrderDto;
import com.daesang.springbatch.sap.domesticorder.domain.SapDomesticOrderItemDto;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * fileName         : SapDomesticOrderJobReader
 * author           : inayoon
 * date             : 2022-11-04
 * description      : 주문관리 - 내수 주문정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-04       inayoon             최초생성
 * 2022-11-22       inayoon             CHUNK_SIZE_M 변경
 */
@Slf4j
@RequiredArgsConstructor
public class SapDomesticOrderJobReader implements ItemReader<SapDomesticOrderDto> {

    private final ModelMapper modelMapper;

    private List<SapDomesticOrderDto> sapDomesticOrderList;

    private int nextIndex;

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public SapDomesticOrderDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (sapDomesticOrderDataIsNotInitialized()){
            sapDomesticOrderList = fetchSapDomesticOrderDataFromAPI();
        }

        SapDomesticOrderDto mappingDto = null;

        if (nextIndex < sapDomesticOrderList.size()) {
            mappingDto = sapDomesticOrderList.get(nextIndex);

            nextIndex++;

        }else {
            nextIndex = 0;
            sapDomesticOrderList = null;
        }

        return mappingDto;
    }

    private boolean sapDomesticOrderDataIsNotInitialized(){
        return this.sapDomesticOrderList == null;
    }

    /**
     * Get SAP data (rfc function : ZSD_CRMSI_OR)
     * @return
     */
    private List<SapDomesticOrderDto> fetchSapDomesticOrderDataFromAPI() throws JsonProcessingException, org.json.simple.parser.ParseException, JCoException {
        List<SapDomesticOrderDto> sapDomesticOrders = new ArrayList<SapDomesticOrderDto>();
        List<SapDomesticOrderItemDto> sapDomesticOrderItems = new ArrayList<SapDomesticOrderItemDto>();

        JCoDestination destination = JCoDestinationManager.getDestination("ABAP_AS_WITH_POOL");
        // STFC_CONNECTION function call
        JCoFunction function = destination.getRepository().getFunction("ZSD_CRMSI_OR");

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
            throw new RuntimeException("ZSD_CRMSI_OR not found in SAP.");

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

                SapDomesticOrderDto sapDomesticOrder = mapper.convertValue(map, SapDomesticOrderDto.class);
                sapDomesticOrders.add(sapDomesticOrder);

                SapDomesticOrderItemDto sapDomesticOrderItem = mapper.convertValue(map, SapDomesticOrderItemDto.class);
                sapDomesticOrderItems.add(sapDomesticOrderItem);
            }
        }

        if (sapDomesticOrders.size() > 0) {
            sapDomesticOrders = sapDomesticOrders.stream()
                    .filter(CommonUtils.distinctByKey(m -> m.getOrderNumber()))
                    .map(sapDomesticOrder -> modelMapper.map(sapDomesticOrder, SapDomesticOrderDto.class))
                    .collect(Collectors.toList());

            for (SapDomesticOrderDto sdoDto : sapDomesticOrders) {
                for(SapDomesticOrderItemDto sdoiDto : sapDomesticOrderItems) {
                    if(sdoDto.getOrderNumber().equals(sdoiDto.getOrderNumber())) {
                        sdoDto.getOrderItem().add(sdoiDto);
                    }
                }
            }

            log.info("DomesticOrder - {} 건", sapDomesticOrders.size());
            log.info("DomesticOrderItem - {} 건", sapDomesticOrderItems.size());

            InterfaceInfo interfaceInfo = new InterfaceInfo();
            if (sapDomesticOrders.size() % Constant.CHUNK_SIZE_M == 0) {
                interfaceInfo.setTotalPage(sapDomesticOrders.size()/ Constant.CHUNK_SIZE_M);
            }else {
                interfaceInfo.setTotalPage(sapDomesticOrders.size()/ Constant.CHUNK_SIZE_M + 1);
            }
            interfaceInfo.setChunkSize(Constant.CHUNK_SIZE_M);
            interfaceInfo.setTotalCount(sapDomesticOrders.size());

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("IF", interfaceInfo);
            jobExecution.getExecutionContext().put("IF", interfaceInfo);

        } else {
            log.info("DomesticOrder - 업데이트된 데이터가 없습니다.");
        }

        return sapDomesticOrders;
    }
}