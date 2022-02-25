package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.*;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Page;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.extend.*;
import com.epam.esm.service.OrderService;
import com.epam.esm.validator.IdValidator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.util.EntityUtils.UNDEFINED_ID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

    private final OrderDao orderDao;
    private final CertificateDao certificateDao;
    private final UserDao userDao;

    private final IdValidator idValidator;

    @Override
    public OrderDto find(int id) {
        try {
            Order order = orderDao.find(id);
            if (order == null) {
                throw new OrderNotFoundException();
            }

            return DtoMapper.mapOrderFromData(order);

        } catch (DaoException e) {
            logger.error("Can't find order by id", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<OrderDto> findAll(PageDto page) {
        try {
            Page pageData = DtoMapper.mapPageToData(page);
            List<Order> orders = orderDao.findAll(pageData);
            return DtoMapper.mapOrdersFromData(orders, Collectors.toList());

        } catch (DaoException e) {
            logger.error("Can't find all orders", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public OrderDto save(OrderDto order) {
        try {
            List<String> errors = validateOrder(order);
            if (!errors.isEmpty()) {
                throw new ObjectValidationException(errors);
            }

            User userData = userDao.find(order.getUser().getId());
            if (userData == null) {
                throw new UserNotFoundException();
            }
            Certificate certificateData = certificateDao.find(order.getCertificate().getId());
            if (certificateData == null) {
                throw new CertificateNotFoundException();
            }
            Order orderData = new Order(UNDEFINED_ID,
                    LocalDateTime.now(),
                    certificateData.getPrice(),
                    userData,
                    certificateData);
            orderDao.save(orderData);
            return DtoMapper.mapOrderFromData(orderData);

        } catch (DaoException e) {
            logger.error("Can't made order", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public OrderDto save(int certificateId, int userId) {
        UserDto user = new UserDto();
        user.setId(userId);
        CertificateDto certificate = new CertificateDto();
        certificate.setId(certificateId);
        OrderDto order = new OrderDto();
        order.setCertificate(certificate);
        order.setUser(user);
        return save(order);
    }

    @Override
    public OrderDto delete(int id) {
        try {
            Order orderData = orderDao.find(id);
            if (orderData == null) {
                throw new OrderNotFoundException();
            }
            OrderDto order = DtoMapper.mapOrderFromData(orderData);
            orderDao.delete(id);
            return order;

        } catch (DaoException e) {
            logger.error("Can't delete order by id", e);
            throw new DataAccessException(e);
        }
    }

    private List<String> validateOrder(OrderDto order) {
        List<String> userErrors = idValidator.validateId(order.getUser().getId());
        List<String> certificateErrors = idValidator.validateId(order.getCertificate().getId());
        userErrors.addAll(certificateErrors);
        return userErrors;
    }
}
