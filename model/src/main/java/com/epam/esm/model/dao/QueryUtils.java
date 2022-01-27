package com.epam.esm.model.dao;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.epam.esm.model.dao.ColumnName.*;

public final class QueryUtils {
    private static final String SQL_OR = "OR";
    private static final String SQL_EQUALS = "=";
    private static final String SQL_QUOTE = "'";
    private static final String SQL_PERCENT = "%";
    private static final String SQL_LIKE = "LIKE";

    private QueryUtils(){}

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

    public static String buildSelectByCertNameOrDescQuery(String notCompleted, String[] searches){
        StringBuilder builder = new StringBuilder(notCompleted);
        Iterator<String> iterator = Arrays.stream(searches).iterator();
        while(iterator.hasNext()){
            String part = iterator.next();
            builder.append(" ")
                    .append(CERTIFICATES_NAME)
                    .append(" ")
                    .append(SQL_LIKE)
                    .append(" ")
                        .append(SQL_QUOTE)
                            .append(SQL_PERCENT)
                            .append(part)
                            .append(SQL_PERCENT)
                        .append(SQL_QUOTE)
                    .append(" ")
                            .append(SQL_OR)
                    .append(" ")
                    .append(CERTIFICATES_DESCRIPTION)
                    .append(" ")
                    .append(SQL_LIKE)
                    .append(" ")
                        .append(SQL_QUOTE)
                            .append(SQL_PERCENT)
                            .append(part)
                            .append(SQL_PERCENT)
                        .append(SQL_QUOTE);
            builder.append(iterator.hasNext() ? " " + SQL_OR : ";");
        }
        return builder.toString();
    }
}
