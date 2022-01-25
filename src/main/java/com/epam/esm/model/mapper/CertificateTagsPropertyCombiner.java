package com.epam.esm.model.mapper;

import com.epam.esm.model.PropertyCombiner;
import com.epam.esm.model.entity.Certificate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

@Component
public class CertificateTagsPropertyCombiner implements PropertyCombiner<Certificate> {
    @Override
    public List<Certificate> combine(List<Certificate> certificates) {
        Optional<Certificate> certificate = certificates.stream().findAny();
        if(!certificate.isPresent()){
            return new ArrayList<>();
        }

        Certificate identity = certificate.get();
        identity.setTags(new HashSet<>());
        BinaryOperator<Certificate> tagCombiner = (a, b) -> {
            b.getTags().addAll(a.getTags());
            return b;
        };

        Map<Integer, Optional<Certificate>> combinedCertificates = certificates.stream()
                .collect(groupingBy(Certificate::getId, reducing(tagCombiner)));
        return combinedCertificates.values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
