package com.epam.esm.controller;

import com.epam.esm.controller.dto.EntityMapper;
import com.epam.esm.controller.dto.Order;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Object> findAllUsers(PageDto page) {
        List<UserDto> usersDto = userService.findAll(page);
        return ResponseEntity.ok(usersDto);
    }

    @GetMapping("/{id:^[0-9]+$}")
    public ResponseEntity<Object> findUserById(@PathVariable int id) {
        UserDto userDto = userService.find(id);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{id:^[0-9]+$}/orders")
    public ResponseEntity<Object> findUserOrders(@PathVariable int id,
                                                 PageDto page) {
        List<OrderDto> userOrdersDto = userService.findUserOrders(id, page);
        List<Order> orders = EntityMapper.mapOrdersFromDto(userOrdersDto, Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{userId:^[0-9]+$}/orders/{orderId:^[0-9]+$}")
    public ResponseEntity<Object> findUsersOrderById(@PathVariable int userId,
                                                     @PathVariable int orderId) {
        OrderDto userOrderDto = userService.findUserOrder(userId, orderId);
        Order order = EntityMapper.mapOrderFromDto(userOrderDto);
        return ResponseEntity.ok(order);
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
