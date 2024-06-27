package com.sogeti.java.anonymous_artist.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "image")
public class FileUploadResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_sequence")
    @SequenceGenerator(name = "image_sequence", sequenceName = "image_sequence", initialValue = 26, allocationSize = 1)
    private Long id;
    private String fileName;
    private String contentType;
    private String url;

    @ManyToOne
    @JoinColumn(name = "fk_product_id")
    Product product;


    public FileUploadResponse(String fileName, String contentType, String url) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.url = url;
    }

    public FileUploadResponse(String fileName, String contentType, String url, Product product) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.url = url;
        this.product = product;
    }
}
