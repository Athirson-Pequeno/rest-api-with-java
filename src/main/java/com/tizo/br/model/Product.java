package com.tizo.br.model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "products")
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_sectors",
            joinColumns = {@JoinColumn(name = "id_products")},
            inverseJoinColumns = {@JoinColumn(name = "id_sectors")}
    )
    private List<Sector> sectors;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_types",
            joinColumns = {@JoinColumn(name = "id_products")},
            inverseJoinColumns = {@JoinColumn(name = "id_types")}
    )
    private List<Type> types;

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
        if (!(o instanceof Product product)) return false;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(productCode, product.productCode) && Objects.equals(colorCode, product.colorCode) && Objects.equals(colorName, product.colorName) && Objects.equals(sectors, product.sectors) && Objects.equals(types, product.types);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, productCode, colorCode, colorName, sectors, types);
    }
}
