package com.epam.esm.model.dao;

import static com.epam.esm.model.dao.TableName.*;

public final class ColumnName {
    private static final String DOT = ".";

    public static final String CERTIFICATES_ID = CERTIFICATES + DOT + "certificate_id";
    public static final String CERTIFICATES_NAME = CERTIFICATES + DOT + "name";
    public static final String CERTIFICATES_DESCRIPTION = CERTIFICATES + DOT + "description";
    public static final String CERTIFICATES_PRICE = CERTIFICATES + DOT + "price";
    public static final String CERTIFICATES_DURATION = CERTIFICATES + DOT + "duration";
    public static final String CERTIFICATES_CREATE_DATE = CERTIFICATES + DOT + "create_date";
    public static final String CERTIFICATES_LAST_UPDATE_DATE = CERTIFICATES + DOT + "last_update_date";

    public static final String TAGS_ID = TAGS + DOT + "tag_id";
    public static final String TAGS_NAME = TAGS + DOT + "name";

    public static final String CERTIFICATE_TAGS_ID = CERTIFICATE_TAGS + DOT + "record_id";
    public static final String CERTIFICATE_TAGS_TAG_ID = CERTIFICATE_TAGS + DOT + "tag_id";
    public static final String CERTIFICATE_TAGS_CERTIFICATE_ID = CERTIFICATE_TAGS + DOT + "certificate_id";
}
