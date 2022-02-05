package com.epam.esm.controller.dto;

import com.epam.esm.service.dto.TagDto;

import java.util.Collection;
import java.util.stream.Collector;

public final class EntityMapper {

    private EntityMapper() {
    }

    public static Tag mapTagFromDto(TagDto dto) {
        return new Tag(dto.getId(), dto.getName());
    }

    public static <T extends Collection<Tag>> T mapTagsFromDto(Collection<TagDto> dtos,
                                                               Collector<Tag, ?, T> collector) {
        T tags = null;
        if (dtos != null) {
            tags = dtos.stream()
                    .map(EntityMapper::mapTagFromDto)
                    .collect(collector);
        }
        return tags;
    }

}
