package com.daesang.springbatch.dwrs.scemployee.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.service.RestApiService;
import com.daesang.springbatch.dwrs.scemployee.domain.DwrsScEmployeeMappingDto;
import com.daesang.springbatch.dwrs.scemployee.domain.DwrsScEmployeeRestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class DwrsScEmployeeJobReader implements ItemReader<DwrsScEmployeeMappingDto> {

    private final String apiURI;
    private final RestApiService<DwrsScEmployeeRestDto> restApiService;
    private List<DwrsScEmployeeMappingDto> dwrsScEmployeeData;
    private int nextIndex;

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public DwrsScEmployeeMappingDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (dwrsScEmployeeDataIsNotInitialized()){
            dwrsScEmployeeData = fetchDwrsScEmployeeDataFromAPI();
        }

        DwrsScEmployeeMappingDto mappingDto = null;


        if (nextIndex < dwrsScEmployeeData.size()) {
            //nextItem =

            mappingDto = dwrsScEmployeeData.get(nextIndex);

//            System.out.println("next_item : " + mappingDto);
            nextIndex++;

        }
        else{
            nextIndex = 0;
            dwrsScEmployeeData = null;
        }

        return mappingDto;
        //return res;
    }

    private boolean dwrsScEmployeeDataIsNotInitialized(){
        return this.dwrsScEmployeeData == null;
    }

    private List<DwrsScEmployeeMappingDto> fetchDwrsScEmployeeDataFromAPI() throws JsonProcessingException, org.json.simple.parser.ParseException {

        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        String dDate = yesterday.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String url = UriComponentsBuilder.fromHttpUrl(apiURI)
                .queryParam("dDate", dDate).build().toString();
//                .queryParam("frMonth", "202211")
//                .queryParam("toMonth", "202211").build().toString();

        DwrsScEmployeeRestDto restBody = restApiService.get(url, restApiService.setHeaderInfo("application/json"),DwrsScEmployeeRestDto.class).getBody();

        List<DwrsScEmployeeMappingDto> list = restBody.getData();

        InterfaceInfo interfaceInfo = new InterfaceInfo();

        if (list.size() % Constant.CHUNK_SIZE_L == 0){
            interfaceInfo.setTotalPage(list.size()/Constant.CHUNK_SIZE_L);
        }else {
            interfaceInfo.setTotalPage(list.size()/Constant.CHUNK_SIZE_L + 1);
        }
        interfaceInfo.setChunkSize(Constant.CHUNK_SIZE_L);
        interfaceInfo.setTotalCount(list.size());

        Map<String, Object> map = new HashMap<>();
        map.put("IF", interfaceInfo);
        jobExecution.getExecutionContext().put("IF", interfaceInfo);

        System.out.println("====================" + list);
        return list;
    }
}
