package com.epam.esm.controller;

import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.View;
import com.epam.esm.service.TagService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.epam.esm.link.HttpMethod.GET;
import static com.epam.esm.link.LinkBuilder.RelType.*;
import static com.epam.esm.link.LinkBuilder.*;
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
            List<Link> links = buildLinks(this.getClass(), tag.getId(), DELETE, SAVE);
            tag.add(self);
            tag.add(links);
        }
        CollectionModel<TagDto> collection = CollectionModel.of(tags);
        if (!tags.isEmpty()) {
            String pageQuery = pageQuery(page);
            String query = prepareQuery(pageQuery);
            Link self = linkTo(this.getClass()).slash(query).withSelfRel().withType(GET);
            collection.add(self);
        }

        return collection;
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
        List<Link> links = buildLinks(this.getClass(), id, DELETE, SAVE);
        Link self = buildSelf(this.getClass(), id, FIND);
        tag.add(self);
        tag.add(links);
        return tag;
    }

    @GetMapping(value = "/top-of-user/{id:^[0-9]+$}")
    @JsonView(View.Base.class)
    public RepresentationModel<TagDto> getTopTagOfUser(@PathVariable int id) {
        TagDto tag = tagService.findMostUsedOfUser(id);
        Link self = linkTo(methodOn(this.getClass()).getTopTagOfUser(id)).withSelfRel().withType(GET);
        List<Link> links = buildLinks(this.getClass(), id, DELETE, SAVE);
        tag.add(self);
        tag.add(links);
        return tag;
    }

    @GetMapping(value = "/top-of-top-user")
    @JsonView(View.Base.class)
    public RepresentationModel<TagDto> getTopTagOfTopUser() {
        TagDto tag = tagService.findMostUsedOfTopUser();
        Link self = linkTo(methodOn(this.getClass()).getTopTagOfTopUser()).withSelfRel().withType(GET);
        List<Link> links = buildLinks(this.getClass(), tag.getId(), FIND, DELETE, SAVE);
        tag.add(self);
        tag.add(links);
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
        List<Link> links = buildLinks(this.getClass(), savedTag.getId(), FIND, DELETE);
        savedTag.add(self);
        savedTag.add(links);
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
        List<Link> links = buildLinks(this.getClass(), SAVE);
        tag.add(self);
        tag.add(links);
        return tag;
    }
}