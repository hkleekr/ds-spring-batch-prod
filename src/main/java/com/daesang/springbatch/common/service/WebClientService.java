package com.daesang.springbatch.common.service;

import com.daesang.springbatch.common.enumerate.UrlEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;


/**
 * Batch Job ItemWriter에서 호출
 * RestAPI 호출 공통 서비스
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WebClientService {

    private final RestApiService<JSONObject> restApiService;

    /**
     * Call Spring Cloud Gateway Service (post)
     * @param object
     * @param requestHost
     * @param urlEnum
     * @return
     */
    public void getGatewayResponse(JSONObject object, String requestHost, UrlEnum urlEnum) throws JsonProcessingException {
        object.put("InterfaceId", urlEnum.getName());
        //log.info(request);
        try {
            //restapi 호출 시 endpoint url 정보는 UrlEnum 클래스를 통해서 전달 한다.
            //Object는 ItemReader에서 읽어드린 정보로 Chunk 단위로 ItemWriter로 전달하여 JSON OBJECT에 Data Set
            JSONObject body = restApiService.post(requestHost + urlEnum.getSalesforceUrl(), restApiService.setHeaderInfo("application/json"), object, JSONObject.class).getBody();
            log.info("responseStatus:  {}", body.get("status"));
            log.info("responseMessage: {}", body.get("message"));
            log.info("responseDate:    {}", body.get("responseDate"));
        } catch (Exception ex) {
            //Error 발생 시 request 정보 확인
            ObjectMapper objectMapper = new ObjectMapper();
            String request = objectMapper.writeValueAsString(object.get("request"));
            log.error("EXCEPTION MESSAGE :::::::: {}", ex.getMessage());
            log.error("EXCEPTION CAUSE   :::::::: {}", ex.getCause());
            log.error("EXCEPTION DATA    :::::::: {}", request);
        }
    }


    /**
     * 시험성적서 - salesforce에서 return 정보를 받아 후 처리 작업을 위해 필요
     * @param
     * @return JSONObject
     */
    public JSONObject getGatewayResponse2(JSONObject object, String requestHost, UrlEnum urlEnum) throws JsonProcessingException {
        object.put("InterfaceId", urlEnum.getName());
        //log.info(request);
        try {
            JSONObject body = restApiService.post(requestHost + urlEnum.getSalesforceUrl(), restApiService.setHeaderInfo("application/json"), object, JSONObject.class).getBody();
            log.info("responseStatus:  {}", body.get("status"));
            log.info("responseMessage: {}", body.get("message"));
            log.info("responseDate:    {}", body.get("responseDate"));
            return body;
        } catch (Exception ex) {
            ObjectMapper objectMapper = new ObjectMapper();
            String request = objectMapper.writeValueAsString(object.get("request"));
            log.error("EXCEPTION MESSAGE :::::::: {}", ex.getMessage());
            log.error("EXCEPTION CAUSE   :::::::: {}", ex.getCause());
            log.error("EXCEPTION DATA    :::::::: {}", request);
            return null;
        }
    }

}
