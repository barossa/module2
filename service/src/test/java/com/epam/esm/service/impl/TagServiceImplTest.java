package com.epam.esm.service.impl;

import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.TagDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    private TagDto tagDto;
    private List<TagDto> tagDtos;

    @Mock
    private TagServiceImpl tagService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(tagService);
        tagDto = new TagDto(1, "Tag", Collections.emptySet());
        tagDtos = new ArrayList<>();
        tagDtos.add(tagDto);
    }

    @Test
    void findTest() {
        when(tagService.find(anyInt())).thenReturn(tagDto);
        TagDto actualTag = tagService.find(1);
        Assertions.assertEquals(tagDto, actualTag);
    }

    @Test
    void findAllTest() {
        when(tagService.findAll(any(PageDto.class))).thenReturn(tagDtos);
        List<TagDto> actualTags = tagService.findAll(new PageDto());
        Assertions.assertEquals(tagDtos, actualTags);
    }

    @Test
    void saveTest() {
        when(tagService.save(any(TagDto.class))).thenReturn(tagDto);
        TagDto savedTag = tagService.save(tagDto);
        Assertions.assertEquals(tagDto, savedTag);
    }

    @Test
    void deleteTest() {
        when(tagService.delete(anyInt())).thenReturn(tagDto);
        TagDto actualDto = tagService.delete(1);
        Assertions.assertNotEquals(0, actualDto.getId());
    }

}
