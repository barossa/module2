package com.epam.esm.service;

import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.CertificateSort;
import com.epam.esm.service.exception.extend.InvalidSortParametersException;

import java.util.*;
import java.util.stream.Collectors;

public final class CertificateSortUtils {
    private static final int EQUALS_VALUE = 0;
    private static final int MAX_SORTS = 2;

    public static List<CertificateDto> sort(List<CertificateDto> certificates, Set<String> sorts) {
        Set<CertificateSort> certificateSorts = mapSorts(sorts);
        validateSorts(certificateSorts);
        certificates.sort((o1, o2) -> {
            Iterator<CertificateSort> iterator = certificateSorts.iterator();
            int compare = 0;
            while (iterator.hasNext()) {
                Comparator<CertificateDto> comparator = getComparator(iterator.next());
                if (compare == EQUALS_VALUE) {
                    compare = comparator.compare(o1, o2);
                }
            }
            return compare;
        });

        return certificates;
    }

    private static Comparator<CertificateDto> getComparator(CertificateSort sort) {
        Comparator<CertificateDto> comparator = sort.getComparator();
        return sort.isReverse() ? comparator.reversed() : comparator;
    }

    private static Set<CertificateSort> mapSorts(Set<String> sorts) {
        if (sorts == null || sorts.isEmpty()) {
            return Collections.singleton(CertificateSort.DATE_ASC);
        }
        try {
            Set<CertificateSort> certificateSorts = new HashSet<>();
            for (String sort : sorts) {
                CertificateSort certificateSort = CertificateSort.valueOf(sort.toUpperCase());
                certificateSorts.add(certificateSort);
            }
            return certificateSorts;

        } catch (IllegalArgumentException e) {
            throw new InvalidSortParametersException();
        }
    }

    private static void validateSorts(Set<CertificateSort> sorts) {
        if (sorts.size() > MAX_SORTS) {
            throw new InvalidSortParametersException();
        }
        Set<? extends Class<?>> comparators = sorts.stream()
                .map(CertificateSort::getComparator)
                .map(Object::getClass)
                .collect(Collectors.toSet());
        if (comparators.size() != sorts.size()) {
            throw new InvalidSortParametersException();
        }
    }
}
