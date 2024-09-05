package com.daesang.springbatch.common.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * fileName         : JdbcH2Config
 * author           : 김수진과장
 * date             : 2022-11-01
 * descrition       : Spring Boot Batch Default DB (Batch Meta 정보 JOB 실행 정보)
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-01       김수진과장             최초생성
 */

@Configuration
@EnableJpaRepositories(
        basePackages = "com.daesang.springbatch.common.repository.h2",
        entityManagerFactoryRef = "h2ManagerFactory",
        transactionManagerRef = "h2TransactionManager"
)
// batch job에 대한 정보(테이블)가 DB에 쌓이기 때문에 h2로 설정 (다른DB 서버에 로그를 쌓을수 없기때문에 내부에 쌓기위함)
public class JdbcH2Config {
    /**
     * Default 설정을 위해서 @Primary annotation 설정
     * @return
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.h2")
    public DataSource h2DataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @Primary // 우선순위 빈을 지정
    public LocalContainerEntityManagerFactoryBean h2ManagerFactory() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(h2DataSource());

        // JPA 엔티티 패키지 경로 설정
        localContainerEntityManagerFactoryBean.setPackagesToScan(
                new String[]{"com.daesang.springbatch.common.domain.h2"}
        );

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        //Hibernate 설정
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.ddl-auto","none");
        properties.put("hibernate.dialect","org.hibernate.dialect.H2Dialect");
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(properties);
        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    @Primary
    public PlatformTransactionManager h2TransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(h2ManagerFactory().getObject());
        return transactionManager;
    }
}
