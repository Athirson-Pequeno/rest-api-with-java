package com.tizo.br.model;

import jakarta.persistence.*;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Table(name = "parts")
@Entity
public class Part extends RepresentationModel<Part> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "part_sectors",
            joinColumns = {@JoinColumn(name = "id_part")},
            inverseJoinColumns = {@JoinColumn(name = "id_sector")}
    )
    private List<Sector> sectors;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "part_types",
            joinColumns = {@JoinColumn(name = "id_part")},
            inverseJoinColumns = {@JoinColumn(name = "id_type")}
    )
    private List<Type> types;

    public Part() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Sector> getSectors() {
        return sectors;
    }

    public void setSectors(List<Sector> sectors) {
        this.sectors = sectors;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Part part)) return false;
        return Objects.equals(id, part.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Part{" +
                "id=" + id +
                ", description='" + description +
                '}';
    }
}
