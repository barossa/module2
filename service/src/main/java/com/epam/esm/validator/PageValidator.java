package com.epam.esm.validator;

import com.epam.esm.dto.PageDto;

import java.util.List;

public interface PageValidator {
    List<String> validatePage(int offset);
    List<String> validateLimit(int limit);
    List<String> validatePage(PageDto page);
}

