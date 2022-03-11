package com.epam.esm.link;

import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.util.SecurityUtils;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.esm.dto.UserRoles.ADMIN_ROLE;
import static com.epam.esm.link.LinkUtils.RelType.*;
import static com.epam.esm.link.LinkUtils.buildLink;
import static com.epam.esm.link.LinkUtils.buildLinks;

@Component
public class OrderLinkBuilder implements LinkBuilder<OrderDto> {

    @Override
    public void attachLinks(OrderDto order, Link self) {
        List<Link> links = buildLinks(OrderController.class, order.getId(), FIND, FIND_ALL, SAVE);
        order.add(self).add(links);

        List<String> roles = SecurityUtils.getCurrentRoles();
        if (roles.contains(ADMIN_ROLE)) {
            Link delete = buildLink(OrderController.class, order.getId(), DELETE);
            order.add(delete);
        }
        LinkUtils.distinctLinks(order);
    }
}
