package com.epam.esm.model.dao;

import com.epam.esm.model.config.TestConfig;
import com.epam.esm.model.dto.*;
import com.epam.esm.model.exception.DaoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.epam.esm.model.util.EntityUtils.UNDEFINED_ID;

@SpringJUnitConfig(TestConfig.class)
@Sql(scripts = "classpath:sql/db.sql")
@ExtendWith(SpringExtension.class)
public class CertificateDaoImplTest {

    @Autowired
    private CertificateDao certificateDao;

    @Test
    public void findTest() throws DaoException {
        CertificateData certificateData = certificateDao.find(15);
        Assertions.assertNotNull(certificateData);
    }

    @Test
    public void saveTest() throws DaoException {
        CertificateData certificateData = new CertificateData(UNDEFINED_ID, "Certificate", "Description", new BigDecimal(100), 7L,
                LocalDateTime.now(), LocalDateTime.now(), Collections.emptySet());
        CertificateData actualData = certificateDao.save(certificateData);
        Assertions.assertNotEquals(0, actualData.getId());
    }

    @Test
    public void deleteTest() throws DaoException {
        int affectedObjects = certificateDao.delete(15);
        Assertions.assertNotEquals(0, affectedObjects);
    }

    @Test
    public void findAllTest() throws DaoException {
        List<CertificateData> actualData = certificateDao.findAll(new PageData());
        Assertions.assertFalse(actualData.isEmpty());
    }

    @Test
    public void updateTest() throws DaoException {
        CertificateData certificateData = certificateDao.find(16);
        certificateData.setName("New name of certificate");
        CertificateData updated = certificateDao.update(certificateData);
        Assertions.assertEquals(certificateData, updated);
    }

    @Test
    public void findByOptions() throws DaoException {
        TagData tagData = new TagData(49, "another-tag", Collections.emptySet());
        List<TagData> tags = Collections.singletonList(tagData);
        CertificateFilter filter = new CertificateFilter(tags, Collections.emptyList(), Collections.emptyList());
        Set<CertificateSort> sorts = new HashSet<>();
        List<CertificateData> actualData = certificateDao.findByFilter(filter, new PageData(), sorts);
        Assertions.assertFalse(actualData.isEmpty());
    }

}
