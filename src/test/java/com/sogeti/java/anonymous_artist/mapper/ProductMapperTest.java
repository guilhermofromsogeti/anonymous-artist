package com.sogeti.java.anonymous_artist.mapper;

import com.sogeti.java.anonymous_artist.dto.ProductDto;
import com.sogeti.java.anonymous_artist.factory.ProductFactory;
import com.sogeti.java.anonymous_artist.model.Product;
import com.sogeti.java.anonymous_artist.request.ProductRequest;
import com.sogeti.java.anonymous_artist.response.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductMapperTest {

    @MockBean
    private ProductMapper productMapper;

    @Test
    void givenProductDto_whenMappedToProductResponse_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        ProductDto productDto = ProductFactory.aProductDto().build();

        // When
        ProductResponse productResponse = ProductMapper.fromProductDtoToProductResponse(productDto);

        // Then
        assertEquals(productDto.id(), productResponse.id());
        assertEquals(productDto.title(), productResponse.title());
        assertEquals(productDto.description(), productResponse.description());
        assertEquals(productDto.smallSummary(), productResponse.smallSummary());
        assertEquals(productDto.amountInStock(), productResponse.amountInStock());
        assertEquals(productDto.price(), productResponse.price());
    }

    @Test
    void givenProductDto_whenMappedToProduct_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        ProductDto productDto = ProductFactory.aProductDto().build();

        // When
        Product product = ProductMapper.fromProductDtoToProduct(productDto);

        // Then
        assertEquals(productDto.id(), product.getId());
        assertEquals(productDto.title(), product.getTitle());
        assertEquals(productDto.description(), product.getDescription());
        assertEquals(productDto.smallSummary(), product.getSmallSummary());
        assertEquals(productDto.amountInStock(), product.getAmountInStock());
        assertEquals(productDto.price(), product.getPrice());
    }

    @Test
    void givenProductRequest_whenMappedToProductDto_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        ProductRequest productRequest = ProductFactory.aProductRequest().build();

        // When
        ProductDto productDto = ProductMapper.fromProductRequestToDto(productRequest);

        // Then
        assertEquals(productDto.id(), productRequest.id());
        assertEquals(productDto.title(), productRequest.title());
        assertEquals(productDto.description(), productRequest.description());
        assertEquals(productDto.smallSummary(), productRequest.smallSummary());
        assertEquals(productDto.amountInStock(), productRequest.amountInStock());
        assertEquals(productDto.price(), productRequest.price());
    }

    @Test
    void givenProductRequest_whenMappedToProductDTO_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        ProductRequest productRequest = ProductFactory.aProductRequest().build();

        // When
        ProductDto productDto = ProductMapper.fromProductRequestToDto(productRequest);

        // Then
        assertEquals(productDto.id(), productRequest.id());
        assertEquals(productDto.title(), productRequest.title());
        assertEquals(productDto.description(), productRequest.description());
        assertEquals(productDto.smallSummary(), productRequest.smallSummary());
        assertEquals(productDto.price(), productRequest.price());
        assertEquals(productDto.amountInStock(), productRequest.amountInStock());
    }
}
