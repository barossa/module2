package com.epam.esm.model.mapper;

import com.epam.esm.model.PropertyCombiner;
import com.epam.esm.model.dto.TagData;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.BinaryOperator;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

@Component
public class TagCertificatesPropertyCombiner implements PropertyCombiner<TagData> {
    @Override
    public List<TagData> combine(List<TagData> tags) {
        Optional<TagData> tagOptional = tags.stream().findAny();
        if (!tagOptional.isPresent()) {
            return new ArrayList<>();
        }

        TagData tag = tagOptional.get();
        TagData identity = new TagData();
        BeanUtils.copyProperties(tag, identity, "certificates");
        BinaryOperator<TagData> certificateCombiner = (a, b) -> {
            b.getCertificates().addAll(a.getCertificates());
            return b;
        };

        Map<Integer, TagData> combinedTags = tags.stream()
                .collect(groupingBy(TagData::getId,
                        reducing(identity, certificateCombiner)));
        return new ArrayList<>(combinedTags.values());
    }
}
