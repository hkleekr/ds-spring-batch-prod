package com.daesang.springbatch.common.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    /**
     * RestTemplate 기본 설정 정보
     * @param restTemplateBuilder
     * @return
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {

        return restTemplateBuilder
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .setConnectTimeout(Duration.ofMillis(120000))	//connection-timeout Salesforce ConnectionTime out 시간에 맞춰 설정
                .setReadTimeout(Duration.ofMillis(0))	//read-timeout
                .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8"))) //MesageConverter Charset 설정 UTF-8
                .build();
    }

}