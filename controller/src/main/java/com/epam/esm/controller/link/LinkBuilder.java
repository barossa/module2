package com.epam.esm.controller.link;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.controller.link.HttpMethod.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public final class LinkBuilder {
    private LinkBuilder() {
    }

    public static List<Link> buildLinks(Class<?> controller, int id, RelType... reels) {
        List<Link> links = new ArrayList<>();
        for (RelType rel : reels) {
            WebMvcLinkBuilder builder = linkTo(controller);
            if (rel.isIdentifier()) {
                builder = builder.slash(id);
            }
            Link link = builder.withRel(rel.getName()).withType(rel.getMethod());
            links.add(link);
        }
        return links;
    }

    public static List<Link> buildLinks(Class<?> controller, RelType... reels) {
        return buildLinks(controller, 0, reels);
    }

    public static Link buildSelf(Class<?> controller, int id, RelType reel) {
        return linkTo(controller).slash(id).withSelfRel().withType(reel.method);
    }

    public static Link buildSelf(Class<?> controller, RelType reel) {
        return linkTo(controller).withSelfRel().withType(reel.method);
    }

    public static Link buildForName(Class<?> controller, RelType reel, String name){
        return linkTo(controller).withRel(name).withType(reel.method);
    }

    public enum RelType {
        FIND("view", GET, true),
        FIND_ALL("findAll", GET, false),
        SAVE("create", POST, false),
        UPDATE("update", PATCH, true),
        DELETE("delete", HttpMethod.DELETE, true);

        private final String name;
        private final String method;
        private final boolean identifier;

        RelType(String name, String method, boolean identifier) {
            this.name = name;
            this.method = method;
            this.identifier = identifier;
        }

        public String getMethod() {
            return method;
        }

        public String getName() {
            return name;
        }

        public boolean isIdentifier() {
            return identifier;
        }
    }
}
