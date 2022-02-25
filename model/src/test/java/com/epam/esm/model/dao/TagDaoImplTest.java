package com.epam.esm.model.dao;

import com.epam.esm.dao.TagDao;
import com.epam.esm.model.config.TestConfig;
import com.epam.esm.entity.Page;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.epam.esm.util.EntityUtils.UNDEFINED_ID;

@SpringJUnitConfig(TestConfig.class)
@Sql(scripts = "classpath:sql/db.sql")
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
public class TagDaoImplTest {

    @Autowired
    private TagDao tagDao;

    @Test
    public void findTest() throws DaoException {
        Tag tagData = tagDao.find(58);
        Assertions.assertNotNull(tagData);
    }

    @Test
    public void saveTest() throws DaoException {
        Tag tagData = new Tag();
        tagData.setName("TagName");
        tagData.setCertificates(Collections.emptySet());
        Tag actualData = tagDao.save(tagData);
        Assertions.assertNotEquals(UNDEFINED_ID, actualData.getId());
    }

    @Test
    public void deleteTest() throws DaoException {
        int affectedObjects = tagDao.delete(58);
        Assertions.assertNotEquals(0, affectedObjects);
    }

    @Test
    public void findAllTest() throws DaoException {
        List<Tag> tagsData = tagDao.findAll(new Page());
        Assertions.assertFalse(tagsData.isEmpty());
    }

    @Test
    public void findByNameTest() throws DaoException {
        Tag actualTag = tagDao.findByName("inserted-tag");
        Assertions.assertNotNull(actualTag);
    }

    @Test
    public void saveAllTest() throws DaoException {
        Set<Tag> tagData = new HashSet<>();
        tagData.add(new Tag(UNDEFINED_ID, "first-tag", Collections.emptySet()));
        tagData.add(new Tag(UNDEFINED_ID, "second-tag", Collections.emptySet()));
        tagData.add(new Tag(UNDEFINED_ID, "third-tag", Collections.emptySet()));
        Set<Tag> actualData = tagDao.saveAll(tagData);
        Assertions.assertFalse(actualData.isEmpty());
    }
}
