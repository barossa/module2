package com.epam.esm.controller.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ProductionAppConfig {
    private static final int POOL_INITIAL_SIZE = 2;
    private static final int POOL_MAX_SIZE = 5;

    private static final String DATASOURCE_URL = "jdbc:mysql://localhost:3306/rest-basics";
    private static final String DATASOURCE_DRIVER = "com.mysql.jdbc.Driver";
    private static final String USERNAME = "antoxa";
    private static final String PASSWORD = "31102012";


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
