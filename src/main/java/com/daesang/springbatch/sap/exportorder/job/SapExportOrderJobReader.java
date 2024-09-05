package com.daesang.springbatch.sap.exportorder.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.util.CommonUtils;
import com.daesang.springbatch.sap.exportorder.domain.SapExportOrderDto;
import com.daesang.springbatch.sap.exportorder.domain.SapExportOrderItemDto;
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
 * fileName         : SapExportOrderJobReader
 * author           : inayoon
 * date             : 2022-11-08
 * description      : 주문관리 - 수출 주문정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-08       inayoon             최초생성
 * 2022-11-22       inayoon             CHUNK_SIZE_S 변경
 */
@Slf4j
@RequiredArgsConstructor
public class SapExportOrderJobReader implements ItemReader<SapExportOrderDto> {

    private final ModelMapper modelMapper;

    private List<SapExportOrderDto> sapExportOrderList;

    private int nextIndex;

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public SapExportOrderDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (sapExportOrderDataIsNotInitialized()){
            sapExportOrderList = fetchSapExportOrderDataFromAPI();
        }

        SapExportOrderDto mappingDto = null;

        if (nextIndex < sapExportOrderList.size()) {
            mappingDto = sapExportOrderList.get(nextIndex);

            nextIndex++;

        }else {
            nextIndex = 0;
            sapExportOrderList = null;
        }

        return mappingDto;
    }

    private boolean sapExportOrderDataIsNotInitialized(){
        return this.sapExportOrderList == null;
    }

    /**
     * Get SAP data (rfc function : ZEX_CRMSI_OR)
     * @return
     */
    private List<SapExportOrderDto> fetchSapExportOrderDataFromAPI() throws JsonProcessingException, org.json.simple.parser.ParseException, JCoException {
        List<SapExportOrderDto> sapExportOrders = new ArrayList<SapExportOrderDto>();
        List<SapExportOrderItemDto> sapExportOrderItems = new ArrayList<SapExportOrderItemDto>();

        JCoDestination destination = JCoDestinationManager.getDestination("ABAP_AS_WITH_POOL");
        // STFC_CONNECTION function call
        JCoFunction function = destination.getRepository().getFunction("ZEX_CRMSI_OR");

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
            throw new RuntimeException("ZEX_CRMSI_OR not found in SAP.");

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

                SapExportOrderDto sapExportOrder = mapper.convertValue(map, SapExportOrderDto.class);
                sapExportOrders.add(sapExportOrder);

                SapExportOrderItemDto sapExportOrderItem = mapper.convertValue(map, SapExportOrderItemDto.class);
                sapExportOrderItems.add(sapExportOrderItem);
            }
        }

        if (sapExportOrders.size() > 0) {
            sapExportOrders = sapExportOrders.stream()
                    .filter(CommonUtils.distinctByKey(m -> m.getOrderNumber()))
                    .map(sapExportOrder -> modelMapper.map(sapExportOrder, SapExportOrderDto.class))
                    .collect(Collectors.toList());

            for (SapExportOrderDto sdoDto : sapExportOrders) {
                for(SapExportOrderItemDto sdoiDto : sapExportOrderItems) {
                    if(sdoDto.getOrderNumber().equals(sdoiDto.getOrderNumber())) {
                        sdoDto.getOrderItem().add(sdoiDto);
                    }
                }
            }

            log.info("ExportOrder - {} 건", sapExportOrders.size());
            log.info("ExportOrderItem - {} 건", sapExportOrderItems.size());

            InterfaceInfo interfaceInfo = new InterfaceInfo();
            if (sapExportOrders.size() % Constant.CHUNK_SIZE_S == 0) {
                interfaceInfo.setTotalPage(sapExportOrders.size()/ Constant.CHUNK_SIZE_S);
            }else {
                interfaceInfo.setTotalPage(sapExportOrders.size()/ Constant.CHUNK_SIZE_S + 1);
            }
            interfaceInfo.setChunkSize(Constant.CHUNK_SIZE_S);
            interfaceInfo.setTotalCount(sapExportOrders.size());

            jobExecution.getExecutionContext().put("IF", interfaceInfo);

        } else {
            log.info("ExportOrder - 업데이트된 데이터가 없습니다.");
        }

        return sapExportOrders;
    }
}