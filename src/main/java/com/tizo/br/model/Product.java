package com.tizo.br.model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "products")
@Entity
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String productCode;
    private String colorCode;
    private String colorName;
    private Integer quantityPerBatch;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_parts",
            joinColumns = {@JoinColumn(name = "id_product")},
            inverseJoinColumns = {@JoinColumn(name = "id_part")}
    )
    private List<Part> parts;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public Integer getQuantityPerBatch() {
        return quantityPerBatch;
    }

    public void setQuantityPerBatch(Integer quantityPerBatch) {
        this.quantityPerBatch = quantityPerBatch;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    public List<Sector> getSectors() {
        List<Sector> sectors = new ArrayList<>();
        for (Part part : parts) {
            for (Sector sector : part.getSectors()) {
                if (!sectors.contains(sector)) {
                    sectors.add(sector);
                }
            }
        }
        return sectors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", colorName='" + colorName + '\'' +
                '}';
    }
}
