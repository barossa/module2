package com.epam.esm.model.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

public final class EntityUtils {
    public static final int UNDEFINED_ID = 0;

    private EntityUtils() {
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }

    //Copy all not null properties to destination object
    public static <T> void replaceNotNullProperties(Object source, T dest){
        BeanUtils.copyProperties(source, dest, getNullPropertyNames(source));
    }
}
