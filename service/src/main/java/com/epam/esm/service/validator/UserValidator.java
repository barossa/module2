package com.epam.esm.service.validator;

import java.util.List;

public interface UserValidator {
    List<String> validateName(String name);
}
