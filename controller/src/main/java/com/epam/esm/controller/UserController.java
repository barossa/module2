package com.epam.esm.controller;

import com.epam.esm.controller.dto.EntityMapper;
import com.epam.esm.controller.dto.Order;
import com.epam.esm.controller.dto.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.epam.esm.controller.link.HttpMethod.GET;
import static com.epam.esm.controller.link.HttpMethod.POST;
import static com.epam.esm.controller.link.LinkBuilder.RelType.FIND;
import static com.epam.esm.controller.link.LinkBuilder.RelType.FIND_ALL;
import static com.epam.esm.controller.link.LinkBuilder.*;
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
     */
    @GetMapping
    public CollectionModel<User> findAllUsers(PageDto page) {
        List<UserDto> usersDto = userService.findAll(page);
        List<User> users = EntityMapper.mapUsersFromDto(usersDto, Collectors.toList());

        for (User user : users) {
            Link self = buildSelf(this.getClass(), user.getId(), FIND);
            Link userOrders = linkTo(methodOn(UserController.class).findUserOrders(user.getId(), new PageDto()))
                    .withRel("userOrders")
                    .withType(GET);
            Link makeOrder = linkTo(methodOn(this.getClass()).makeOrder(user.getId(), 0))
                    .withRel("makeOrder")
                    .withType(POST);
            Link topTag = linkTo(methodOn(TagController.class).getTopTagOfUser(user.getId()))
                    .withRel("topTag")
                    .withType(GET);
            user.add(self);
            user.add(userOrders);
            user.add(makeOrder);
            user.add(topTag);
        }

        Link certificates = buildForName(CertificateController.class, FIND_ALL, "findCertificates");
        Link tags = buildForName(TagController.class, FIND_ALL, "findTags");
        Link self = buildSelf(this.getClass(), FIND_ALL);
        Link topUser = linkTo(methodOn(this.getClass()).findTopUser()).withRel("topUser").withType(GET);
        List<Link> others = Stream.of(self, tags, certificates, topUser).collect(Collectors.toList());

        return CollectionModel.of(users, others);
    }

    /**
     * Find user by id.
     *
     * @param id the user id
     * @return the user object
     */
    @GetMapping("/{id:^[0-9]+$}")
    public RepresentationModel<User> findUserById(@PathVariable int id) {
        UserDto userDto = userService.find(id);
        User user = EntityMapper.mapUserFromDto(userDto);
        Link self = buildSelf(this.getClass(), id, FIND);
        Link userOrders = linkTo(methodOn(UserController.class).findUserOrders(user.getId(), new PageDto()))
                .withRel("userOrders")
                .withType(GET);
        Link makeOrder = linkTo(methodOn(this.getClass()).makeOrder(id, 0))
                .withRel("makeOrder")
                .withType(POST);
        Link topTag = linkTo(methodOn(TagController.class).getTopTagOfUser(user.getId()))
                .withRel("topTag")
                .withType(GET);
        user.add(self);
        user.add(userOrders);
        user.add(makeOrder);
        user.add(topTag);
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
    public CollectionModel<Order> findUserOrders(@PathVariable int id,
                                                 PageDto page) {
        List<OrderDto> userOrdersDto = userService.findUserOrders(id, page);
        List<Order> orders = EntityMapper.mapOrdersFromDto(userOrdersDto, Collectors.toList());

        for (Order order : orders) {
            Link self = linkTo(methodOn(this.getClass()).findUsersOrderById(id, order.getId()))
                    .withSelfRel()
                    .withType(GET);
            Link makeOrder = linkTo(methodOn(this.getClass()).makeOrder(id, 0))
                    .withRel("makeOrder")
                    .withType(POST);
            order.add(self);
            order.add(makeOrder);
        }

        Link self = linkTo(methodOn(this.getClass()).findUserOrders(id, page)).withSelfRel().withType(GET);
        Link topTag = linkTo(methodOn(TagController.class).getTopTagOfUser(id))
                .withRel("topTag")
                .withType(GET);
        List<Link> others = Stream.of(self, topTag).collect(Collectors.toList());

        return CollectionModel.of(orders, others);
    }

    /**
     * Find user's order by id's.
     *
     * @param userId  the user id
     * @param orderId the order id
     * @return the order object
     */
    @GetMapping("/{userId:^[0-9]+$}/orders/{orderId:^[0-9]+$}")
    public RepresentationModel<Order> findUsersOrderById(@PathVariable int userId,
                                                         @PathVariable int orderId) {
        OrderDto userOrderDto = userService.findUserOrder(userId, orderId);
        Order order = EntityMapper.mapOrderFromDto(userOrderDto);

        Link self = linkTo(methodOn(this.getClass()).findUsersOrderById(userId, orderId))
                .withSelfRel()
                .withType(GET);
        Link makeOrder = linkTo(methodOn(this.getClass()).makeOrder(userId, 0))
                .withRel("makeOrder")
                .withType(POST);
        List<Link> user = buildLinks(this.getClass(), userId, FIND);
        order.add(self);
        order.add(makeOrder);
        order.add(user);

        return order;
    }

    /**
     * Find top user with the highest cost of all orders.
     *
     * @return the user object
     */
    @GetMapping("/top")
    public RepresentationModel<User> findTopUser() {
        UserDto userDto = userService.findTopUser();
        User user = EntityMapper.mapUserFromDto(userDto);
        Link self = linkTo(methodOn(UserController.class).findTopUser()).withSelfRel().withType(GET);
        Link userOrders = linkTo(methodOn(UserController.class).findUserOrders(user.getId(), new PageDto()))
                .withRel("userOrders")
                .withType(GET);
        Link makeOrder = linkTo(methodOn(this.getClass()).makeOrder(user.getId(), 0))
                .withRel("makeOrder")
                .withType(POST);
        Link topTag = linkTo(methodOn(TagController.class).getTopTagOfUser(user.getId()))
                .withRel("topTag")
                .withType(GET);
        user.add(self);
        user.add(userOrders);
        user.add(makeOrder);
        user.add(topTag);
        return user;
    }

    /**
     * Make order for user with id.
     *
     * @param userId        the user id
     * @param certificateId the certificate id
     * @return the order object
     */
    @PostMapping("/{userId:^[0-9]+$}/orders")
    public RepresentationModel<Order> makeOrder(@PathVariable int userId,
                                                @RequestParam(defaultValue = "0") int certificateId) {
        OrderDto orderDto = userService.makeOrder(userId, certificateId);
        Order order = EntityMapper.mapOrderFromDto(orderDto);
        Link self = linkTo(methodOn(UserController.class).makeOrder(userId, certificateId)).withSelfRel().withType(GET);
        Link userOrders = linkTo(methodOn(UserController.class).findUserOrders(orderDto.getUser().getId(), new PageDto()))
                .withRel("userOrders")
                .withType(GET);
        List<Link> links = buildLinks(this.getClass(), orderDto.getId(), FIND);
        Link topTag = linkTo(methodOn(TagController.class).getTopTagOfUser(orderDto.getUser().getId()))
                .withRel("topTag")
                .withType(GET);
        order.add(self);
        order.add(userOrders);
        order.add(links);
        order.add(topTag);
        return order;
    }
}
