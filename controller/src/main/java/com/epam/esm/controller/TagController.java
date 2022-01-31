package com.epam.esm.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Tag rest controller.
 */
@RestController
@RequestMapping(value = "/tags")
public class TagController {
    private final TagService tagService;

    /**
     * Instantiates a new Tag controller.
     *
     * @param tagService the tag service
     */
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Gets all tag objects.
     *
     * @return the all tag objects
     */
    @GetMapping
    public ResponseEntity<Object> getAllTags() {
        List<TagDto> tags = tagService.findAll();
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
        TagDto tag = tagService.find(id);
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
        TagDto savedTag = tagService.save(tag);
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
        tagService.delete(id);
        return ResponseEntity.ok(null);
    }
}
