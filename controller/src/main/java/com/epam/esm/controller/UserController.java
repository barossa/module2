package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.View;
import com.epam.esm.link.LinkBuilder;
import com.epam.esm.service.UserService;
import com.epam.esm.util.JwtUtils;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.epam.esm.link.HttpMethod.GET;
import static com.epam.esm.link.LinkUtils.RelType.*;
import static com.epam.esm.link.LinkUtils.*;
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
    private final LinkBuilder<UserDto> userLinkBuilder;
    private final LinkBuilder<OrderDto> orderLinkBuilder;

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
        for (UserDto user : users) {
            Link self = buildSelf(this.getClass(), user.getId(), FIND);
            userLinkBuilder.attachLinks(user, self);
        }
        Link self = buildSelf(this.getClass(), FIND_ALL);
        return attachToCollection(users, self);
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
        userLinkBuilder.attachLinks(user, self);
        return user;
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
            Link self = linkTo(methodOn(this.getClass()).findUsersOrderById(id, order.getId())).withSelfRel().withType(GET);
            orderLinkBuilder.attachLinks(order, self);
        }

        String pageQuery = pageQuery(page);
        String query = prepareQuery(pageQuery);
        Link self = linkTo(methodOn(this.getClass()).findUserOrders(id, page)).slash(query).withSelfRel().withType(GET);
        return attachToCollection(orders, self);
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
        userLinkBuilder.attachLinks(user, self);
        return user;
    }

    @PostMapping
    public RepresentationModel<?> register(@RequestBody UserDto user) {
        UserDto savedUser = userService.save(user);
        Map<String, String> tokens = JwtUtils.buildTokens(savedUser);
        Link self = buildSelf(this.getClass(), SAVE);
        return RepresentationModel.of(tokens).add(self);
    }
}
