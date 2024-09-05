package com.daesang.springbatch.sap.exportdelivery.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.util.CommonUtils;
import com.daesang.springbatch.sap.exportdelivery.domain.SapExportDeliveryDto;
import com.daesang.springbatch.sap.exportdelivery.domain.SapExportDeliveryItemDto;
import com.daesang.springbatch.sap.exportdelivery.domain.SapExportShipmentDto;
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
 * fileName         : SapExportDeliveryJobReader
 * author           : inayoon
 * date             : 2022-11-09
 * description      : 배송관리 - 수출 배송정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-09       inayoon             최초생성
 * 2022-11-22       inayoon             CHUNK_SIZE_S 변경
 */
@Slf4j
@RequiredArgsConstructor
public class SapExportDeliveryJobReader implements ItemReader<SapExportDeliveryDto> {

    private final ModelMapper modelMapper;
    private List<SapExportDeliveryDto> sapExportDeliveryList;
    private int nextIndex;
    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public SapExportDeliveryDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (sapExportDeliveryDataIsNotInitialized()){
            sapExportDeliveryList = fetchSapExportDeliveryDataFromAPI();
        }

        SapExportDeliveryDto mappingDto = null;

        if (nextIndex < sapExportDeliveryList.size()) {
            mappingDto = sapExportDeliveryList.get(nextIndex);

            nextIndex++;

        }else {
            nextIndex = 0;
            sapExportDeliveryList = null;
        }

        return mappingDto;
    }

    private boolean sapExportDeliveryDataIsNotInitialized(){
        return this.sapExportDeliveryList == null;
    }

    /**
     * Get SAP data (rfc function : ZEX_CRMSI_DN)
     * @return
     */
    private List<SapExportDeliveryDto> fetchSapExportDeliveryDataFromAPI() throws JsonProcessingException, org.json.simple.parser.ParseException, JCoException {
        List<SapExportDeliveryDto> sapExportDeliverys = new ArrayList<SapExportDeliveryDto>();
        List<SapExportDeliveryItemDto> sapExportDeliveryItems = new ArrayList<SapExportDeliveryItemDto>();
        List<SapExportShipmentDto> sapExportShipments = new ArrayList<SapExportShipmentDto>();

        JCoDestination destination = JCoDestinationManager.getDestination("ABAP_AS_WITH_POOL");
        // STFC_CONNECTION function call
        JCoFunction function = destination.getRepository().getFunction("ZEX_CRMSI_DN");

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
            throw new RuntimeException("ZEX_CRMSI_DN not found in SAP.");

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

                SapExportDeliveryDto sapExportDelivery = mapper.convertValue(map, SapExportDeliveryDto.class);
                sapExportDeliverys.add(sapExportDelivery);

                SapExportDeliveryItemDto sapExportDeliveryItem = mapper.convertValue(map, SapExportDeliveryItemDto.class);
                sapExportDeliveryItems.add(sapExportDeliveryItem);

                if((map.get("MBLNO") != null && !map.get("MBLNO").equals("")) || (map.get("HBLNO") != null && !map.get("HBLNO").equals(""))) {
                    SapExportShipmentDto sapExportShipment = mapper.convertValue(map, SapExportShipmentDto.class);
                    sapExportShipments.add(sapExportShipment);
                }
            }
        }

        if (sapExportDeliverys.size() > 0) {
            // Delivery
            sapExportDeliverys = sapExportDeliverys.stream()
                    .filter(CommonUtils.distinctByKey(m -> m.getDoDoc()))
                    .map(sapDomesticDelivery -> modelMapper.map(sapDomesticDelivery, SapExportDeliveryDto.class))
                    .collect(Collectors.toList());
            // Shipment
            sapExportShipments = sapExportShipments.stream()
                    .filter(m -> m.getDoDoc() != null || !m.getDoDoc().equals(""))
                    .map(sapDomesticShipment -> modelMapper.map(sapDomesticShipment, SapExportShipmentDto.class))
                    .collect(Collectors.toList());

            for (SapExportDeliveryDto sedDto : sapExportDeliverys) {
                 // DeliveryItem
                for(SapExportDeliveryItemDto sediDto : sapExportDeliveryItems) {
                    if(sedDto.getDoDoc().equals(sediDto.getDoDoc())) {
                        sedDto.getDeliveryItem().add(sediDto);
                    }
                }
                // Shipment
                for(SapExportShipmentDto sesDto : sapExportShipments) {
                    if(sesDto.getMasterBL() != null && !sesDto.getMasterBL().equals("")) {  // MasterBL
                        if(sedDto.getDoDoc().equals(sesDto.getDoDoc())) {
                            sedDto.getShipment().add(sesDto);
                        }
                    } else {  // HouseBL
                        if(sedDto.getDoDoc().equals(sesDto.getDoDoc())) {
                            sedDto.getShipment().add(sesDto);
                        }
                    }
                }
            }

            log.info("ExportDelivery - {} 건", sapExportDeliverys.size());
            log.info("ExportDeliveryItem - {} 건", sapExportDeliveryItems.size());
            log.info("ExportShipmentItem - {} 건", sapExportShipments.size());

            InterfaceInfo interfaceInfo = new InterfaceInfo();
            if (sapExportDeliverys.size() % Constant.CHUNK_SIZE_S == 0) {
                interfaceInfo.setTotalPage(sapExportDeliverys.size()/ Constant.CHUNK_SIZE_S);
            }else {
                interfaceInfo.setTotalPage(sapExportDeliverys.size()/ Constant.CHUNK_SIZE_S + 1);
            }
            interfaceInfo.setChunkSize(Constant.CHUNK_SIZE_S);
            interfaceInfo.setTotalCount(sapExportDeliverys.size());

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("IF", interfaceInfo);
            jobExecution.getExecutionContext().put("IF", interfaceInfo);

        } else {
            log.info("ExportDelivery - 업데이트된 데이터가 없습니다.");
        }

        return sapExportDeliverys;
    }
}
