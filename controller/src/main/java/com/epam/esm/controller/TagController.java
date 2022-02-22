package com.epam.esm.controller;

import com.epam.esm.controller.dto.EntityMapper;
import com.epam.esm.controller.dto.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.TagDto;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.epam.esm.controller.link.HttpMethod.GET;
import static com.epam.esm.controller.link.LinkBuilder.RelType.*;
import static com.epam.esm.controller.link.LinkBuilder.*;
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
    public CollectionModel<Tag> getAllTags(PageDto page) {
        List<TagDto> tagsDto = tagService.findAll(page);
        List<Tag> tags = EntityMapper.mapTagsFromDto(tagsDto, Collectors.toList());

        for (Tag tag : tags) {
            Link self = buildSelf(this.getClass(), tag.getId(), FIND);
            List<Link> links = buildLinks(this.getClass(), tag.getId(), DELETE, SAVE);
            tag.add(self);
            tag.add(links);
        }

        Link self = buildSelf(this.getClass(), FIND_ALL);
        Link users = buildForName(UserController.class, FIND_ALL, "findUsers");
        Link certificates = buildForName(CertificateController.class, FIND_ALL, "findTags");
        Link topTag = linkTo(methodOn(this.getClass()).getTopTagOfTopUser()).withSelfRel().withType(GET);
        List<Link> others = Stream.of(self, users, certificates, topTag).collect(Collectors.toList());

        return CollectionModel.of(tags, others);
    }

    /**
     * Gets all tag objects.
     *
     * @param id *path variable of requesting tag
     * @return the tag object
     */
    @GetMapping(value = "/{id:^[0-9]+$}")
    public RepresentationModel<Tag> getTag(@PathVariable int id) {
        TagDto tagDto = tagService.find(id);
        Tag tag = EntityMapper.mapTagFromDto(tagDto);
        List<Link> links = buildLinks(this.getClass(), id, DELETE, SAVE);
        Link self = buildSelf(this.getClass(), id, FIND);
        tag.add(self);
        tag.add(links);
        return tag;
    }

    @GetMapping(value = "/top-of-user/{id:^[0-9]+$}")
    public RepresentationModel<Tag> getTopTagOfUser(@PathVariable int id) {
        TagDto tagDto = tagService.findMostUsedOfUser(id);
        Tag tag = EntityMapper.mapTagFromDto(tagDto);
        Link self = linkTo(methodOn(this.getClass()).getTopTagOfUser(id)).withSelfRel().withType(GET);
        List<Link> links = buildLinks(this.getClass(), id, DELETE, SAVE);
        tag.add(self);
        tag.add(links);
        return tag;
    }

    @GetMapping(value = "/top-of-top-user")
    public RepresentationModel<Tag> getTopTagOfTopUser() {
        TagDto tagDto = tagService.findMostUsedOfTopUser();
        Tag tag = EntityMapper.mapTagFromDto(tagDto);
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
    public RepresentationModel<Tag> addTag(TagDto tag) {
        TagDto savedTagDto = tagService.save(tag);
        Tag savedTag = EntityMapper.mapTagFromDto(savedTagDto);
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
    public RepresentationModel<Tag> deleteTag(@PathVariable int id) {
        TagDto tagDto = tagService.delete(id);
        Tag tag = EntityMapper.mapTagFromDto(tagDto);
        Link self = buildSelf(this.getClass(), DELETE);
        List<Link> links = buildLinks(this.getClass(), SAVE);
        tag.add(self);
        tag.add(links);
        return tag;
    }
}
