package com.epam.esm.model.util;

import java.util.List;

public interface PropertyCombiner<T> {
    List<T> combine(List<T> entities);
}
