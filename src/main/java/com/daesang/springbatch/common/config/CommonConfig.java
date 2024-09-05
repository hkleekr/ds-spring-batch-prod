package com.daesang.springbatch.common.config;

import com.daesang.springbatch.common.domain.LogReportProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * fileName         : ReportConfig
 * author           : 권용성사원
 * date             : 2023-03-03
 * description      : 업무구분 - 상세업무
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2023-03-03       권용성사원             최초생성
 */
@Configuration
public class CommonConfig {
    @Bean
    @ConfigurationProperties(prefix = "report")
    public LogReportProperties logReportProperties(){
        return new LogReportProperties();
    }
}
