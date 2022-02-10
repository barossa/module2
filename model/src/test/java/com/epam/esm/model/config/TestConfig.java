package com.epam.esm.model.config;

import com.epam.esm.model.dao.CertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.impl.CertificateDaoImpl;
import com.epam.esm.model.dao.impl.TagDaoImpl;
import com.epam.esm.model.dto.CertificateData;
import com.epam.esm.model.dto.TagData;
import com.epam.esm.model.util.CertificateTagsPropertyCombiner;
import com.epam.esm.model.util.PropertyCombiner;
import com.epam.esm.model.util.TagCertificatesPropertyCombiner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
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
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PropertyCombiner<TagData> tagDataPropertyCombiner() {
        return new TagCertificatesPropertyCombiner();
    }

    @Bean
    PropertyCombiner<CertificateData> certificateDataPropertyCombiner() {
        return new CertificateTagsPropertyCombiner();
    }

    /*@Bean
    public TagDao tagDao(EntityManager entityManager) {
        return new TagDaoImpl(entityManager);
    }

    @Bean
    public CertificateDao certificateDao(EntityManager entityManager) {
        return new CertificateDaoImpl(entityManager);
    }*/
}
