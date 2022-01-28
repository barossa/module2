package com.epam.esm.model.dao;

import java.util.List;
import java.util.ListIterator;

import static com.epam.esm.model.dao.ColumnName.*;

public final class QueryUtils {
    private static final String SQL_OR = "OR";
    private static final String SQL_AND = "AND";
    private static final String SQL_EQUALS = "=";
    private static final String SQL_QUOTE = "'";
    private static final String SQL_PERCENT = "%";
    private static final String SQL_LIKE = "LIKE";

    private QueryUtils() {
    }

    public static String buildSelectByTagNamesQuery(String notCompleted, List<String> names) {
        StringBuilder builder = new StringBuilder(notCompleted);
        ListIterator<String> iterator = names.listIterator();
        while (iterator.hasNext()) {
            builder.append(" ")
                    .append(TAGS_NAME)
                    .append(SQL_EQUALS)
                    .append(SQL_QUOTE)
                    .append(iterator.next())
                    .append(SQL_QUOTE);
            builder.append(iterator.hasNext() ? " " + SQL_OR : ";");
        }
        return builder.toString();
    }

    public static String buildSelectByOptions(String notCompleted, List<String> tags, List<String> names, List<String> descriptions, boolean strong) {
        StringBuilder builder = new StringBuilder(notCompleted);
        tags.forEach(tag -> appendCondition(builder, TAGS_NAME, tag, strong));
        names.forEach(name -> appendCondition(builder, CERTIFICATES_NAME, name, strong));
        descriptions.forEach(description -> appendCondition(builder, CERTIFICATES_DESCRIPTION, description, strong));
        int appendix = strong ? SQL_AND.length() : SQL_OR.length();
        builder.replace(builder.length() - appendix - 1, builder.length(), ";");
        return builder.toString();
    }

    private static void appendCondition(StringBuilder builder, String column, String condition, boolean strong) {
        builder.append(" ")
                .append(column)
                .append(" ")
                .append(SQL_LIKE)
                .append(" ")
                    .append(SQL_QUOTE)
                .append(SQL_PERCENT)
                .append(condition)
                .append(SQL_PERCENT)
                    .append(SQL_QUOTE)
                .append(" ")
                .append(strong ? SQL_AND : SQL_OR);
    }
}
