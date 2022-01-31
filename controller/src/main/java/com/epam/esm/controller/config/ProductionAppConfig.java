package com.epam.esm.controller.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;


@PropertySource({"classpath:props/db.properties", "classpath:props/prod.properties"})
public class ProductionAppConfig {

    @Value("${pool.initialSize}")
    private int POOL_INITIAL_SIZE;

    @Value("${pool.maxSize}")
    private int POOL_MAX_SIZE;

    @Value("${datasource.url}")
    private String DATASOURCE_URL;

    @Value("${datasource.driver}")
    private String DATASOURCE_DRIVER;

    @Value("${datasource.username}")
    private String USERNAME;

    @Value("${datasource.password}")
    private String PASSWORD;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setInitialSize(POOL_INITIAL_SIZE);
        dataSource.setMaxTotal(POOL_MAX_SIZE);
        dataSource.setUrl(DATASOURCE_URL);
        dataSource.setDriverClassName(DATASOURCE_DRIVER);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        return dataSource;
    }

}
