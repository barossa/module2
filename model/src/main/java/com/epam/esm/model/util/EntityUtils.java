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

    public static String[] getNotNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) != null)
                .toArray(String[]::new);
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) != null)
                .toArray(String[]::new);
    }

    //Merge all not null properties from source
    public static void mergeNotNullProperties(Object source, Object dest){
        BeanUtils.copyProperties(source, dest, getNullPropertyNames(source));
    }

    //Replace all null properties in dest object
    public static void replaceNullProperties(Object source, Object dest){
        BeanUtils.copyProperties(source, dest, getNotNullPropertyNames(dest));
    }
}
