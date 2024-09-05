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
 * fileName         : JdbcOracleHrConfig
 * author           : 김수진과장
 * date             : 2022-11-01
 * descrition       : HR DB 설정
 * =======================================================
 * DATE                AUTHOR             NOTE
 * -------------------------------------------------------
 * 2022-11-01       김수진과장             최초생성
 */
// 설정파일 어노테이션
@Configuration
// Spring Data Jpa를 활성화 (JPA Repository로 접근할수 있게 해줌)
@EnableJpaRepositories(
        // Repository 클래스가 위치한 패키지 경로 (업무 패키지로지정 - 하위폴더에 있는것들을 읽어드릴수 있게)
        basePackages = "com.daesang.springbatch.hr",
        // 해당 TransactionEntityManagerFactory가 사용할 메소드명
        entityManagerFactoryRef = "oracleHrManagerFactory",
        // 해당 TransactinoManager가 참조할 메소드 명
        transactionManagerRef = "oracleHrTransactionManager"
)
public class JdbcOracleHrConfig {

    @Bean
    // 프로퍼티 설정 파일을 읽어드리는 어노테이션 (yml 파일의 spring.datasource.oracle-hr의 설정을 가져온다.)
    @ConfigurationProperties(prefix = "spring.datasource.oracle-hr")
    // 내가 원하는 데이터 소스를 정의
    public DataSource oracleHrDataSource() {
        // HikariDataSource > oracle,h2 등의 커넥션을 도와줌
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    // EntityManagerFactory Bean(DataSource, Hibernate Property, Entity 설정 (Entity 패키지 설정))
    public LocalContainerEntityManagerFactoryBean oracleHrManagerFactory() {
        // localContainerEntityManagerFactoryBean 생성
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        // DataSource 설정
        localContainerEntityManagerFactoryBean.setDataSource(oracleHrDataSource());

        // 엔티티 패키지 경로 (@Entity 클래스가 위치할 패키지의 경로 > 업무별 패키지가 나뉘어져있어 basePackages와 동일하게 설정)
        localContainerEntityManagerFactoryBean.setPackagesToScan(
                new String[]{"com.daesang.springbatch.hr"}
        );

        // HibernateJpaVendorAdapter 생성 - JPA 규약과 Hibernate간의 Adapter
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("hrEntityManager");

        // Hibernate 설정
        HashMap<String, Object> properties = new HashMap<>();
        // DDL 설정
        properties.put("hibernate.ddl-auto","none");
        properties.put("hibernate.dialect","org.hibernate.dialect.Oracle10gDialect");
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(properties);
        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    // TransactionManager Bean (트랜잭션 관리를 위한 빈)
    public PlatformTransactionManager oracleHrTransactionManager() {
        // transactionManager 생성
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        // transactionManager에 EntityManagerFactory를 설정해 반환
        transactionManager.setEntityManagerFactory(oracleHrManagerFactory().getObject());
        return transactionManager;
    }

}
