package com.epam.esm.config;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.impl.CertificateDaoImpl;
import com.epam.esm.dao.impl.TagDaoImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@TestConfiguration
@EntityScan(basePackages = "com.epam.esm.entity")
@PropertySource("classpath:test.properties")
public class TestConfig {
    private static final String DATASOURCE_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL";
    private static final String DATASOURCE_DRIVER = "org.h2.Driver";
    private static final String DATASOURCE_USER = "sa";
    private static final String DATASOURCE_PASSWORD = "sa";

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DATASOURCE_DRIVER);
        dataSource.setUrl(DATASOURCE_URL);
        dataSource.setUsername(DATASOURCE_USER);
        dataSource.setPassword(DATASOURCE_PASSWORD);
        return dataSource;
    }

    @Bean
    public TagDao tagDao(EntityManager manager) {
        return new TagDaoImpl(manager);
    }

    @Bean
    public CertificateDao certificateDao(EntityManager manager) {
        return new CertificateDaoImpl(manager);
    }
}
