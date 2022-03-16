package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.DtoMapper;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Page;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.extend.*;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.PageValidator;
import com.epam.esm.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private final UserDao userDao;
    private final OrderDao orderDao;

    private final UserValidator userValidator;
    private final PageValidator pageValidator;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto find(int id) {
        try {
            User userData = userDao.find(id);
            if (userData == null) {
                throw new UserNotFoundException();
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
            Page pageData = DtoMapper.mapPageToData(page);
            List<User> usersData = userDao.findAll(pageData);
            return DtoMapper.mapUsersFromData(usersData, Collectors.toList());

        } catch (DaoException e) {
            logger.error("Can't find users", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public UserDto save(UserDto userDto) {
        try {
            List<String> errors = userValidator.validate(userDto);
            if (!errors.isEmpty()) {
                throw new ObjectValidationException(errors);
            }
            User userData = DtoMapper.mapUserToData(userDto);
            String encodedPassword = passwordEncoder.encode(userData.getPassword());
            userData.setPassword(encodedPassword);
            User savedUserData = userDao.save(userData);
            return DtoMapper.mapUserFromData(savedUserData);

        } catch (DaoException e) {
            logger.error("Can't save user", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public UserDto delete(int id) {
        try {
            User userData = userDao.find(id);
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
            User userData = userDao.find(userId);
            if (userData == null) {
                throw new UserNotFoundException();
            }
            Page pageData = DtoMapper.mapPageToData(page);
            List<Order> userOrdersData = orderDao.findByUser(userData, pageData);
            return DtoMapper.mapOrdersFromData(userOrdersData, Collectors.toList());

        } catch (DaoException e) {
            logger.error("Can't find user's orders", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public OrderDto findUserOrder(int userId, int orderId) {
        try {
            Order orderData = orderDao.find(orderId);
            if (orderData == null || orderData.getUser().getId() != userId) {
                throw new OrderNotFoundException();
            }
            return DtoMapper.mapOrderFromData(orderData);

        } catch (DaoException e) {
            logger.error("Can't find user's order by id", e);
            throw new DataAccessException(e);
        }
    }

    @Override
    public UserDto findTopUser() {
        try {
            User userData = userDao.findTopUser();
            return DtoMapper.mapUserFromData(userData);

        } catch (DaoException e) {
            logger.error("Cant find top user", e);
            throw new DataAccessException();
        }
    }

    private void validatePage(PageDto page) {
        List<String> errors = pageValidator.validatePage(page);
        if (!errors.isEmpty()) {
            throw new ObjectValidationException(errors);
        }
    }

    @Override
    @Transactional
    public UserDto loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userDao.findByName(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not presented");
            }
            return DtoMapper.mapUserFromData(user);

        } catch (DaoException e) {
            logger.error("Can't load user by name from db", e);
            throw new UsernameNotFoundException("Can't load user by name from db", e);
        }
    }
}
