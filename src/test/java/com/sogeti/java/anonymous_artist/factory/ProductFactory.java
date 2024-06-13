package com.sogeti.java.anonymous_artist.factory;

import com.github.javafaker.Faker;
import com.sogeti.java.anonymous_artist.dto.ProductDto;
import com.sogeti.java.anonymous_artist.model.Product;
import com.sogeti.java.anonymous_artist.request.ProductRequest;
import com.sogeti.java.anonymous_artist.response.ProductResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductFactory {

    private final static Faker faker = new Faker();

    public static Product.ProductBuilder aProduct() {
        return Product.builder()
                .id(UUID.randomUUID())
                .title(faker.lorem().sentence(3))
                .smallSummary(faker.lorem().paragraph())
                .description(faker.lorem().paragraph())
                .price(BigDecimal.valueOf(15.99))
                .amountInStock((int) faker.number().numberBetween(1,5));
    }

    public static ProductRequest.ProductRequestBuilder aProductRequest() {
        return ProductRequest.builder()
                .id(UUID.randomUUID())
                .title(faker.lorem().sentence(3))
                .smallSummary(faker.lorem().paragraph())
                .description(faker.lorem().paragraph())
                .price(BigDecimal.valueOf(15.99))
                .amountInStock((int) faker.number().numberBetween(1,5));
    }

    public static ProductResponse fromProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getTitle(),
                product.getSmallSummary(),
                product.getDescription(),
                product.getPrice(),
                product.getAmountInStock());
    }

    public static Product fromProductRequestToProduct(ProductRequest productRequest) {
        return new Product(
                productRequest.id(),
                productRequest.title(),
                productRequest.smallSummary(),
                productRequest.description(),
                productRequest.price(),
                productRequest.amountInStock());
    }

    public static ProductDto fromProductRequestToProductDto(ProductRequest productRequest) {
        return new ProductDto(
                productRequest.id(),
                productRequest.title(),
                productRequest.smallSummary(),
                productRequest.description(),
                productRequest.price(),
                productRequest.amountInStock());
    }

    public static ProductDto.ProductDtoBuilder aProductDto() {
        return ProductDto.builder()
                .id(UUID.randomUUID())
                .title(faker.lorem().sentence(3))
                .smallSummary(faker.lorem().paragraph())
                .description(faker.lorem().paragraph())
                .price(BigDecimal.valueOf(15.99));
    }

    public static ProductResponse fromProductDtoToProductResponse(ProductDto productDto) {
        return new ProductResponse(
                productDto.id(),
                productDto.title(),
                productDto.smallSummary(),
                productDto.description(),
                productDto.price(),
                productDto.amountInStock()
        );
    }

    public static Product fromProductDtoToProduct(ProductDto productDto) {
        return new Product(
                productDto.id(),
                productDto.title(),
                productDto.smallSummary(),
                productDto.description(),
                productDto.price(),
                productDto.amountInStock()
        );
    }

    public static ProductDto fromProductToProductDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getSmallSummary(),
                product.getDescription(),
                product.getPrice(),
                product.getAmountInStock()
        );
    }
}