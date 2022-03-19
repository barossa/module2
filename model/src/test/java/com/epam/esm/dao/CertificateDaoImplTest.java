package com.epam.esm.dao;

import com.epam.esm.config.TestConfig;
import com.epam.esm.entity.*;
import com.epam.esm.exception.DaoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.epam.esm.util.EntityUtils.UNDEFINED_ID;

@SpringJUnitConfig(TestConfig.class)
@Sql(scripts = "classpath:sql/db.sql")
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
class CertificateDaoImplTest {

    @Autowired
    private CertificateDao certificateDao;

    @Test
    void findTest() throws DaoException {
        Certificate certificate = certificateDao.find(15);
        Assertions.assertNotNull(certificate);
    }

    @Test
    void saveTest() throws DaoException {
        Certificate certificate = new Certificate(UNDEFINED_ID, "Certificate", "Description", new BigDecimal(100), 7L,
                LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet());
        Certificate actual = certificateDao.save(certificate);
        Assertions.assertNotEquals(0, actual.getId());
    }

    @Test
    void deleteTest() throws DaoException {
        int affectedObjects = certificateDao.delete(15);
        Assertions.assertNotEquals(0, affectedObjects);
    }

    @Test
    void findAllTest() throws DaoException {
        List<Certificate> actual = certificateDao.findAll(new Page());
        Assertions.assertFalse(actual.isEmpty());
    }

    @Test
    void updateTest() throws DaoException {
        Certificate certificate = certificateDao.find(16);
        certificate.setName("New name of certificate");
        Certificate updated = certificateDao.update(certificate);
        Assertions.assertEquals(certificate.getName(), updated.getName());
    }

    @Test
    void findByOptions() throws DaoException {
        Tag tag = new Tag(49, "another-tag", Collections.emptySet());
        List<Tag> tags = Collections.singletonList(tag);
        CertificateFilter filter = new CertificateFilter(tags, Collections.emptyList(), Collections.emptyList());
        Set<CertificateSort> sorts = new HashSet<>();
        List<Certificate> actual = certificateDao.findByFilter(filter, new Page(), sorts);
        Assertions.assertFalse(actual.isEmpty());
    }

}
