package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

public interface TagService extends BaseService<TagDto> {
    TagDto findMostUsedOfUser(int userId);

    TagDto findMostUsedOfTopUser();
}
