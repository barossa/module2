package com.epam.esm.model.dao;

import com.epam.esm.model.config.TestConfig;
import com.epam.esm.model.dto.TagData;
import com.epam.esm.model.exception.DaoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.epam.esm.model.util.EntityUtils.UNDEFINED_ID;

@SpringJUnitConfig(TestConfig.class)
@Sql(scripts = "classpath:sql/db.sql")
@ExtendWith(SpringExtension.class)
public class TagDaoImplTest {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private TagDao tagDao;

    @Test
    public void findTest() throws DaoException {
        TagData tagData = tagDao.find(58);
        Assertions.assertNotNull(tagData);
    }

    @Test
    public void saveTest() throws DaoException {
        TagData tagData = new TagData();
        tagData.setName("TagName");
        tagData.setCertificates(Collections.emptySet());
        TagData actualData = tagDao.save(tagData);
        Assertions.assertNotEquals(UNDEFINED_ID, actualData.getId());
    }

    @Test
    public void deleteTest() throws DaoException {
        int affectedObjects = tagDao.delete(58);
        Assertions.assertNotEquals(0, affectedObjects);
    }

    @Test
    public void findAllTest() throws DaoException {
        List<TagData> tagsData = tagDao.findAll();
        Assertions.assertFalse(tagsData.isEmpty());
    }

    @Test
    public void findByNameTest() throws DaoException {
        TagData actualTag = tagDao.findByName("inserted-tag");
        Assertions.assertNotNull(actualTag);
    }

    @Test
    public void findByCertificateIdTest() throws DaoException {
        Set<TagData> actualData = tagDao.findByCertificateId(15);
        Assertions.assertFalse(actualData.isEmpty());
    }

    @Test
    public void saveAllTest() throws DaoException {
        Set<TagData> tagData = new HashSet<>();
        tagData.add(new TagData(UNDEFINED_ID, "first-tag", Collections.emptySet()));
        tagData.add(new TagData(UNDEFINED_ID, "second-tag", Collections.emptySet()));
        tagData.add(new TagData(UNDEFINED_ID, "third-tag", Collections.emptySet()));
        Set<TagData> actualData = tagDao.saveAll(tagData);
        Assertions.assertFalse(actualData.isEmpty());
    }
}
