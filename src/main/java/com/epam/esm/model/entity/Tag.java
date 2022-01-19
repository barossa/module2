package com.epam.esm.model.entity;

import java.util.Set;

public class Tag {
    private int id;
    private String name;
    private Set<Certificate> certificates;

    public Tag(){
    }

    public Tag(int id, String name, Set<Certificate> certificates) {
        this.id = id;
        this.name = name;
        this.certificates = certificates;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(Set<Certificate> certificates) {
        this.certificates = certificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (name != null ? !name.equals(tag.name) : tag.name != null) return false;
        return certificates != null ? certificates.equals(tag.certificates) : tag.certificates == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (certificates != null ? certificates.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", certificates=" + certificates +
                '}';
    }
}
