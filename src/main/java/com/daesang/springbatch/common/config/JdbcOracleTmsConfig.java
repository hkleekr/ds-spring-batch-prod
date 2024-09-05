package com.daesang.springbatch.common.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * fileName         : JdbcOracleTmsConfig
 * author           : 김수진과장
 * date             : 2022-11-10
 * descrition       : TMS 고객정보 정보 조회용 (SWS로 이관 -> TMS 추가 시 Connect 정보)
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-10       김수진과장             최초생성
 * 2022-11-24       김수진과장             default_schema 추가
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "com.daesang.springbatch.tms",
        entityManagerFactoryRef = "oracleTmsManagerFactory",
        transactionManagerRef = "oracleMdmTransactionManager"
)
public class JdbcOracleTmsConfig {

    @Value("${schema.tms}")
    private String default_schema;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.oracle-tms")
    public DataSource oracleTmsDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean oracleTmsManagerFactory() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(oracleTmsDataSource());

        // 엔티티 패키지 경로
        localContainerEntityManagerFactoryBean.setPackagesToScan(
            new String[]{
                    "com.daesang.springbatch.tms"
            }
        );

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        //Hibernate 설정
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.ddl-auto","none");
        properties.put("hibernate.dialect","org.hibernate.dialect.Oracle10gDialect");
        properties.put("hibernate.default_schema",default_schema);
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(properties);
        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager oracleTmsTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(oracleTmsManagerFactory().getObject());
        return transactionManager;
    }
}
