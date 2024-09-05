package com.daesang.springbatch.mdm.productSalesInfo.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.mdm.productSalesInfo.domain.MdmProductSalesInfo;
import com.daesang.springbatch.mdm.productSalesInfo.domain.MdmProductSalesInfoDto;
import com.daesang.springbatch.mdm.productSalesInfo.repository.MdmProductSalesInfoRepositorySupport;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * fileName         : MdmProductSalesInfoJobReader
 * author           : 권용성사원
 * date             : 2023-01-17
 * description      : 제품정보 - 제품 판매조직 기준 정보 배치 READER
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-01-17       권용성사원             최초생성
 */
@Slf4j
@RequiredArgsConstructor
public class MdmProductSalesInfoJobReader implements ItemReader<MdmProductSalesInfoDto> {

    private final MdmProductSalesInfoRepositorySupport mdmProductSalesInfoRepositorySupport;

    private final ModelMapper modelMapper;

    private List<MdmProductSalesInfoDto> mdmProductMasterData;

    private int nextIndex;

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public MdmProductSalesInfoDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (mdmProductMasterDataIsNotInitialized()){
            mdmProductMasterData = fetchMdmProductMasterDataFromAPI();
        }

        MdmProductSalesInfoDto mappingDto = null;

        if (nextIndex < mdmProductMasterData.size()) {
            mappingDto = mdmProductMasterData.get(nextIndex);

            nextIndex++;

        }else {
            nextIndex = 0;
            mdmProductMasterData = null;
        }

        return mappingDto;
    }

    private boolean mdmProductMasterDataIsNotInitialized(){
        return this.mdmProductMasterData == null;
    }

    private List<MdmProductSalesInfoDto> fetchMdmProductMasterDataFromAPI() throws JsonProcessingException, org.json.simple.parser.ParseException {
        Map<String, JobParameter> parameters = jobExecution.getJobParameters().getParameters();
        String fromDate = String.valueOf(parameters.get("fromDate"));
        String toDate = String.valueOf(parameters.get("toDate"));

        List<MdmProductSalesInfo> mdmProductSalesInfoList = mdmProductSalesInfoRepositorySupport.findByLastUpdateDateBetween(fromDate, toDate);

        mdmProductMasterData = mdmProductSalesInfoList.stream()
                .sorted(Comparator.comparing((MdmProductSalesInfo::getMdmCode)).reversed()
                        .thenComparing(MdmProductSalesInfo::getCreateDate).reversed()
                        .thenComparing(MdmProductSalesInfo::getLastUpdateDate).reversed())
                .map(mdmProduct -> modelMapper.map(mdmProduct, MdmProductSalesInfoDto.class))
                .collect(Collectors.toList());

        if (mdmProductMasterData.size() > 0) {
            log.info("MdmProductMaster - {} 건", mdmProductMasterData.size());

            InterfaceInfo interfaceInfo = new InterfaceInfo();
            if (mdmProductMasterData.size() % Constant.CHUNK_SIZE_S == 0){
                interfaceInfo.setTotalPage(mdmProductMasterData.size()/Constant.CHUNK_SIZE_S);
            }else {
                interfaceInfo.setTotalPage(mdmProductMasterData.size()/Constant.CHUNK_SIZE_S + 1);
            }
            interfaceInfo.setChunkSize(Constant.CHUNK_SIZE_S);
            interfaceInfo.setTotalCount(mdmProductMasterData.size());

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("IF", interfaceInfo);
            jobExecution.getExecutionContext().put("IF", interfaceInfo);

        } else {
            log.info("업데이트된 데이터가 없습니다.");
        }

        return mdmProductMasterData;
    }
}