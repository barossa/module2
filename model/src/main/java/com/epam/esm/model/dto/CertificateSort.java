package com.epam.esm.model.dto;

public enum CertificateSort {
    NAME_ASC(CertificateData_.NAME, true),
    NAME_DESC(CertificateData_.NAME, false),
    DATE_ASC(CertificateData_.CREATE_DATE, true),
    DATE_DESC(CertificateData_.CREATE_DATE, false);

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
