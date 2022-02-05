package com.epam.esm.controller;

import com.epam.esm.controller.dto.EntityMapper;
import com.epam.esm.controller.dto.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Tag rest controller.
 */
@RestController
@RequestMapping(value = "/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    /**
     * Gets all tag objects.
     *
     * @return the all tag objects
     */
    @GetMapping
    public ResponseEntity<Object> getAllTags() {
        List<TagDto> tagsDto = tagService.findAll();
        List<Tag> tags = EntityMapper.mapTagsFromDto(tagsDto, Collectors.toList());
        return ResponseEntity.ok(tags);
    }

    /**
     * Gets all tag objects.
     *
     * @param id *path variable of requesting tag
     * @return the tag object
     */
    @GetMapping(value = "/{id:^[0-9]+$}")
    public ResponseEntity<Object> getTag(@PathVariable int id) {
        TagDto tagDto = tagService.find(id);
        Tag tag = EntityMapper.mapTagFromDto(tagDto);
        return ResponseEntity.ok(tag);
    }

    /**
     * Add tag object.
     *
     * @param tag *the tag
     * @return the added tag object
     */
    @PostMapping
    public ResponseEntity<Object> addTag(TagDto tag) {
        TagDto savedTagDto = tagService.save(tag);
        Tag savedTag = EntityMapper.mapTagFromDto(savedTagDto);
        return ResponseEntity.ok(savedTag);
    }

    /**
     * Delete tag by id.
     *
     * @param id *path variable of deleting tag
     * @return the delete response
     */
    @DeleteMapping("/{id:^[0-9]+$}")
    public ResponseEntity<Object> deleteTag(@PathVariable int id) {
        TagDto tagDto = tagService.delete(id);
        Tag tag = EntityMapper.mapTagFromDto(tagDto);
        return ResponseEntity.ok(tag);
    }
}
