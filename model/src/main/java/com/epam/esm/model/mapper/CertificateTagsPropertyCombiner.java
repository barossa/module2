package com.epam.esm.model.mapper;

import com.epam.esm.model.PropertyCombiner;
import com.epam.esm.model.dto.CertificateData;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

@Component
public class CertificateTagsPropertyCombiner implements PropertyCombiner<CertificateData> {
    @Override
    public List<CertificateData> combine(List<CertificateData> certificates) {
        Optional<CertificateData> certificateOptional = certificates.stream().findAny();
        if(!certificateOptional.isPresent()){
            return new ArrayList<>();
        }

        CertificateData certificate = certificateOptional.get();
        CertificateData identity = new CertificateData();
        BeanUtils.copyProperties(certificate, identity, "tags");
        BinaryOperator<CertificateData> tagCombiner = (a, b) -> {
            a.getTags().addAll(b.getTags());
            return a;
        };

        Map<Integer, Optional<CertificateData>> combinedCertificates = certificates.stream()
                .collect(groupingBy(CertificateData::getId, reducing(tagCombiner)));
        return combinedCertificates.values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
