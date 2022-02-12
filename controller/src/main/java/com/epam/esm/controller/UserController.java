package com.epam.esm.controller;

import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

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
        List<OrderDto> userOrders = userService.findUserOrders(id, page);
        return ResponseEntity.ok().body(userOrders);
    }

    @GetMapping("/{userId:^[0-9]+$}/orders/{orderId}")
    public ResponseEntity<Object> findUsersOrderById(@PathVariable int userId,
                                                 @PathVariable int orderId) {
        OrderDto userOrder = userService.findUserOrder(userId, orderId);
        return ResponseEntity.ok().body(userOrder);
    }
}
