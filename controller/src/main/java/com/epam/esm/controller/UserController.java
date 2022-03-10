package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.View;
import com.epam.esm.service.UserService;
import com.epam.esm.util.JwtUtils;
import com.epam.esm.util.SecurityUtils;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.epam.esm.dto.UserRoles.ADMIN_ROLE;
import static com.epam.esm.link.HttpMethod.GET;
import static com.epam.esm.link.LinkBuilder.RelType.*;
import static com.epam.esm.link.LinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Find all users collection.
     *
     * @param page the page
     *             includes *limit* of elements on the page
     *             and *page* number from 0 to infinity
     * @return collection of all users
     **/
    @GetMapping
    @JsonView(View.Base.class)
    public CollectionModel<UserDto> findAllUsers(PageDto page) {
        List<UserDto> users = userService.findAll(page);
        UserDto principal = SecurityUtils.getCurrentUser();
        for (UserDto user : users) {
            Link self = buildSelf(this.getClass(), user.getId(), FIND);
            Link topTag = linkTo(methodOn(TagController.class).getTopTagOfUser(user.getId()))
                    .withRel("topTag")
                    .withType(GET);
            user.add(self);
            user.add(topTag);

            if (principal.getRoles().contains(ADMIN_ROLE)) {
                Link adminLinks = linkTo(methodOn(UserController.class).findUserOrders(user.getId(), new PageDto()))
                        .withRel("userOrders")
                        .withType(GET);
                user.add(adminLinks);
            }
        }

        CollectionModel<UserDto> collection = CollectionModel.of(users);
        if (!users.isEmpty()) {
            Link self = buildSelf(this.getClass(), FIND_ALL);
            collection.add(self);
        }
        return collection;
    }

    /**
     * Find user by id.
     *
     * @param id the user id
     * @return the user object
     */
    @GetMapping("/{id:^[0-9]+$}")
    @JsonView(View.Base.class)
    public RepresentationModel<UserDto> findUserById(@PathVariable int id) {
        UserDto user = userService.find(id);
        Link self = buildSelf(this.getClass(), id, FIND);
        user.add(self);
        return prepareUserModel(user);
    }

    /**
     * Find user orders collection.
     *
     * @param id   the id
     * @param page the page
     *             includes *limit* of elements on the page
     *             and *page* number from 0 to infinity
     * @return the orders collection
     */
    @GetMapping("/{id:^[0-9]+$}/orders")
    @JsonView(View.Base.class)
    public CollectionModel<OrderDto> findUserOrders(@PathVariable int id,
                                                    PageDto page) {
        List<OrderDto> orders = userService.findUserOrders(id, page);
        for (OrderDto order : orders) {
            Link self = linkTo(methodOn(this.getClass()).findUsersOrderById(id, order.getId()))
                    .withSelfRel()
                    .withType(GET);
            order.add(self);
        }

        CollectionModel<OrderDto> collection = CollectionModel.of(orders);
        if (!orders.isEmpty()) {
            String pageQuery = pageQuery(page);
            String query = prepareQuery(pageQuery);
            Link self = linkTo(methodOn(this.getClass()).findUserOrders(id, page)).slash(query).withSelfRel().withType(GET);
            Link topTag = linkTo(methodOn(TagController.class).getTopTagOfUser(id)).withRel("topTag").withType(GET);
            collection.add(self);
            collection.add(topTag);
        }
        return collection;
    }

    /**
     * Find user's order by id's.
     *
     * @param userId  the user id
     * @param orderId the order id
     * @return the order object
     */
    @GetMapping("/{userId:^[0-9]+$}/orders/{orderId:^[0-9]+$}")
    @JsonView(View.Base.class)
    public RepresentationModel<OrderDto> findUsersOrderById(@PathVariable int userId,
                                                            @PathVariable int orderId) {
        OrderDto order = userService.findUserOrder(userId, orderId);
        Link self = linkTo(methodOn(this.getClass()).findUsersOrderById(userId, orderId)).withSelfRel().withType(GET);
        List<Link> user = buildLinks(this.getClass(), userId, FIND);
        order.add(self);
        order.add(user);
        return order;
    }

    /**
     * Find top user with the highest cost of all orders.
     *
     * @return the user object
     */
    @GetMapping("/top")
    @JsonView(View.Base.class)
    public RepresentationModel<UserDto> findTopUser() {
        UserDto user = userService.findTopUser();
        Link self = linkTo(methodOn(UserController.class).findTopUser()).withSelfRel().withType(GET);
        user.add(self);
        return prepareUserModel(user);
    }

    @PostMapping
    public RepresentationModel<?> register(@RequestBody UserDto user) {
        UserDto savedUser = userService.save(user);
        String issuer = "/users";
        Map<String, String> tokens = JwtUtils.buildTokens(savedUser, issuer);
        Link self = buildSelf(this.getClass(), SAVE);
        return CollectionModel.of(tokens).add(self);
    }

    private RepresentationModel<UserDto> prepareUserModel(UserDto user) {
        Link topTag = linkTo(methodOn(TagController.class).getTopTagOfUser(user.getId()))
                .withRel("topTag")
                .withType(GET);
        user.add(topTag);

        List<String> roles = SecurityUtils.getCurrentRoles();
        if (roles.contains(ADMIN_ROLE)) {
            Link adminLinks = linkTo(methodOn(UserController.class).findUserOrders(user.getId(), new PageDto()))
                    .withRel("userOrders")
                    .withType(GET);
            user.add(adminLinks);
        }
        return user;
    }

}
