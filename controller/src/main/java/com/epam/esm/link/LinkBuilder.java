package com.epam.esm.link;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

public interface LinkBuilder <T extends RepresentationModel<T>>{
    void attachLinks(T representationModel, Link self);
}
