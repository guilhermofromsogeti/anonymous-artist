package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.dto.ProductDto;
import com.sogeti.java.anonymous_artist.exception.NoDataFoundException;
import com.sogeti.java.anonymous_artist.exception.ProductAlreadyExistException;
import com.sogeti.java.anonymous_artist.exception.ProductNotFoundException;
import com.sogeti.java.anonymous_artist.mapper.ProductMapper;
import com.sogeti.java.anonymous_artist.model.FileUploadResponse;
import com.sogeti.java.anonymous_artist.model.Product;
import com.sogeti.java.anonymous_artist.repository.FileUploadRepository;
import com.sogeti.java.anonymous_artist.repository.ProductRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final FileUploadRepository fileUploadRepository;

    public ProductService(ProductRepository productRepository, FileUploadRepository fileUploadRepository) {
        this.productRepository = productRepository;
        this.fileUploadRepository = fileUploadRepository;
    }


    public List<ProductDto> getListOfAllProductsForAdmin() {
        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream()
                .map(ProductMapper::fromProductToProductDto)
                .toList();
    }

    public List<ProductDto> getListOfAllProductsForUser() {
        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream()
                .filter(product -> isValidStock(product.getAmountInStock()))
                .map(ProductMapper::fromProductToProductDto)
                .toList();
    }


    public List<ProductDto> getListOfAllProducts() {
        List<String> roles = getUserRoles();

        if (roles.contains("ROLE_ADMIN")) {
            return getListOfAllProductsForAdmin();
        } else if (roles.contains("ROLE_USER")) {
            return getListOfAllProductsForUser();
        } else if (roles.stream().noneMatch(String::isBlank)) {
            return getListOfAllProductsForUser();
        } else {
            throw new IllegalStateException("unexpected user role, please contact the administrator");
        }
    }

    List<String> getUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Collections.emptyList();
        }
        return authentication.getAuthorities().stream()
                .map(Object::toString)
                .toList();
    }

    boolean isValidStock(Integer stock) {
        return stock != null && stock >= 1;
    }

    public ProductDto getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
        return ProductMapper.fromProductToProductDto(product);
    }

    public boolean productExists(UUID id) {
        return productRepository.findById(id).isPresent();
    }

    public ProductDto registerNewProduct(ProductDto productDto) {
        if (productExists(productDto.id())) {
            throw new ProductAlreadyExistException(productDto.id());
        }
        Product newProduct = ProductMapper.fromProductDtoToProduct(productDto);
        productRepository.save(newProduct);
        return ProductMapper.fromProductToProductDto(newProduct);
    }

    public ProductDto updateExistingProduct(UUID idToUpdate, ProductDto productDto) {

        Product productToUpdate = productRepository.findById(idToUpdate)
                .orElseThrow(() -> new NoDataFoundException("Product with id " + idToUpdate + " not found"));
        productToUpdate.setTitle(productDto.title());
        if (!Objects.equals(productDto.smallSummary(), productToUpdate.getSmallSummary())) {
            productToUpdate.setSmallSummary(productDto.smallSummary());
        }
        if (!Objects.equals(productDto.description(), productToUpdate.getDescription())) {
            productToUpdate.setDescription(productDto.description());
        }
        productToUpdate.setPrice(productDto.price());
        productToUpdate.setAmountInStock(productDto.amountInStock());
        productRepository.save(productToUpdate);

        return ProductMapper.fromProductToProductDto(productToUpdate);
    }

    public void assignImageToProduct(String fileName, UUID productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Optional<FileUploadResponse> fileUploadResponse = fileUploadRepository.findByFileName(fileName);

        if (optionalProduct.isPresent() && fileUploadResponse.isPresent()) {
            FileUploadResponse image = fileUploadResponse.get();
            Product product = optionalProduct.get();

            product.addImage(image);
            image.setProduct(product);
            productRepository.save(product);
            ProductMapper.fromProductToProductDto(product);
        } else {
            throw new NoDataFoundException("no product found with this id: " + productId);
        }
    }
}
