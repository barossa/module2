package com.epam.esm.controller;

import com.epam.esm.dto.*;
import com.epam.esm.exception.extend.OrderNotFoundException;
import com.epam.esm.link.LinkBuilder;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.SecurityUtils;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.epam.esm.dto.UserRoles.ADMIN_ROLE;
import static com.epam.esm.link.HttpMethod.GET;
import static com.epam.esm.link.LinkUtils.RelType.DELETE;
import static com.epam.esm.link.LinkUtils.RelType.FIND;
import static com.epam.esm.link.LinkUtils.*;
import static com.epam.esm.util.EntityUtils.UNDEFINED_ID;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final LinkBuilder<OrderDto> linkBuilder;

    @GetMapping
    @JsonView(View.Base.class)
    public CollectionModel<OrderDto> findAll(PageDto page) {
        UserDto user = SecurityUtils.getCurrentUser();
        List<OrderDto> orders;
        if (user.getRoles().contains(ADMIN_ROLE)) {
            orders = orderService.findAll(page);
        } else {
            orders = userService.findUserOrders(user.getId(), page);
        }
        orders.forEach(order -> {
            Link self = buildSelf(this.getClass(), order.getId(), FIND);
            linkBuilder.attachLinks(order, self);
        });

        String pageQuery = pageQuery(page);
        String query = prepareQuery(pageQuery);
        Link self = linkTo(this.getClass()).slash(query).withSelfRel().withType(GET);

        return attachToCollection(orders, self);
    }

    @GetMapping("/{id:^[0-9]+$}")
    @JsonView(View.Base.class)
    public RepresentationModel<OrderDto> findById(@PathVariable int id) {
        OrderDto order = orderService.find(id);
        UserDto user = SecurityUtils.getCurrentUser();
        if (order.getUser().getId() != user.getId() && !user.getRoles().contains(ADMIN_ROLE)) {
            throw new OrderNotFoundException();
        }
        Link self = buildSelf(this.getClass(), FIND);
        linkBuilder.attachLinks(order, self);
        return order;
    }

    @DeleteMapping("/{id:^[0-9]+$}")
    @JsonView(View.Base.class)
    public RepresentationModel<OrderDto> deleteById(@PathVariable int id) {
        OrderDto order = orderService.delete(id);
        Link self = buildSelf(this.getClass(), id, DELETE);
        linkBuilder.attachLinks(order, self);
        return order;
    }

    /**
     * Make order for user with id.
     *
     * @param userId        the user id
     * @param certificateId the certificate id
     * @return the order object
     */
    @PostMapping
    @JsonView(View.Base.class)
    public RepresentationModel<OrderDto> makeOrder(@RequestBody OrderParams params) {
        UserDto user = SecurityUtils.getCurrentUser();
        OrderDto order;
        if (user.getRoles().contains(ADMIN_ROLE)) {
            if (params.getUserId() == UNDEFINED_ID) {
                params.setUserId(user.getId());
            }
            order = orderService.save(params.getCertificateId(), params.getUserId());
        } else {
            order = orderService.save(params.getCertificateId(), user.getId());
        }

        Link self = linkTo(this.getClass()).withSelfRel().withType(GET);
        linkBuilder.attachLinks(order, self);
        return order;
    }

}
