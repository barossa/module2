package com.epam.esm.model.config;

import com.epam.esm.model.dao.CertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.impl.CertificateDaoImpl;
import com.epam.esm.model.dao.impl.TagDaoImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@EntityScan(basePackages = "com.epam.esm.model.dto")
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
