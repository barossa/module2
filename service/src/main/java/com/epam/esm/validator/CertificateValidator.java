package com.epam.esm.validator;

import com.epam.esm.dto.CertificateDto;

import java.math.BigDecimal;
import java.util.List;

public interface CertificateValidator {
    List<String> validateName(String name);

    List<String> validateDescription(String description);

    List<String> validatePrice(BigDecimal price);

    List<String> validateDuration(Long duration);

    List<String> validateTagName(String tag);

    List<String> validate(CertificateDto certificate);
}
