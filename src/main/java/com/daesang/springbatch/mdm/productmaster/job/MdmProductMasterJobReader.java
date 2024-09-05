package com.daesang.springbatch.mdm.productmaster.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.mdm.productmaster.domain.MdmProductMaster;
import com.daesang.springbatch.mdm.productmaster.domain.MdmProductMasterDto;
import com.daesang.springbatch.mdm.productmaster.repository.MdmProductMasterRepository;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * fileName         : MdmProductMasterJobReader
 * author           : inayoon
 * date             : 2022-10-21
 * description      : 제품정보 - 제품 마스터 정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-21       inayoon             최초생성
 * 2022-11-22       inayoon             Chunk_SIZE_S 변경
 */
@Slf4j
@RequiredArgsConstructor
public class MdmProductMasterJobReader implements ItemReader<MdmProductMasterDto> {

    private final MdmProductMasterRepository mdmProductMasterRepository;

    private final ModelMapper modelMapper;

    private List<MdmProductMasterDto> mdmProductMasterData;

    private int nextIndex;

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public MdmProductMasterDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (mdmProductMasterDataIsNotInitialized()){
            mdmProductMasterData = fetchMdmProductMasterDataFromAPI();
        }

        MdmProductMasterDto mappingDto = null;

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

    private List<MdmProductMasterDto> fetchMdmProductMasterDataFromAPI() throws JsonProcessingException, org.json.simple.parser.ParseException {
        Map<String, JobParameter> parameters = jobExecution.getJobParameters().getParameters();
        String fromDate = String.valueOf(parameters.get("fromDate"));
        String toDate = String.valueOf(parameters.get("toDate"));

        List<MdmProductMaster> mdmProductMasterList = mdmProductMasterRepository.findByLastUpdateDateBetween(fromDate, toDate);

        mdmProductMasterData = mdmProductMasterList.stream()
                .sorted(Comparator.comparing((MdmProductMaster::getMdmCode)).reversed()
                        .thenComparing(MdmProductMaster::getCreateDate).reversed()
                        .thenComparing(MdmProductMaster::getLastUpdateDate).reversed())
                .map(mdmProduct -> modelMapper.map(mdmProduct, MdmProductMasterDto.class))
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