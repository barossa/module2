package com.epam.esm.model.dto;

import lombok.*;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.annotations.FetchMode.JOIN;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tags")
public class TagData {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "tag_id")
    private int id;

    private String name;

    @ToString.Exclude
    @ManyToMany(cascade = {PERSIST, MERGE})
    @Fetch(JOIN)
    @JoinTable(name = "certificate_tags",
            joinColumns = {@JoinColumn(name = "tag_id")},
            inverseJoinColumns = {@JoinColumn(name = "certificate_id")})
    private Set<CertificateData> certificates = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagData tagData = (TagData) o;

        if (id != tagData.id) return false;
        return name != null ? name.equals(tagData.name) : tagData.name == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
