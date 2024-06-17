package com.sogeti.java.anonymous_artist.mapper;

import com.sogeti.java.anonymous_artist.dto.ImageDto;
import com.sogeti.java.anonymous_artist.dto.ProductDto;
import com.sogeti.java.anonymous_artist.model.Product;
import com.sogeti.java.anonymous_artist.request.ProductRequest;
import com.sogeti.java.anonymous_artist.response.ProductResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMapper {

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

    public static ProductDto fromProductToProductDto(Product product) {
        List<ImageDto> imageDtos = null;
        if (product.getImage() != null) {
            imageDtos = product.getImage().stream()
                    .map(ImageMapper::fromImage)
                    .toList();
        }
        return new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getSmallSummary(),
                product.getDescription(),
                product.getPrice(),
                product.getAmountInStock(),
                imageDtos
        );
    }

    public static ProductDto fromProductRequestToDto(ProductRequest productRequest) {
        return new ProductDto(
                productRequest.id(),
                productRequest.title(),
                productRequest.smallSummary(),
                productRequest.description(),
                productRequest.price(),
                productRequest.amountInStock(),
                productRequest.imageDto()
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
}
