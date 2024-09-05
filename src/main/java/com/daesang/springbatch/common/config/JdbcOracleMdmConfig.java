package com.daesang.springbatch.common.config;

import com.zaxxer.hikari.HikariDataSource;
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
 * fileName         : JdbcOracleMdmConfig
 * author           : 김수진과장
 * date             : 2022-11-01
 * descrition       : MDM DB 설정
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-01       김수진과장             최초생성
 */

@Configuration
@EnableJpaRepositories(
        basePackages = "com.daesang.springbatch.mdm",
        entityManagerFactoryRef = "oracleMdmManagerFactory",
        transactionManagerRef = "oracleMdmTransactionManager"
)
public class JdbcOracleMdmConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.oracle-mdm")
    public DataSource oracleMdmDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean oracleMdmManagerFactory() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(oracleMdmDataSource());

        // 엔티티 패키지 경로
        localContainerEntityManagerFactoryBean.setPackagesToScan(
                new String[]{
                          "com.daesang.springbatch.mdm"
                }
        );

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("mdmEntityManager");

        //Hibernate 설정
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.ddl-auto","none");
        properties.put("hibernate.dialect","org.hibernate.dialect.Oracle10gDialect");
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(properties);
        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager oracleMdmTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(oracleMdmManagerFactory().getObject());
        return transactionManager;
    }

}
