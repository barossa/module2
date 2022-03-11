package com.epam.esm.controller;

import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.View;
import com.epam.esm.link.LinkBuilder;
import com.epam.esm.service.TagService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.epam.esm.link.HttpMethod.GET;
import static com.epam.esm.link.LinkUtils.RelType.*;
import static com.epam.esm.link.LinkUtils.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The type Tag rest controller.
 */
@RestController
@RequestMapping(value = "/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final LinkBuilder<TagDto> linkBuilder;

    /**
     * Gets all tag objects.
     *
     * @return the all tag objects
     */
    @GetMapping
    @JsonView(View.Base.class)
    public CollectionModel<TagDto> getAllTags(PageDto page) {
        List<TagDto> tags = tagService.findAll(page);
        for (TagDto tag : tags) {
            Link self = buildSelf(this.getClass(), tag.getId(), FIND);
            linkBuilder.attachLinks(tag, self);
        }

        String pageQuery = pageQuery(page);
        String query = prepareQuery(pageQuery);
        Link self = linkTo(this.getClass()).slash(query).withSelfRel().withType(GET);

        return attachToCollection(tags, self);
    }

    /**
     * Gets all tag objects.
     *
     * @param id *path variable of requesting tag
     * @return the tag object
     */
    @GetMapping(value = "/{id:^[0-9]+$}")
    @JsonView(View.Base.class)
    public RepresentationModel<TagDto> getTag(@PathVariable int id) {
        TagDto tag = tagService.find(id);
        Link self = buildSelf(this.getClass(), id, FIND);
        linkBuilder.attachLinks(tag, self);
        return tag;
    }

    @GetMapping(value = "/top-of-user/{id:^[0-9]+$}")
    @JsonView(View.Base.class)
    public RepresentationModel<TagDto> getTopTagOfUser(@PathVariable int id) {
        TagDto tag = tagService.findMostUsedOfUser(id);
        Link self = linkTo(methodOn(this.getClass()).getTopTagOfUser(id)).withSelfRel().withType(GET);
        linkBuilder.attachLinks(tag, self);
        return tag;
    }

    @GetMapping(value = "/top-of-top-user")
    @JsonView(View.Base.class)
    public RepresentationModel<TagDto> getTopTagOfTopUser() {
        TagDto tag = tagService.findMostUsedOfTopUser();
        Link self = linkTo(methodOn(this.getClass()).getTopTagOfTopUser()).withSelfRel().withType(GET);
        linkBuilder.attachLinks(tag, self);
        return tag;
    }

    /**
     * Add tag object.
     *
     * @param tag *the tag
     * @return the added tag object
     */
    @PostMapping
    @JsonView(View.Base.class)
    public RepresentationModel<TagDto> addTag(@RequestBody TagDto tag) {
        TagDto savedTag = tagService.save(tag);
        Link self = buildSelf(this.getClass(), SAVE);
        linkBuilder.attachLinks(savedTag, self);
        return savedTag;
    }

    /**
     * Delete tag by id.
     *
     * @param id *path variable of deleting tag
     * @return the delete response
     */
    @DeleteMapping("/{id:^[0-9]+$}")
    @JsonView(View.Base.class)
    public RepresentationModel<TagDto> deleteTag(@PathVariable int id) {
        TagDto tag = tagService.delete(id);
        Link self = buildSelf(this.getClass(), DELETE);
        linkBuilder.attachLinks(tag, self);
        return tag;
    }
}