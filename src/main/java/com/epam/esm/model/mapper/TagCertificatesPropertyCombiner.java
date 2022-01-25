package com.epam.esm.model.mapper;

import com.epam.esm.model.PropertyCombiner;
import com.epam.esm.model.entity.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.BinaryOperator;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

@Component
public class TagCertificatesPropertyCombiner implements PropertyCombiner<Tag> {
    @Override
    public List<Tag> combine(List<Tag> tags) {
        Optional<Tag> tagOptional = tags.stream().findAny();
        if (!tagOptional.isPresent()) {
            return new ArrayList<>();
        }

        Tag tag = tagOptional.get();
        Tag identity = new Tag();
        BeanUtils.copyProperties(tag, identity, "certificates");
        BinaryOperator<Tag> certificateCombiner = (a, b) -> {
            b.getCertificates().addAll(a.getCertificates());
            return b;
        };

        Map<Integer, Tag> combinedTags = tags.stream()
                .collect(groupingBy(Tag::getId,
                        reducing(identity, certificateCombiner)));
        return new ArrayList<>(combinedTags.values());
    }
}
