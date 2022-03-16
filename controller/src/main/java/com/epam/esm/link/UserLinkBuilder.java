package com.epam.esm.link;

import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDto;
import com.epam.esm.util.SecurityUtils;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.esm.dto.UserRoles.ADMIN_ROLE;
import static com.epam.esm.link.HttpMethod.GET;
import static com.epam.esm.link.LinkUtils.RelType.FIND;
import static com.epam.esm.link.LinkUtils.buildLink;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserLinkBuilder implements LinkBuilder<UserDto> {
    @Override
    public void attachLinks(UserDto user, Link self) {
        Link findById = buildLink(this.getClass(), user.getId(), FIND);
        Link topTag = linkTo(methodOn(TagController.class).getTopTagOfUser(user.getId())).withSelfRel().withType(GET);
        Link topUser = linkTo(methodOn(UserController.class).findTopUser()).withSelfRel().withType(GET);
        Link topOfTop = linkTo(methodOn(TagController.class).getTopTagOfTopUser()).withSelfRel().withType(GET);
        user.add(self, findById, topUser, topTag, topOfTop);

        List<String> roles = SecurityUtils.getCurrentRoles();
        if(roles.contains(ADMIN_ROLE)){
            Link adminLink = linkTo(methodOn(UserController.class).findUserOrders(user.getId(), null)).withSelfRel().withType(GET);
            user.add(adminLink);
        }
        LinkUtils.distinctLinks(user);
    }
}
