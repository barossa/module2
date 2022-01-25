package com.epam.esm.model;

import java.util.List;

public interface PropertyCombiner<T> {
    List<T> combine(List<T> entities);
}
