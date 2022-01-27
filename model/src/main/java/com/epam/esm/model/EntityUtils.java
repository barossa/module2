package com.epam.esm.model;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

public final class EntityUtils {
    public static final int UNDEFINED_ID = 0;

    private EntityUtils() {
    }

    public static String[] getNotNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) != null)
                .toArray(String[]::new);
    }

    public static <T> void replaceNullProperties(T source, T dest) {
        BeanUtils.copyProperties(source, dest, getNotNullPropertyNames(dest));
    }
}
