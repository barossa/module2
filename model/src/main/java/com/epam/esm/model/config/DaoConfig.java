package com.epam.esm.model.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@PropertySource(value = "classpath:props/db.properties")
@EntityScan(basePackages = "com.epam.esm.model.dto")
public class DaoConfig {

    @Value("${datasource.url}")
    private String DATASOURCE_URL;

    @Value("${datasource.driver}")
    private String DATASOURCE_DRIVER;

    @Value("${datasource.username}")
    private String DATASOURCE_USERNAME;

    @Value("${datasource.password}")
    private String DATASOURCE_PASSWORD;

    @Bean
    DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(DATASOURCE_URL);
        dataSource.setDriverClassName(DATASOURCE_DRIVER);
        dataSource.setUsername(DATASOURCE_USERNAME);
        dataSource.setPassword(DATASOURCE_PASSWORD);
        return dataSource;
    }
}
