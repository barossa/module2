package com.epam.esm.validator.impl;

import com.epam.esm.dto.PageDto;
import com.epam.esm.validator.ErrorMessageProvider;
import com.epam.esm.validator.PageValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PageValidatorImpl implements PageValidator {
    private static final String PAGE_MUST_BE_POSITIVE_KEY = "pageMustBePositive";
    private static final String LIMIT_MUST_BE_POSITIVE_KEY = "limitMustBePositive";
    private static final String MAX_LIMIT_KEY = "maxLimit";

    private static final int MIN_OFFSET = 0;

    private static final int MIN_LIMIT = 0;
    private static final int MAX_LIMIT = 50;

    private final ErrorMessageProvider messageProvider;

    @Override
    public List<String> validatePage(int offset) {
        List<String> errors = new ArrayList<>();
        if(offset < MIN_OFFSET){
            errors.add(messageProvider.getMessage(PAGE_MUST_BE_POSITIVE_KEY));
        }
        return errors;
    }

    @Override
    public List<String> validateLimit(int limit) {
        List<String> errors = new ArrayList<>();
        if(limit < MIN_LIMIT){
            errors.add(messageProvider.getMessage(LIMIT_MUST_BE_POSITIVE_KEY));
        }
        if(limit > MAX_LIMIT){
            errors.add(messageProvider.getMessage(MAX_LIMIT_KEY) + MAX_LIMIT);
        }
        return errors;
    }

    @Override
    public List<String> validatePage(PageDto page) {
        List<String> pageErrors = validatePage(page.getPage());
        List<String> limitErrors = validateLimit(page.getLimit());
        pageErrors.addAll(limitErrors);
        return pageErrors;
    }
}
