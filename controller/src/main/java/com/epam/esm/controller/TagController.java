package com.epam.esm.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
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
        List<TagDto> tags = tagService.findAll();
        return ResponseEntity.ok(tags);
    }

    @GetMapping(value = "/{id:^[0-9]+$}")
    public ResponseEntity<Object> getTag(@PathVariable int id) {
        TagDto tag = tagService.find(id);
        return ResponseEntity.ok(tag);
    }

    @PostMapping
    public ResponseEntity<Object> addTag(TagDto tag) {
        TagDto savedTag = tagService.save(tag);
        return ResponseEntity.ok(savedTag);
    }

    @DeleteMapping("/{id:^[0-9]+$}")
    public ResponseEntity<Object> deleteTag(@PathVariable int id) {
        tagService.delete(id);
        return ResponseEntity.ok(null);
    }
}
