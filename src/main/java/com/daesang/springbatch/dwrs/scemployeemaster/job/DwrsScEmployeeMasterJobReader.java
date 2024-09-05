package com.daesang.springbatch.dwrs.scemployeemaster.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.enumerate.UrlEnum;
import com.daesang.springbatch.common.service.RestApiService;
import com.daesang.springbatch.dwrs.scemployeeaccount.domain.DwrsScEmployeeAccountRestDto;
import com.daesang.springbatch.dwrs.scemployeemaster.domain.DwrsScEmployeeMasterMappingDto;
import com.daesang.springbatch.dwrs.scemployeemaster.domain.DwrsScEmployeeMasterRestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.daesang.springbatch.dwrs.scemployeemaster.job.DwrsScEmployeeMasterJobConfiguration.*;


@RequiredArgsConstructor
public class DwrsScEmployeeMasterJobReader implements ItemReader<DwrsScEmployeeMasterMappingDto> {

    private final String apiURI;
    private final RestApiService<DwrsScEmployeeMasterRestDto> restApiService;
    private List<DwrsScEmployeeMasterMappingDto> dwrsScEmployeeMasterData;
    private int nextIndex;

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }
    @Override
    public DwrsScEmployeeMasterMappingDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (dwrsScEmployeeMasterDataIsNotInitialized()){
            dwrsScEmployeeMasterData = fetchDwrsScEmployeeMasterDataFromAPI();
        }

        DwrsScEmployeeMasterMappingDto mappingDto = null;


        if (nextIndex < dwrsScEmployeeMasterData.size()) {

            mappingDto = dwrsScEmployeeMasterData.get(nextIndex);

//            System.out.println("next_item : " + mappingDto);
            nextIndex++;

        }
        else{
            nextIndex = 0;
            dwrsScEmployeeMasterData = null;
        }

        return mappingDto;
        //return res;
    }

    private boolean dwrsScEmployeeMasterDataIsNotInitialized(){
        return this.dwrsScEmployeeMasterData == null;
    }

    private List<DwrsScEmployeeMasterMappingDto> fetchDwrsScEmployeeMasterDataFromAPI() throws JsonProcessingException, org.json.simple.parser.ParseException {

//        String uriInfo = UriComponentsBuilder.fromHttpUrl(UrlEnum.dwrs_91.getHost() + UrlEnum.dwrs_91.getUrl())
//                .queryParam("fromDate", "20221101")
//                .queryParam("toDate", "20221110").build().toString();

        DwrsScEmployeeMasterRestDto restBody = restApiService.get(apiURI, restApiService.setHeaderInfo("application/json"), DwrsScEmployeeMasterRestDto.class).getBody();

//        String str = "{\n" +
//                "\"data\": [\n" +
//                "{\n" +
//                "\"PERNR\": \"224230\",\n" +
//                "\"ENAME\": \"224\",\n" +
//                "\"EMAIL\": \"HMR쌀국수(상온)\",\n" +
//                "\"CELPH\": \"N\",\n" +
//                "\"TITL2\": \"224230100\",\n" +
//                "\"STELL\": \"224230100\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"PERNR\": \"224231\",\n" +
//                "\"ENAME\": \"224\",\n" +
//                "\"EMAIL\": \"HMR쌀국수(상온)\",\n" +
//                "\"CELPH\": \"N\",\n" +
//                "\"TITL2\": \"224230100\",\n" +
//                "\"STELL\": \"224230100\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"PERNR\": \"224232\",\n" +
//                "\"ENAME\": \"224\",\n" +
//                "\"EMAIL\": \"HMR쌀국수(상온)\",\n" +
//                "\"CELPH\": \"N\",\n" +
//                "\"TITL2\": \"224230100\",\n" +
//                "\"STELL\": \"224230100\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"PERNR\": \"224233\",\n" +
//                "\"ENAME\": \"224\",\n" +
//                "\"EMAIL\": \"HMR쌀국수(상온)\",\n" +
//                "\"CELPH\": \"N\",\n" +
//                "\"TITL2\": \"224230100\",\n" +
//                "\"STELL\": \"224230100\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"PERNR\": \"224234\",\n" +
//                "\"ENAME\": \"224\",\n" +
//                "\"EMAIL\": \"HMR쌀국수(상온)\",\n" +
//                "\"CELPH\": \"N\",\n" +
//                "\"TITL2\": \"224230100\",\n" +
//                "\"STELL\": \"224230100\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"PERNR\": \"224235\",\n" +
//                "\"ENAME\": \"224\",\n" +
//                "\"EMAIL\": \"HMR쌀국수(상온)\",\n" +
//                "\"CELPH\": \"N\",\n" +
//                "\"TITL2\": \"224230100\",\n" +
//                "\"STELL\": \"224230100\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"PERNR\": \"224236\",\n" +
//                "\"ENAME\": \"224\",\n" +
//                "\"EMAIL\": \"HMR쌀국수(상온)\",\n" +
//                "\"CELPH\": \"N\",\n" +
//                "\"TITL2\": \"224230100\",\n" +
//                "\"STELL\": \"224230100\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"PERNR\": \"224237\",\n" +
//                "\"ENAME\": \"224\",\n" +
//                "\"EMAIL\": \"HMR쌀국수(상온)\",\n" +
//                "\"CELPH\": \"N\",\n" +
//                "\"TITL2\": \"224230100\",\n" +
//                "\"STELL\": \"224230100\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"PERNR\": \"224238\",\n" +
//                "\"ENAME\": \"224\",\n" +
//                "\"EMAIL\": \"HMR쌀국수(상온)\",\n" +
//                "\"CELPH\": \"N\",\n" +
//                "\"TITL2\": \"224230100\",\n" +
//                "\"STELL\": \"224230100\"\n" +
//                "}],\n" +
//                "\"code\" : \"0\"\n" +
//                "}\n" +
//                "\n" +
//                "\n" +
//                "\n";

//        ObjectMapper mapper = new ObjectMapper();
//
//        DwrsScEmployeeMasterRestDto body = mapper.readValue(str, DwrsScEmployeeMasterRestDto.class);

        List<DwrsScEmployeeMasterMappingDto> list = restBody.getData();

        InterfaceInfo interfaceInfo = new InterfaceInfo();

        if (list.size() % Constant.CHUNK_SIZE == 0){
            interfaceInfo.setTotalPage(list.size()/Constant.CHUNK_SIZE);
        }else {
            interfaceInfo.setTotalPage(list.size()/Constant.CHUNK_SIZE + 1);
        }
        interfaceInfo.setChunkSize(Constant.CHUNK_SIZE);
        interfaceInfo.setTotalCount(list.size());

        Map<String, Object> map = new HashMap<>();
        map.put("IF", interfaceInfo);
        jobExecution.getExecutionContext().put("IF", interfaceInfo);

        System.out.println("====================" + list);
        return list;
    }
}
