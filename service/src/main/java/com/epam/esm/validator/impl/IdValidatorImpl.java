package com.epam.esm.validator.impl;

import com.epam.esm.validator.ErrorMessageProvider;
import com.epam.esm.validator.IdValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IdValidatorImpl implements IdValidator {
    private static final String ID_NOT_PRESENTED_KEY = "idNotPresented";
    private static final String ID_UNDEFINED_KEY = "idShouldBeDefined";

    private static final int UNDEFINED_ID = 0;

    private final ErrorMessageProvider messageProvider;

    @Override
    public List<String> validateId(Integer id) {
        List<String> errors = new ArrayList<>();
        if (id == null) {
            errors.add(messageProvider.getMessage(ID_NOT_PRESENTED_KEY));
        }
        if (id == null || id <= UNDEFINED_ID) {
            errors.add(messageProvider.getMessage(ID_UNDEFINED_KEY));
        }
        return errors;
    }
}
