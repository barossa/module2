package com.epam.esm.service.validator;

import com.epam.esm.service.dto.PageDto;

import java.util.List;

public interface PageValidator {
    List<String> validatePage(int offset);
    List<String> validateLimit(int limit);
    List<String> validatePage(PageDto page);
}

