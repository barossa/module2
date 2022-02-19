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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.epam.esm.controller.dto.HttpMethod.GET;
import static com.epam.esm.controller.dto.HttpMethod.POST;
import static com.epam.esm.controller.dto.LinkBuilder.RelType.FIND;
import static com.epam.esm.controller.dto.LinkBuilder.RelType.FIND_ALL;
import static com.epam.esm.controller.dto.LinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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

    @GetMapping("/top")
    public ResponseEntity<Object> findTopUser() {
        UserDto userDto = userService.findTopUser();
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/{userId:^[0-9]+$}/orders")
    public ResponseEntity<Object> makeOrder(@PathVariable int userId,
                                            @RequestParam(defaultValue = "0") int certificateId) {
        OrderDto orderDto = userService.makeOrder(userId, certificateId);
        Order order = EntityMapper.mapOrderFromDto(orderDto);
        return ResponseEntity.ok().body(order);
    }
}
