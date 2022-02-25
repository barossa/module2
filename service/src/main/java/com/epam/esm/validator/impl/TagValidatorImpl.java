package com.epam.esm.validator.impl;

import com.epam.esm.validator.ErrorMessageProvider;
import com.epam.esm.validator.TagValidator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class TagValidatorImpl implements TagValidator {
    private static final String TAG_NAME_REGEXP = "^[a-zA-Zа-яА-Я0-9_-]+$";
    private static final int NAME_MIN_LENGTH = 5;
    private static final int NAME_MAX_LENGTH = 20;

    private static final String NAME_MUST_BETWEEN_KEY = "tagNameMustBetween";
    private static final String NAME_REGEXP_WARN_KEY = "tagRegexpWarn";

    private final ErrorMessageProvider messageProvider;

    private final Pattern namePattern;

    public TagValidatorImpl(ErrorMessageProvider messageProvider) {
        this.messageProvider = messageProvider;
        namePattern = Pattern.compile(TAG_NAME_REGEXP);
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
}
