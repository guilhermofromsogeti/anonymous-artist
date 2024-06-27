package com.sogeti.java.anonymous_artist.controller;

import com.sogeti.java.anonymous_artist.dto.ProductDto;
import com.sogeti.java.anonymous_artist.mapper.ProductMapper;
import com.sogeti.java.anonymous_artist.model.FileUploadResponse;
import com.sogeti.java.anonymous_artist.request.ProductRequest;
import com.sogeti.java.anonymous_artist.response.ProductResponse;
import com.sogeti.java.anonymous_artist.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/anonymous-artist/api/product/")
public class ProductController {


    private final ProductService productService;
    private final ImageController imageController;

    public ProductController(ProductService productService, ImageController imageController) {
        this.productService = productService;
        this.imageController = imageController;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getListOfAllProducts() {
        List<ProductResponse> productResponseList = productService.getListOfAllProducts()
                .stream()
                .map(ProductMapper::fromProductDtoToProductResponse)
                .toList();
        return ResponseEntity.ok().body(productResponseList);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        ProductDto productDto = productService.getProductById(id);
        ProductResponse productResponse = ProductMapper.fromProductDtoToProductResponse(productDto);
        return ResponseEntity.ok().body(productResponse);
    }


    @PostMapping
    public ResponseEntity<String> registerNewProduct(@RequestBody @Valid ProductRequest productRequest) {
        ProductDto productDto = ProductMapper.fromProductRequestToDto(productRequest);
        ProductDto newProductDto = productService.registerNewProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product with id " + newProductDto.id() + " is successfully added to the artist store.");
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateProduct(@PathVariable UUID id, @RequestBody @Valid ProductRequest productRequest) {
        ProductDto productDto = ProductMapper.fromProductRequestToDto(productRequest);
        ProductDto productToEditDto = productService.updateExistingProduct(id, productDto);
        return ResponseEntity.ok().body("Product with id " + productToEditDto.id() + " is successfully updated.");
    }

    @PostMapping("{id}/image")
    public ResponseEntity<Object> assignImageToProduct(@PathVariable("id") UUID productId, @RequestBody MultipartFile file) {
        FileUploadResponse productImage = imageController.singleFileUpload(file);
        productService.assignImageToProduct(productImage.getFileName(), productId);
        return ResponseEntity.noContent().build();
    }

}