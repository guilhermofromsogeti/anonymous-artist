package com.sogeti.java.anonymous_artist.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
@Builder
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String smallSummary;
    private String description;
    private BigDecimal price;
    private Integer amountInStock;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<FileUploadResponse> image;

    public Product(UUID id, String title, String smallSummary, String description, BigDecimal price, Integer amountInStock) {
        this.id = id;
        this.title = title;
        this.smallSummary = smallSummary;
        this.description = description;
        this.price = price;
        this.amountInStock = amountInStock;
    }

    public void addImage(FileUploadResponse image) {
        this.image.add(image);
    }
}
