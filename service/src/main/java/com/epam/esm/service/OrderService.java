package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;

public interface OrderService extends BaseService<OrderDto> {
    OrderDto save(int certificateId, int userId);
}
