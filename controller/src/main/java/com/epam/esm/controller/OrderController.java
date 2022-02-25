package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderParams;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.View;
import com.epam.esm.service.OrderService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    @JsonView(View.Base.class)
    public CollectionModel<OrderDto> findAll(PageDto page) {
        List<OrderDto> orders = orderService.findAll(page);
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
    public RepresentationModel<OrderDto> findById(@PathVariable int id) {
        OrderDto order = orderService.find(id);
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
    public RepresentationModel<OrderDto> makeOrder(@RequestBody OrderParams params) {

        OrderDto orderDto = orderService.save(params.getCertificateId(), params.getUserId());
        Link self = linkTo(this.getClass()).withSelfRel().withType(GET);
        Link userOrders = linkTo(methodOn(UserController.class).findUserOrders(orderDto.getUser().getId(), new PageDto()))
                .withRel("userOrders").withType(GET);
        List<Link> links = buildLinks(this.getClass(), orderDto.getId(), FIND);
        Link topTag = linkTo(methodOn(TagController.class).getTopTagOfUser(orderDto.getUser().getId()))
                .withRel("topTag").withType(GET);
        orderDto.add(self);
        orderDto.add(userOrders);
        orderDto.add(links);
        orderDto.add(topTag);
        return orderDto;
    }

}
