package com.epam.esm.model.mapper;

import com.epam.esm.model.PropertyCombiner;
import com.epam.esm.model.entity.Certificate;
import org.springframework.beans.BeanUtils;
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
        Optional<Certificate> certificateOptional = certificates.stream().findAny();
        if(!certificateOptional.isPresent()){
            return new ArrayList<>();
        }

        Certificate certificate = certificateOptional.get();
        Certificate identity = new Certificate();
        BeanUtils.copyProperties(certificate, identity, "tags");
        BinaryOperator<Certificate> tagCombiner = (a, b) -> {
            a.getTags().addAll(b.getTags());
            return a;
        };

        Map<Integer, Optional<Certificate>> combinedCertificates = certificates.stream()
                .collect(groupingBy(Certificate::getId, reducing(tagCombiner)));
        return combinedCertificates.values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
