package com.epam.esm.validator.impl;

import com.epam.esm.dto.UserDto;
import com.epam.esm.validator.ErrorMessageProvider;
import com.epam.esm.validator.UserValidator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UserValidatorImpl implements UserValidator {
    private static final String NAME_REGEXP = "^[a-zA-Zа-яА-Я0-9_-]+$";
    private static final int NAME_MIN_LENGTH = 5;
    private static final int NAME_MAX_LENGTH = 20;

    private static final String PASS_REGEXP = "^((?=\\S*?[A-Z])(?=\\S*?[a-z])(?=\\S*?[0-9]).+)\\S$";
    private static final int PASS_MIN_LENGTH = 6;
    private static final int PASS_MAX_LENGTH = 50;

    private static final String NAME_MUST_BETWEEN_KEY = "userNameMustBetween";
    private static final String NAME_REGEXP_WARN_KEY = "userRegexpWarn";

    private static final String PASS_REGEXP_WARN_KEY = "userPassRegexpWarn";
    private static final String PASS_MUST_BETWEEN_KEY = "userPassMustBetween";

    private final ErrorMessageProvider messageProvider;

    private final Pattern namePattern;
    private final Pattern passPattern;

    public UserValidatorImpl(ErrorMessageProvider messageProvider) {
        this.messageProvider = messageProvider;
        namePattern = Pattern.compile(NAME_REGEXP);
        passPattern = Pattern.compile(PASS_REGEXP);
    }

    @Override
    public List<String> validateName(String name) {
        List<String> errors = new ArrayList<>();
        if (name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH) {
            errors.add(messageProvider.getMessage(NAME_MUST_BETWEEN_KEY));
        }
        if (!namePattern.matcher(name).matches()) {
            errors.add(messageProvider.getMessage(NAME_REGEXP_WARN_KEY));
        }
        return errors;
    }

    @Override
    public List<String> validatePassword(String password) {
        List<String> errors = new ArrayList<>();
        if (password.length() < PASS_MIN_LENGTH || password.length() > PASS_MAX_LENGTH) {
            errors.add(messageProvider.getMessage(PASS_MUST_BETWEEN_KEY));
        }
        if (!passPattern.matcher(password).matches()) {
            errors.add(messageProvider.getMessage(PASS_REGEXP_WARN_KEY));
        }
        return errors;
    }

    @Override
    public List<String> validate(UserDto user) {
        List<String> nameErrors = validateName(user.getUsername());
        List<String> passErrors = validatePassword(user.getPassword());
        return Stream.of(nameErrors, passErrors)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }
}
