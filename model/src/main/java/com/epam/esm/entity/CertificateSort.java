package com.epam.esm.entity;

public enum CertificateSort {
    NAME_ASC(Certificate_.NAME, true),
    NAME_DESC(Certificate_.NAME, false),
    DATE_ASC(Certificate_.CREATE_DATE, true),
    DATE_DESC(Certificate_.CREATE_DATE, false);

    private final String fieldName;
    private final boolean ascending;

    CertificateSort(String fieldName, boolean ascending) {
        this.fieldName = fieldName;
        this.ascending = ascending;

    }

    public String getFieldName() {
        return fieldName;
    }

    public boolean isAscending() {
        return ascending;
    }
}
