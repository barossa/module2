package com.epam.esm.link;

import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDto;
import com.epam.esm.util.SecurityUtils;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.esm.dto.UserRoles.ADMIN_ROLE;
import static com.epam.esm.link.LinkUtils.RelType.*;
import static com.epam.esm.link.LinkUtils.buildLink;
import static com.epam.esm.link.LinkUtils.buildLinks;

@Component
public class TagLinkBuilder implements LinkBuilder<TagDto> {

    @Override
    public void attachLinks(TagDto tag, Link self) {
        Link view = buildLink(TagController.class, tag.getId(), FIND);
        tag.add(self, view);
        List<String> roles = SecurityUtils.getCurrentRoles();
        if (roles.contains(ADMIN_ROLE)) {
            List<Link> adminLinks = buildLinks(this.getClass(), tag.getId(), DELETE, SAVE);
            tag.add(adminLinks);
        }
        LinkUtils.distinctLinks(tag);
    }
}
