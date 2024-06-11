package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.dto.ProductDto;
import com.sogeti.java.anonymous_artist.exception.NoDataFoundException;
import com.sogeti.java.anonymous_artist.exception.ProductAlreadyExistException;
import com.sogeti.java.anonymous_artist.mapper.ProductMapper;
import com.sogeti.java.anonymous_artist.model.Product;
import com.sogeti.java.anonymous_artist.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> getListOfAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream()
                .map(ProductMapper::fromProductToProductDto)
                .toList();
    }

    public ProductDto registerNewProduct(ProductDto productDto) {
        if (productExists((productDto.id()))) {
            throw new ProductAlreadyExistException(productDto.id());
        }
        Product newProduct = ProductMapper.fromProductDtoToProduct(productDto);
        productRepository.save(newProduct);
        return ProductMapper.fromProductToProductDto(newProduct);
    }


    public boolean productExists(UUID id) {
        return productRepository.findById(id).isPresent();
    }


    public ProductDto updateExistingProduct(UUID id, ProductDto productDto) {

        Product productToUpdate = productRepository.findById(id)
                .orElseThrow(() -> new NoDataFoundException("Product with id " + id + " not found"));
        productToUpdate.setTitle(productDto.title());
        productToUpdate.setSmallSummary(productDto.smallSummary());
        if (!Objects.equals(productDto.description(), productToUpdate.getDescription())) {
            productToUpdate.setDescription(productDto.description());
        }
        if (!Objects.equals(productDto.smallSummary(), productToUpdate.getSmallSummary())) {
            productToUpdate.setSmallSummary(productDto.smallSummary());
        }
        productToUpdate.setPrice(productDto.price());
        productToUpdate.setAmountInStock(productDto.amountInStock());
        productRepository.save(productToUpdate);

        return ProductMapper.fromProductToProductDto(productToUpdate);
    }
}
