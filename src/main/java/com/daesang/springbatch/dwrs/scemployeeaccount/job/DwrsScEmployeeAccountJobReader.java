package com.daesang.springbatch.dwrs.scemployeeaccount.job;

import com.daesang.springbatch.common.Constant;
import com.daesang.springbatch.common.domain.InterfaceInfo;
import com.daesang.springbatch.common.enumerate.UrlEnum;
import com.daesang.springbatch.common.service.RestApiService;
import com.daesang.springbatch.dwrs.scemployeeaccount.domain.DwrsScEmployeeAccountMappingDto;
import com.daesang.springbatch.dwrs.scemployeeaccount.domain.DwrsScEmployeeAccountRestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
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

@RequiredArgsConstructor
public class DwrsScEmployeeAccountJobReader implements ItemReader<DwrsScEmployeeAccountMappingDto> {

    private final String apiURI;
    private final RestApiService<DwrsScEmployeeAccountRestDto> restApiService;
    private List<DwrsScEmployeeAccountMappingDto> dwrsScEmployeeAccountData;
    private int nextIndex;

    private JobExecution jobExecution;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecution = stepExecution.getJobExecution();
    }
    @Override
    public DwrsScEmployeeAccountMappingDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (dwrsScEmployeeAccountDataIsNotInitialized()){
            dwrsScEmployeeAccountData = fetchDwrsScEmployeeAccountDataFromAPI();
        }

        DwrsScEmployeeAccountMappingDto mappingDto = null;


        if (nextIndex < dwrsScEmployeeAccountData.size()) {
            //nextItem =

            mappingDto = dwrsScEmployeeAccountData.get(nextIndex);

//            System.out.println("next_item : " + mappingDto);
            nextIndex++;

        }
        else{
            nextIndex = 0;
            dwrsScEmployeeAccountData = null;
        }

        return mappingDto;
        //return res;
    }

    private boolean dwrsScEmployeeAccountDataIsNotInitialized(){
        return this.dwrsScEmployeeAccountData == null;
    }

    private List<DwrsScEmployeeAccountMappingDto> fetchDwrsScEmployeeAccountDataFromAPI() throws JsonProcessingException, org.json.simple.parser.ParseException {

      String uriInfo = UriComponentsBuilder.fromHttpUrl(apiURI).build().toString();
//                .queryParam("fromDate", "20221101")
//                .queryParam("toDate", "20221102").build().toString();

      DwrsScEmployeeAccountRestDto restBody = restApiService.get(uriInfo, restApiService.setHeaderInfo("application/json"), DwrsScEmployeeAccountRestDto.class).getBody();


//        String str = "{\n" +
//                "\"data\": [\n" +
//                "{\n" +
//                "\"CUST_ID\": \"0001005359\",\n" +
//                "\"PERNR\": \"22020083\",\n" +
//                "\"REG_DT\": \"20221014\",\n" +
//                "\"UPDATE_DT\": \"20221014\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"CUST_ID\": \"0001043410\",\n" +
//                "\"PERNR\": \"21070431\",\n" +
//                "\"REG_DT\": \"20221014\",\n" +
//                "\"UPDATE_DT\": \"20221014\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"CUST_ID\": \"0001007133\",\n" +
//                "\"PERNR\": \"22080522\",\n" +
//                "\"REG_DT\": \"20221014\",\n" +
//                "\"UPDATE_DT\": \"20221014\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"CUST_ID\": \"0001008244\",\n" +
//                "\"PERNR\": \"22080049\",\n" +
//                "\"REG_DT\": \"20221014\",\n" +
//                "\"UPDATE_DT\": \"20221014\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"CUST_ID\": \"0001029341\",\n" +
//                "\"PERNR\": \"22080993\",\n" +
//                "\"REG_DT\": \"20221014\",\n" +
//                "\"UPDATE_DT\": \"20221014\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"CUST_ID\": \"0001000129\",\n" +
//                "\"PERNR\": \"22070025\",\n" +
//                "\"REG_DT\": \"20221014\",\n" +
//                "\"UPDATE_DT\": \"20221014\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"CUST_ID\": \"0001000344\",\n" +
//                "\"PERNR\": \"22070030\",\n" +
//                "\"REG_DT\": \"20221014\",\n" +
//                "\"UPDATE_DT\": \"20221014\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"CUST_ID\": \"0001000336\",\n" +
//                "\"PERNR\": \"21040464\",\n" +
//                "\"REG_DT\": \"20221014\",\n" +
//                "\"UPDATE_DT\": \"20221014\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"CUST_ID\": \"0001000339\",\n" +
//                "\"PERNR\": \"21040469\",\n" +
//                "\"REG_DT\": \"20221014\",\n" +
//                "\"UPDATE_DT\": \"20221014\"\n" +
//                "}],\n" +
//                "\"code\" : \"0\"\n" +
//                "}\n" +
//                "\n" +
//                "\n" +
//                "\n";

//        ObjectMapper mapper = new ObjectMapper();
//
//        DwrsScEmployeeAccountRestDto body = mapper.readValue(str, DwrsScEmployeeAccountRestDto.class);
//        List<DwrsScEmployeeAccountMappingDto> list = body.getData();

        List<DwrsScEmployeeAccountMappingDto> list = restBody.getData();

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
