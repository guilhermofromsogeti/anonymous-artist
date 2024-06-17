package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.dto.ProductDto;
import com.sogeti.java.anonymous_artist.exception.NoDataFoundException;
import com.sogeti.java.anonymous_artist.exception.ProductAlreadyExistException;
import com.sogeti.java.anonymous_artist.exception.ProductNotFoundException;
import com.sogeti.java.anonymous_artist.factory.ProductFactory;
import com.sogeti.java.anonymous_artist.mapper.ProductMapper;
import com.sogeti.java.anonymous_artist.model.Product;
import com.sogeti.java.anonymous_artist.repository.FileUploadRepository;
import com.sogeti.java.anonymous_artist.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private FileUploadRepository fileUploadRepository;

    @InjectMocks
    private ProductService productService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private GrantedAuthority grantedAuthority;

    @Mock
    ProductMapper productMapper;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void givenProductExist_whenGetProductById_thenProductIsFound() {
        // Given
        ProductDto mockProductDto = ProductFactory.aProductDto().build();

        // When
        when(productRepository.findById(mockProductDto.id())).thenReturn(Optional.of(ProductFactory.fromProductDtoToProduct(mockProductDto)));
        ProductDto returnedProduct = productService.getProductById(mockProductDto.id());

        // Then
        verify(productRepository).findById(mockProductDto.id());
        assertNotNull(returnedProduct);
        assertEquals(mockProductDto, returnedProduct);
    }


    @Test
    void givenProductNotExist_whenGetProductById_thenNoProductIsReturned() {
        // Given
        UUID nonExistingId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        // When
        when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            productService.getProductById(nonExistingId);
        });

        // Then
        verify(productRepository).findById(nonExistingId);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Product not found", exception.getReason());
    }


    @Test
    void givenProductIdAlreadyExistInProductStore_whenRegisteringAProductInStore_thenProductAlreadyExistExceptionIsThrown() {
        // Given
        UUID existingId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Product existingProduct = ProductFactory.aProduct()
                .id(existingId)
                .build();
        ProductDto newProduct = ProductFactory.aProductDto()
                .id(existingId)
                .build();

        // When
        when(productRepository.findById(existingId)).thenThrow(new ProductAlreadyExistException(existingProduct.getId()));

        // Then
        assertThrows(ProductAlreadyExistException.class, () -> {
            productService.registerNewProduct(newProduct);
        });

        verify(productRepository).findById(existingId);
        verify(productRepository, never()).save(any());
        verifyNoMoreInteractions(productRepository);
    }


    @Test
    void givenProductExists_whenFindingById_thenProductExistTrueIsReturned() {
        // Given
        Product mockProduct = ProductFactory.aProduct().build();

        // When
        when(productRepository.findById(mockProduct.getId())).thenReturn(Optional.of(mockProduct));
        boolean result = productService.productExists(mockProduct.getId());

        // Then
        verify(productRepository).findById(mockProduct.getId());
        assertTrue(result);
        verifyNoMoreInteractions(productRepository);
    }


    @Test
    void givenProductDoesNotExist_whenFindingById_thenProductExistFalseIsReturned() {
        // Given
        Product newProduct = ProductFactory.aProduct().build();
        newProduct.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));

        // When
        when(productRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"))).thenReturn(Optional.empty());
        boolean result = productService.productExists(newProduct.getId());

        // Then
        verify(productRepository).findById(newProduct.getId());
        assertFalse(result);
        verifyNoMoreInteractions(productRepository);
    }


    @Test
    void givenProductToUpdate_whenProductExists_thenUpdatesAreCorrectlySetAndSaved() {
        // Given
        UUID idToSearch = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Product existingProduct = ProductFactory.aProduct()
                .id(idToSearch)
                .build();
        ProductDto newProductInfo = ProductFactory.aProductDto()
                .id(idToSearch)
                .build();

        // When
        when(productRepository.findById(idToSearch)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);
        productService.updateExistingProduct(idToSearch, newProductInfo);

        // Then
        verify(productRepository).findById(idToSearch);
        verify(productRepository).save(existingProduct);
        assertEquals(existingProduct.getId(), newProductInfo.id());
        assertEquals(existingProduct.getTitle(), newProductInfo.title());
        assertEquals(existingProduct.getSmallSummary(), newProductInfo.smallSummary());
        assertEquals(existingProduct.getDescription(), newProductInfo.description());
        assertEquals(existingProduct.getPrice(), newProductInfo.price());
        verifyNoMoreInteractions(productRepository);
    }


    @Test
    void givenProductToUpdate_whenProductDoesNotExist_thenProductNotFoundExceptionIsThrown() {
        // Given
        UUID idToSearch = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        ProductDto newProductInfo = ProductFactory.aProductDto()
                .id(idToSearch)
                .build();

        // When
        when(productRepository.findById(idToSearch)).thenThrow(new ProductNotFoundException());

        // Then
        assertThrows(ProductNotFoundException.class, () -> {
            productService.updateExistingProduct(idToSearch, newProductInfo);
        });

        verify(productRepository).findById(idToSearch);
        verify(productRepository, never()).save(any());
        verifyNoMoreInteractions(productRepository);
    }


    @Test
    void givenAUserAndAdminRolesArePresent_whenGettingARoleFromJwtFilterAndSecurityContext_thenAuthenticationShouldReturnBothRoleUserAndRoleAdmin() {
        // Given
        Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password", authorities);

        // When
        when(securityContext.getAuthentication()).thenReturn(authentication);

        ProductService productService = new ProductService(productRepository, fileUploadRepository);
        List<String> roles = productService.getUserRoles();

        // Then
        assertEquals(2, roles.size());
        assertTrue(roles.contains("ROLE_USER"));
        assertTrue(roles.contains("ROLE_ADMIN"));

        verify(securityContext).getAuthentication();
        verifyNoMoreInteractions(securityContext);
    }


    @Test
    void givenAUserRoleIsEmpty_whenGettingARoleFromJwtFilterAndSecurityContext_thenAuthenticationShouldReturnARoleEmpty() {
        // Given
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password", Collections.emptyList());
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // When
        ProductService productService1 = new ProductService(productRepository, fileUploadRepository);
        List<String> roles = productService1.getUserRoles();

        // Then
        assertTrue(roles.isEmpty());
        verify(securityContext).getAuthentication();
        verifyNoMoreInteractions(securityContext);
    }


    @Test
    void givenValidRequestAndExistingId_whenUpdatingExistingProduct_thenAExistingProductShouldBeUpdated() {
        // Given
        UUID idToUpdate = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        ProductDto productDto = ProductFactory.aProductDto().build();

        Product existingProduct = new Product();
        existingProduct.setId(idToUpdate);
        existingProduct.setTitle("Old Title");
        existingProduct.setSmallSummary("Old Summary");
        existingProduct.setDescription("Old Description");
        existingProduct.setPrice(BigDecimal.valueOf(10.00));
        existingProduct.setAmountInStock(1);

        // When
        when(productRepository.findById(idToUpdate)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);
        ProductDto updatedProductDto = productService.updateExistingProduct(idToUpdate, productDto);

        // Then
        assertNotNull(updatedProductDto);
        assertEquals(productDto.title(), updatedProductDto.title());
        assertEquals(productDto.smallSummary(), updatedProductDto.smallSummary());
        assertEquals(productDto.description(), updatedProductDto.description());
        assertEquals(productDto.price(), updatedProductDto.price());
        assertEquals(productDto.amountInStock(), updatedProductDto.amountInStock());

        verify(productRepository).findById(idToUpdate);
        verify(productRepository).save(any(Product.class));
    }


    @Test
    void givenNonExistingProduct_whenUpdatingExistingProduct_thenThrowProductNotFoundException() {
        // Given
        UUID idToUpdate = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        ProductDto ProductDto = ProductFactory.aProductDto().id(idToUpdate).build();

        // When
        when(productRepository.findById(idToUpdate)).thenReturn(Optional.empty());

        // Then
        assertThrows(NoDataFoundException.class, () -> {
            productService.updateExistingProduct(idToUpdate, ProductDto);
        });

        verify(productRepository).findById(idToUpdate);
        verify(productRepository, never()).save(any());
    }


    @Test
    void givenAValidRequest_whenGettingListForAdmin_thenListOfAllProductsShouldBeReturned() {
        // Given
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(ProductFactory.aProduct().build());
        mockProducts.add(ProductFactory.aProduct().amountInStock(0).build());

        // When
        when(productRepository.findAll()).thenReturn(mockProducts);

        List<ProductDto> ProductDtos = productService.getListOfAllProductsForAdmin();

        // Then
        verify(productRepository).findAll();
        assertEquals(mockProducts.size(), ProductDtos.size());
        assertEquals(mockProducts.get(0).getAmountInStock(), ProductDtos.get(0).amountInStock());
        assertEquals(mockProducts.get(1).getAmountInStock(), ProductDtos.get(1).amountInStock());
    }


    @Test
    void givenAValidRequest_whenGettingListForUser_thenListOfAllProductsShouldBeReturned() {
        // Given
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(ProductFactory.aProduct().build());
        mockProducts.add(ProductFactory.aProduct().build());
        mockProducts.add(ProductFactory.aProduct().amountInStock(0).build());
        when(productRepository.findAll()).thenReturn(mockProducts);

        // When
        List<ProductDto> ProductDtos = productService.getListOfAllProductsForUser();

        // Then
        assertEquals(2, ProductDtos.size());
        assertEquals(3, mockProducts.size());
        assertEquals(mockProducts.size() - 1, ProductDtos.size());
        assertFalse(ProductDtos.contains(ProductFactory.fromProductToProductDto(mockProducts.get(2))));
        verify(productRepository).findAll();
    }


    @Test
    void givenRoleUser_whenGettingAProductList_thenOnlyProductInStockShouldBeShown() {
        // Given
        Collection<? extends GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password", authorities);

        // When
        when(securityContext.getAuthentication()).thenReturn(authentication);

        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(ProductFactory.aProduct().build());
        mockProducts.add(ProductFactory.aProduct().build());
        mockProducts.add(ProductFactory.aProduct().amountInStock(0).build());
        when(productRepository.findAll()).thenReturn(mockProducts);

        // Then
        List<ProductDto> ProductDtos = productService.getListOfAllProducts();
        assertEquals(2, ProductDtos.size());
        assertEquals(3, mockProducts.size());
        assertEquals(mockProducts.size() - 1, ProductDtos.size());
        assertEquals(mockProducts.get(0).getAmountInStock(), ProductDtos.get(0).amountInStock());
        assertEquals(mockProducts.get(1).getAmountInStock(), ProductDtos.get(1).amountInStock());
        assertFalse(ProductDtos.contains(ProductFactory.fromProductToProductDto(mockProducts.get(2))));

        verify(securityContext).getAuthentication();
        verify(productRepository).findAll();
    }


    @Test
    void givenRoleAdmin_whenGettingProductList_thenAllProductsInAndOutOfStockAreShown() {
        // Given
        Collection<? extends GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password", authorities);

        // When
        when(securityContext.getAuthentication()).thenReturn(authentication);
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(ProductFactory.aProduct().build());
        mockProducts.add(ProductFactory.aProduct().amountInStock(0).build());
        when(productRepository.findAll()).thenReturn(mockProducts);


        // Then
        List<ProductDto> ProductDtos = productService.getListOfAllProducts();
        assertEquals(2, ProductDtos.size());
        assertEquals(2, mockProducts.size());
        assertEquals(mockProducts.size(), ProductDtos.size());
        assertEquals(mockProducts.get(0).getAmountInStock(), ProductDtos.get(0).amountInStock());
        assertEquals(mockProducts.get(1).getAmountInStock(), ProductDtos.get(1).amountInStock());

        verify(securityContext).getAuthentication();
        verify(productRepository).findAll();
    }
}