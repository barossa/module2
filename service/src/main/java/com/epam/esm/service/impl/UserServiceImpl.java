package com.epam.esm.service.impl;

import com.epam.esm.model.dao.OrderDao;
import com.epam.esm.model.dao.UserDao;
import com.epam.esm.model.dto.OrderData;
import com.epam.esm.model.dto.PageData;
import com.epam.esm.model.dto.UserData;
import com.epam.esm.model.exception.DaoException;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.DtoMapper;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.PageDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.exception.extend.*;
import com.epam.esm.service.validator.PageValidator;
import com.epam.esm.service.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private final UserDao userDao;
    private final OrderDao orderDao;

    private final UserValidator userValidator;
    private final PageValidator pageValidator;

    @Override
    public UserDto find(int id) {
        try {
            UserData userData = userDao.find(id);
            if (userData == null) {
                throw new ObjectNotFoundException();
            }
            return DtoMapper.mapUserFromData(userData);
        } catch (DaoException e) {
            logger.error("Can't find user", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<UserDto> findAll(PageDto page) {
        try {
            validatePage(page);
            PageData pageData = DtoMapper.mapPageToData(page);
            List<UserData> usersData = userDao.findAll(pageData);
            return DtoMapper.mapUsersFromData(usersData, Collectors.toList());

        } catch (DaoException e) {
            logger.error("Can't find users", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public UserDto save(UserDto userDto) {
        try {
            List<String> errors = userValidator.validateName(userDto.getUsername());
            if (!errors.isEmpty()) {
                throw new ObjectValidationException(errors);
            }
            UserData userData = DtoMapper.mapUserToData(userDto);
            UserData savedUserData = userDao.save(userData);
            return DtoMapper.mapUserFromData(savedUserData);

        } catch (DaoException e) {
            logger.error("Can't save user", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public UserDto delete(int id) {
        try {
            UserData userData = userDao.find(id);
            if (userData == null) {
                throw new ObjectNotPresentedForDelete();
            }
            int affectedObject = userDao.delete(id);
            if (affectedObject == 0) {
                throw new ObjectDeletionException();
            }
            return DtoMapper.mapUserFromData(userData);

        } catch (DaoException e) {
            logger.error("Can't delete user", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<OrderDto> findUserOrders(int userId, PageDto page) {
        try {
            validatePage(page);
            UserData userData = userDao.find(userId);
            if (userData == null) {
                throw new ObjectNotFoundException();
            }
            PageData pageData = DtoMapper.mapPageToData(page);
            List<OrderData> userOrdersData = orderDao.findByUser(userData, pageData);
            return DtoMapper.mapOrdersFromData(userOrdersData, Collectors.toList());

        } catch (DaoException e) {
            logger.error("Can't find user's orders", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public OrderDto findUserOrder(int userId, int orderId) {
        try {
            OrderData orderData = orderDao.find(orderId);
            if (orderData == null || orderData.getUser().getId() != userId) {
                throw new ObjectNotFoundException();
            }
            return DtoMapper.mapOrderFromData(orderData);

        } catch (DaoException e) {
            logger.error("Can't find user's order by id");
            throw new DataAccessException(e);
        }
    }

    private void validatePage(PageDto page) {
        List<String> errors = pageValidator.validatePage(page);
        if (!errors.isEmpty()) {
            throw new ObjectValidationException(errors);
        }
    }
}
