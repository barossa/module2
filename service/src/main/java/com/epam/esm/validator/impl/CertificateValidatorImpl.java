package com.epam.esm.validator.impl;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.validator.CertificateValidator;
import com.epam.esm.validator.ErrorMessageProvider;
import com.epam.esm.validator.TagValidator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CertificateValidatorImpl implements CertificateValidator {
    private static final String NAME_REGEXP = "^[a-zA-Zа-яА-Я0-9_ -]+$";
    private static final int NAME_MIN_LENGTH = 5;
    private static final int NAME_MAX_LENGTH = 45;

    private static final String DESCRIPTION_REGEXP = "^[a-zA-Zа-яА-Я0-9_ -!?.,;:()\"'@#$%^&*=№]+$";
    private static final long DESCRIPTION_MIN = 10L;
    private static final long DESCRIPTION_MAX = 100L;

    private static final long PRICE_MIN = 0L;
    private static final long PRICE_MAX = 1_000_000_000_000L;

    private static final long DURATION_MIN = 1L;
    private static final long DURATION_MAX = 365L;

    private static final String NAME_MUST_BETWEEN_KEY = "certNameMustBetween";
    private static final String NAME_REGEXP_WARN_KEY = "certNameRegexpWarn";

    private static final String DESCRIPTION_MUST_BETWEEN_KEY = "descriptionMustBetween";
    private static final String DESCRIPTION_REGEXP_WARN_KEY = "descriptionRegexpWarn";

    private static final String PRICE_MUST_BE_POSITIVE_KEY = "pricePositive";
    private static final String PRICE_TOO_LARGE_KEY = "priceLarge";
    private static final String DURATION_MUST_BETWEEN_KEY = "durationMustBetween";

    private final TagValidator tagValidator;
    private final ErrorMessageProvider messageProvider;

    private final Pattern namePattern;
    private final Pattern descriptionPattern;

    public CertificateValidatorImpl(TagValidator tagValidator, ErrorMessageProvider messageProvider) {
        this.tagValidator = tagValidator;
        this.messageProvider = messageProvider;
        namePattern = Pattern.compile(NAME_REGEXP);
        descriptionPattern = Pattern.compile(DESCRIPTION_REGEXP);
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
    public List<String> validateDescription(String description) {
        List<String> errors = new ArrayList<>();
        if (description.length() < DESCRIPTION_MIN || description.length() > DESCRIPTION_MAX) {
            errors.add(messageProvider.getMessage(DESCRIPTION_MUST_BETWEEN_KEY));
        }
        if (!descriptionPattern.matcher(description).matches()) {
            errors.add(messageProvider.getMessage(DESCRIPTION_REGEXP_WARN_KEY));
        }
        return errors;
    }

    @Override
    public List<String> validatePrice(BigDecimal price) {
        List<String> errors = new ArrayList<>();
        if (price.longValue() < PRICE_MIN) {
            errors.add(messageProvider.getMessage(PRICE_MUST_BE_POSITIVE_KEY));
        }
        if (price.longValue() > PRICE_MAX) {
            errors.add(messageProvider.getMessage(PRICE_TOO_LARGE_KEY));
        }
        return errors;
    }

    @Override
    public List<String> validateDuration(Long duration) {
        List<String> errors = new ArrayList<>();
        if (duration < DURATION_MIN || duration > DURATION_MAX) {
            errors.add(messageProvider.getMessage(DURATION_MUST_BETWEEN_KEY));
        }
        return errors;
    }

    @Override
    public List<String> validateTagName(String tag) {
        return tagValidator.validateName(tag);
    }

    @Override
    public List<String> validate(CertificateDto certificate) {
        List<String> nameErrors = validateName(certificate.getName());
        List<String> descriptionErrors = validateDescription(certificate.getDescription());
        List<String> priceErrors = validatePrice(certificate.getPrice());
        List<String> durationErrors = validateDuration(certificate.getDuration());
        List<String> tagErrors = certificate.getTags().stream()
                .map(TagDto::getName)
                .map(this::validateTagName)
                .reduce(new ArrayList<>(), (a, b) -> {
                    b.addAll(a);
                    return b;
                });
        return Stream.of(nameErrors, descriptionErrors, priceErrors, durationErrors, tagErrors)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }
}
