package com.daesang.springbatch.mdm.accountpartner.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.service.RestApiService;
import com.daesang.springbatch.mdm.accountmaster.repository.MdmAccountMasterRepository;
import com.daesang.springbatch.mdm.accountpartner.domain.MdmAccountPartner;
import com.daesang.springbatch.mdm.accountpartner.domain.MdmAccountPartnerDto;
import com.daesang.springbatch.mdm.accountpartner.repository.MdmAccountPartnerRepositorySupport;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * fileName         : MdmAccountPartnerJobReader
 * author           : 김수진과장
 * date             : 2022-11-10
 * descrition       : 고객정보 - Account 파트너 기능 배치
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-10       김수진과장             최초생성
 */
@Slf4j
@RequiredArgsConstructor
public class MdmAccountPartnerJobReader implements ItemReader<MdmAccountPartnerDto> {

    private final MdmAccountPartnerRepositorySupport mdmAccountPartnerRepositorySupport;
    private List<MdmAccountPartnerDto> mdmAccountPartner;

    private final RestApiService<MdmAccountPartnerDto> restApiService;
    private final ModelMapper modelMapper;
    private int nextIndex;
    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public MdmAccountPartnerDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if (mdmAccountPartnerIsNotInitialized()) {
            mdmAccountPartner = fetchAccountPartnerData();
        }

        MdmAccountPartnerDto accountPartnerDto = null;

        if (nextIndex < mdmAccountPartner.size()) {
            accountPartnerDto = mdmAccountPartner.get(nextIndex);
            nextIndex++;
        }  else {
            nextIndex = 0;
            mdmAccountPartner = null;
        }
        return accountPartnerDto;
    }

    private boolean mdmAccountPartnerIsNotInitialized(){
        return this.mdmAccountPartner == null;
    }

    private List<MdmAccountPartnerDto> fetchAccountPartnerData() throws JsonProcessingException, org.json.simple.parser.ParseException {

        try {
            Map<String, JobParameter> parameters = jobExecution.getJobParameters().getParameters();
            String fromDate = String.valueOf(parameters.get("fromDate"));
            String toDate = String.valueOf(parameters.get("toDate"));
            log.info(fromDate);
            log.info(toDate);

            List<MdmAccountPartner> mdmAccountPartnerList = mdmAccountPartnerRepositorySupport.findByPartnerList(fromDate,toDate);

            mdmAccountPartner = mdmAccountPartnerList.stream()
                    .map(partner -> modelMapper.map(partner, MdmAccountPartnerDto.class))
                    .collect(Collectors.toList());

            if (mdmAccountPartnerList.size() > 0) {
                log.info("데이터 조회 성공 - {} 건", mdmAccountPartnerList.size());

                InterfaceInfo interfaceInfo = new InterfaceInfo();

                if (mdmAccountPartnerList.size() % Constant.CHUNK_SIZE_L == 0) {
                    interfaceInfo.setTotalPage(mdmAccountPartnerList.size() / Constant.CHUNK_SIZE_L);
                } else {
                    interfaceInfo.setTotalPage(mdmAccountPartnerList.size() / Constant.CHUNK_SIZE_L + 1);
                }

                interfaceInfo.setChunkSize(Constant.CHUNK_SIZE_L);
                interfaceInfo.setTotalCount(mdmAccountPartnerList.size());

                jobExecution.getExecutionContext().put("IF", interfaceInfo);

            } else {
                log.info("업데이트된 데이터가 없습니다.");
            }

        } catch (Exception e) {
            log.error("fetchMdmAccountPartnerData: {}", e.getMessage());
        }

        return mdmAccountPartner;
    }
}
