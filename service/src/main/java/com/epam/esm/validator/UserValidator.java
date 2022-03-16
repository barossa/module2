package com.epam.esm.validator;

import com.epam.esm.dto.UserDto;

import java.util.List;

public interface UserValidator {
    List<String> validateName(String name);
    List<String> validatePassword(String password);
    List<String> validate(UserDto user);
}
