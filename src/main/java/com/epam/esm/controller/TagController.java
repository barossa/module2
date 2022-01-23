package com.epam.esm.controller;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllTags() {
        List<Tag> tags = tagService.findAll();
        return ResponseEntity.ok(tags);
    }

    @GetMapping(value = "/{id:^[0-9]+$}")
    public ResponseEntity<Object> getTag(@PathVariable int id) {
        Tag tag = tagService.find(id);
        return ResponseEntity.ok(tag);
    }

    @PostMapping
    public ResponseEntity<Object> addTag(Tag tag) {
        Tag savedTag = tagService.save(tag);
        return ResponseEntity.ok(savedTag);
    }

    @DeleteMapping("/{id:^[0-9]+$}")
    public ResponseEntity<Object> deleteTag(@PathVariable int id) {
        tagService.delete(id);
        return ResponseEntity.ok(null);
    }
}
