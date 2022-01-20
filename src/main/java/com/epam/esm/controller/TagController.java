package com.epam.esm.controller;

import com.epam.esm.controller.error.ErrorResponseBuilder;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.exception.DaoException;
import com.epam.esm.model.exception.ServiceException;
import com.epam.esm.service.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.controller.error.ErrorResponseBuilder.*;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping(value = "/tags")
public class TagController {
    private static final Logger logger = LogManager.getLogger(TagController.class);

    private final TagService tagService;
    private final ErrorResponseBuilder errorBuilder;

    public TagController(TagService tagService, ErrorResponseBuilder errorResponseBuilder) {
        this.tagService = tagService;
        this.errorBuilder = errorResponseBuilder;
    }

    @GetMapping
    public ResponseEntity<Object> getAllTags() {
        try {
            List<Tag> tags = tagService.findAll();
            return ResponseEntity.ok(tags);

        } catch (ServiceException e) {
            logger.error("Error occurred loading all certificates: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(errorBuilder.build(INTERNAL_ERROR));
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getTag(@PathVariable int id) {
        try {
            Optional<Tag> tagOptional = tagService.find(id);
            if (tagOptional.isPresent()) {
                return ResponseEntity.ok(tagOptional.get());
            } else {
                return ResponseEntity.status(NOT_FOUND).body(errorBuilder.build(OBJECT_NOT_FOUND));
            }
        } catch (ServiceException e) {
            logger.error("Error occurred getting tag: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(errorBuilder.build(INTERNAL_ERROR));
        }
    }

    @PostMapping
    public ResponseEntity<Object> addTag(Tag tag){
        try{
            Optional<Tag> byName = tagService.findByName(tag.getName());
            if(byName.isPresent()){
                return ResponseEntity.status(CONFLICT).body(errorBuilder.build(OBJECT_ALREADY_EXISTS));
            }
            Optional<Tag> tagOptional = tagService.save(tag);
            if(tagOptional.isPresent()){
                return ResponseEntity.ok(tagOptional.get());
            }else{
                return ResponseEntity.internalServerError().body(errorBuilder.build(OBJECT_POSTING_ERROR));
            }
        }catch (ServiceException e){
            logger.error("Error occurred adding new tag: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(errorBuilder.build(INTERNAL_ERROR));
        }
    }
}
