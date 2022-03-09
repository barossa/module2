package com.epam.esm.controller;

import com.epam.esm.dto.*;
import com.epam.esm.exception.extend.OrderNotFoundException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.epam.esm.dto.UserRoles.ADMIN_ROLE;
import static com.epam.esm.link.HttpMethod.GET;
import static com.epam.esm.link.LinkBuilder.RelType.DELETE;
import static com.epam.esm.link.LinkBuilder.RelType.FIND;
import static com.epam.esm.link.LinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    @JsonView(View.Base.class)
    public CollectionModel<OrderDto> findAll(PageDto page, Authentication authentication) {
        UserDto user = (UserDto) authentication.getPrincipal();
        List<OrderDto> orders;
        if(user.getRoles().contains(ADMIN_ROLE)){
            orders = orderService.findAll(page);
        }else {
            orders = userService.findUserOrders(user.getId(), page);
        }
        CollectionModel<OrderDto> collection = CollectionModel.of(orders);
        if (!orders.isEmpty()) {
            String pageQuery = pageQuery(page);
            String query = prepareQuery(pageQuery);
            Link self = linkTo(this.getClass()).slash(query).withSelfRel().withType(GET);
            collection.add(self);
        }
        return collection;
    }

    @GetMapping("/{id:^[0-9]+$}")
    @JsonView(View.Base.class)
    public RepresentationModel<OrderDto> findById(@PathVariable int id, Authentication authentication) {
        OrderDto order = orderService.find(id);
        UserDto user = (UserDto) authentication.getPrincipal();
        if(order.getUser().getId() != user.getId() && !user.getRoles().contains(ADMIN_ROLE)){
            throw new OrderNotFoundException();
        }
        Link self = buildSelf(this.getClass(), FIND);
        order.add(self);
        return order;
    }

    @DeleteMapping("/{id:^[0-9]+$}")
    @JsonView(View.Base.class)
    public RepresentationModel<OrderDto> deleteById(@PathVariable int id) {
        OrderDto order = orderService.delete(id);
        Link self = buildSelf(this.getClass(), id, DELETE);
        order.add(self);
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
    public RepresentationModel<OrderDto> makeOrder(@RequestBody OrderParams params, Authentication authentication) {
        UserDto user = (UserDto) authentication.getPrincipal();
        OrderDto order;
        if(user.getRoles().contains(ADMIN_ROLE)){
            order = orderService.save(params.getCertificateId(), params.getUserId());
        }else{
            order = orderService.save(params.getCertificateId(), user.getId());
        }

        Link self = linkTo(this.getClass()).withSelfRel().withType(GET);
        Link userOrders = linkTo(methodOn(UserController.class).findUserOrders(order.getUser().getId(), new PageDto()))
                .withRel("userOrders").withType(GET);
        List<Link> links = buildLinks(this.getClass(), order.getId(), FIND);
        Link topTag = linkTo(methodOn(TagController.class).getTopTagOfUser(order.getUser().getId()))
                .withRel("topTag").withType(GET);
        order.add(self);
        order.add(userOrders);
        order.add(links);
        order.add(topTag);
        return order;
    }

}
