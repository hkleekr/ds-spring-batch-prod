package com.daesang.springbatch.mdm.accountmaster.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.mdm.accountmaster.domain.MdmAccountMaster;
import com.daesang.springbatch.mdm.accountmaster.domain.MdmAccountMasterDto;
import com.daesang.springbatch.mdm.accountmaster.repository.MdmAccountMasterRepository;
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
 * fileName         : MdmAccountMaster
 * author           : inayoon
 * date             : 2022-10-19
 * description      : 고객정보 - Account 마스터 정보
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-10-19       inayoon             최초생성
 * 2022-11-02       inayoon             Chunk 방식 변경
 * 2022-11-22       inayoon             Chunk_SIZE_S 변경
 */
@Slf4j
@RequiredArgsConstructor
public class MdmAccountMasterJobReader implements ItemReader<MdmAccountMasterDto> {

    private final MdmAccountMasterRepository mdmAccountMasterRepository;

    private final ModelMapper modelMapper;

    private List<MdmAccountMasterDto> mdmAccountMasterData;
    private int nextIndex;

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public MdmAccountMasterDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (mdmAccountMasterDataIsNotInitialized()){
            mdmAccountMasterData = fetchMdmAccountMasterDataFromAPI();
        }

        MdmAccountMasterDto mappingDto = null;

        if (nextIndex < mdmAccountMasterData.size()) {
            mappingDto = mdmAccountMasterData.get(nextIndex);

            nextIndex++;

        }else {
            nextIndex = 0;
            mdmAccountMasterData = null;
        }

        return mappingDto;
    }

    private boolean mdmAccountMasterDataIsNotInitialized(){
        return this.mdmAccountMasterData == null;
    }

    private List<MdmAccountMasterDto> fetchMdmAccountMasterDataFromAPI() throws JsonProcessingException, org.json.simple.parser.ParseException {
        Map<String, JobParameter> parameters = jobExecution.getJobParameters().getParameters();
        String fromDate = String.valueOf(parameters.get("fromDate"));
        String toDate = String.valueOf(parameters.get("toDate"));

        List<MdmAccountMaster> mdmAccountMasterList = mdmAccountMasterRepository.findByLastUpdateDateBetween(fromDate, toDate);

        mdmAccountMasterData = mdmAccountMasterList.stream()
                .sorted(Comparator.comparing((MdmAccountMaster::getMdmCode)).reversed()
                        .thenComparing(MdmAccountMaster::getCreateDate).reversed()
                        .thenComparing(MdmAccountMaster::getLastUpdateDate).reversed())
                .map(mdmAccount -> modelMapper.map(mdmAccount, MdmAccountMasterDto.class))
                .collect(Collectors.toList());

        if (mdmAccountMasterData.size() > 0) {
            log.info("MdmAccountMaster - {} 건", mdmAccountMasterData.size());

            InterfaceInfo interfaceInfo = new InterfaceInfo();
            if (mdmAccountMasterData.size() % Constant.CHUNK_SIZE_S == 0){
                interfaceInfo.setTotalPage(mdmAccountMasterData.size()/Constant.CHUNK_SIZE_S);
            }else {
                interfaceInfo.setTotalPage(mdmAccountMasterData.size()/Constant.CHUNK_SIZE_S + 1);
            }
            interfaceInfo.setChunkSize(Constant.CHUNK_SIZE_S);
            interfaceInfo.setTotalCount(mdmAccountMasterData.size());

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("IF", interfaceInfo);
            jobExecution.getExecutionContext().put("IF", interfaceInfo);

        } else {
            log.info("업데이트된 데이터가 없습니다.");
        }

        return mdmAccountMasterData;
    }
}