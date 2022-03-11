package com.epam.esm.link;


import com.epam.esm.dto.CertificateFilterDto;
import com.epam.esm.dto.PageDto;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.epam.esm.link.HttpMethod.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public final class LinkUtils {
    private static final String PAGE_KEY = "page";
    private static final String LIMIT_KEY = "limit";

    private static final String TAGS_KEY = "tags";
    private static final String NAMES_KEY = "names";
    private static final String DESCRIPTIONS_KEY = "descriptions";

    private static final String AND = "&";
    private static final String QUERY_START = "?";

    private LinkUtils() {
    }

    public static List<Link> buildLinks(Class<?> controller, int id, RelType... reels) {
        List<Link> links = new ArrayList<>();
        for (RelType rel : reels) {
            links.add(buildLink(controller, id, rel));
        }
        return links;
    }

    public static Link buildLink(Class<?> controller, int id, RelType rel) {
        WebMvcLinkBuilder builder = linkTo(controller);
        if (rel.isIdentifier()) {
            builder = builder.slash(id);
        }
        return builder.withRel(rel.getName()).withType(rel.getMethod());
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

    public static String pageQuery(PageDto pageDto) {
        return PAGE_KEY + "=" + pageDto.getPage() + AND + LIMIT_KEY + "=" + pageDto.getLimit();
    }

    public static String prepareQuery(String... queries) {
        StringBuilder builder = new StringBuilder(QUERY_START);
        for (String query : queries) {
            builder.append(query);
            builder.append(AND);
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    public static String filterQuery(CertificateFilterDto filterDto) {
        StringBuilder builder = new StringBuilder();
        for (String tag : filterDto.getTags()) {
            builder.append(AND);
            builder.append(TAGS_KEY);
            builder.append("=");
            builder.append(tag);
        }
        for (String name : filterDto.getNames()) {
            builder.append(AND);
            builder.append(NAMES_KEY);
            builder.append("=");
            builder.append(name);
        }
        for (String description : filterDto.getDescriptions()) {
            builder.append(AND);
            builder.append(DESCRIPTIONS_KEY);
            builder.append("=");
            builder.append(description);
        }
        return builder.length() > 0 ? builder.substring(1) : "";
    }

    public static <T> CollectionModel<T> attachToCollection(Iterable<T> collection, Link... links) {
        CollectionModel<T> collectionModel;
        if (collection.iterator().hasNext()) {
            collectionModel = CollectionModel.of(collection, links);
        } else {
            collectionModel = CollectionModel.of(collection);
        }
        return collectionModel;
    }

    public static void distinctLinks(RepresentationModel<?> representationModel) {
        Links links = representationModel.getLinks();
        Optional<Link> selfOptional = links.getLink(IanaLinkRelations.SELF);
        if (selfOptional.isPresent()) {
            Link self = selfOptional.get();
            Predicate<Link> equalHref = link -> self.getHref().equals(link.getHref());
            Predicate<Link> equalType = link -> self.getType().equals(link.getType());
            Predicate<Link> equalRel = link -> self.getRel().value().equals(link.getRel().value());

            List<Link> distincted = links.stream()
                    .filter(equalHref.and(equalType).and(equalRel.negate()).negate())
                    .collect(Collectors.toList());

            representationModel.removeLinks();
            representationModel.add(distincted);
        }
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