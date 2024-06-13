package com.sogeti.java.anonymous_artist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sogeti.java.anonymous_artist.config.SpringSecurityConfig;
import com.sogeti.java.anonymous_artist.dto.ProductDto;
import com.sogeti.java.anonymous_artist.factory.ProductFactory;
import com.sogeti.java.anonymous_artist.request.ProductRequest;
import com.sogeti.java.anonymous_artist.response.ProductResponse;
import com.sogeti.java.anonymous_artist.service.CustomUserDetailsService;
import com.sogeti.java.anonymous_artist.service.ProductService;
import com.sogeti.java.anonymous_artist.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(SpringSecurityConfig.class)
class ProductControllerTest {

    @MockBean
    CustomUserDetailsService userDetailsService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private ProductService productService;

    @Test
    @WithAnonymousUser
    void givenProductExists_whenGetProductById_thenReturnProductResponse() throws Exception {
        // Given
        ProductDto product = ProductFactory.aProductDto().build();
        ProductResponse productResponse = ProductFactory.fromProductDtoToProductResponse(product);

        // When
        when(productService.getProductById(product.id())).thenReturn(product);

        // Then
        mockMvc.perform(get("/anonymous-artist/api/products/" + product.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(productResponse.id().toString()))
                .andExpect(jsonPath("title").value(productResponse.title()))
                .andExpect(jsonPath("smallSummary").value(productResponse.smallSummary()))
                .andExpect(jsonPath("description").value(productResponse.description()))
                .andExpect(jsonPath("price").value(productResponse.price()));

        // Verify
        verify(productService).getProductById(product.id());
        verifyNoMoreInteractions(productService);
    }

    @Test
    @WithAnonymousUser
    void givenProductDoesNotExist_whenGetProductById_thenReturn404() throws Exception {
        // Given
        UUID idToFind = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        // When
        when(productService.getProductById(idToFind)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No product found by id."));

        // Then
        mockMvc.perform(get("/anonymous-artist/api/products/{id}", idToFind))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    void givenListOfProductsExists_whenRequestingAllProducts_thenReturnListOfProductDtos() throws Exception {
        // Given
        List<ProductDto> listOfProductDtos = List.of(
                // Product 1
                ProductFactory.aProductDto().build(),
                // Product 2
                ProductFactory.aProductDto().build());

        List<ProductResponse> productDtoList = listOfProductDtos.stream().map(ProductFactory::fromProductDtoToProductResponse).toList();

        // When
        when(productService.getListOfAllProducts()).thenReturn(listOfProductDtos);

        // Then
        mockMvc.perform(get("/anonymous-artist/api/products/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("size()").value(2))
                .andExpect(jsonPath("[0].id").value(productDtoList.get(0).id().toString()))
                .andExpect(jsonPath("[0].title").value(productDtoList.get(0).title()))
                .andExpect(jsonPath("[0].smallSummary").value(productDtoList.get(0).smallSummary()))
                .andExpect(jsonPath("[0].description").value(productDtoList.get(0).description()))
                .andExpect(jsonPath("[0].price").value(productDtoList.get(0).price())).andExpect(jsonPath("size()").value(2))
                .andExpect(jsonPath("[1].id").value(productDtoList.get(1).id().toString()))
                .andExpect(jsonPath("[1].title").value(productDtoList.get(1).title()))
                .andExpect(jsonPath("[1].smallSummary").value(productDtoList.get(1).smallSummary()))
                .andExpect(jsonPath("[1].description").value(productDtoList.get(1).description()))
                .andExpect(jsonPath("[1].price").value(productDtoList.get(1).price()));

        // Verify that the service method was called
        verify(productService).getListOfAllProducts();
        verifyNoMoreInteractions(productService);
    }


    @Test
    @WithAnonymousUser
    void givenListOfProductsDoesNotExist_whenRequestingAllProducts_thenEmptyList() throws Exception {
        // Given
        List<ProductDto> productDtoList = List.of();


        // When
        when(productService.getListOfAllProducts()).thenReturn(productDtoList);

        // Then
        mockMvc.perform(get("/anonymous-artist/api/products/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("size()").value(0));

        // Verify that the service method was called
        verify(productService).getListOfAllProducts();
        verifyNoMoreInteractions(productService);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void givenValidRequest_andValidCredentialsAndRole_whenRegisteringANewProductInTheProductStore_thenANewProductIsRegistered() throws Exception {
        // Given
        ProductRequest validProductRequest = ProductFactory.aProductRequest().build();
        ProductDto newProduct = ProductFactory.fromProductRequestToProductDto(validProductRequest);

        // When
        when(productService.registerNewProduct(newProduct)).thenReturn(newProduct);

        // Then
        MvcResult mvcResult = mockMvc.perform(post("/anonymous-artist/api/products/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        assertEquals("Product with id " + newProduct.id() + " is successfully added to the artist store.", mvcResult.getResponse().getContentAsString());

        verify(productService).registerNewProduct(any(ProductDto.class));
        verifyNoMoreInteractions(productService);
    }

    @Test
    @WithMockUser(username = "test@administrator.com", authorities = "ROLE_ADMIN")
    void givenInvalidRequest_andValidCredentialsAndRole_whenRegisteringNewProduct_thenBadRequestIsPassed() throws Exception {
        // Given
        ProductRequest invalidProductRequest = ProductFactory.aProductRequest()
                .smallSummary("")
                .build();

        // When
        when(productService.registerNewProduct(any(ProductDto.class))).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        // Then
        mockMvc.perform(post("/anonymous-artist/api/products/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProductRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test@administrator.com", authorities = "ROLE_ADMIN")
    void givenValidRequest_andAdminRights_whenUpdatingAProduct_thenSuccessMessageIsDisplayed() throws Exception {
        // Given
        UUID existingId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        ProductRequest newProductInfo = ProductFactory.aProductRequest()
                .id(existingId)
                .build();
        ProductDto ProductToEdit = ProductFactory.fromProductRequestToProductDto(newProductInfo);

        // When
        when(productService.updateExistingProduct(existingId, ProductToEdit)).thenReturn(ProductToEdit);

        // Then
        MvcResult mvcResult = mockMvc.perform(put("/anonymous-artist/api/products/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProductInfo))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals("Product with id " + existingId + " is successfully updated.", mvcResult.getResponse().getContentAsString());

        verify(productService).updateExistingProduct(any(UUID.class), any(ProductDto.class));
        verifyNoMoreInteractions(productService);
    }


    @Test
    @WithMockUser(username = "test@administrator.com", authorities = "ROLE_ADMIN")
    void givenInvalidRequest_andAdminRights_whenUpdatingAProduct_thenBadRequestIsDisplayed() throws Exception {
        // Given
        UUID existingId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        ProductRequest invalidRequest = ProductFactory.aProductRequest()
                .id(existingId)
                .title("")
                .build();

        // When
        BindingResult bindingResult = new BeanPropertyBindingResult(invalidRequest, "productRequest");
        bindingResult.rejectValue("title", "NotEmpty");


        MethodParameter methodParameter = new MethodParameter(
                ProductService.class.getMethod("updateExistingProduct", UUID.class, ProductDto.class), 0);
        doAnswer(invocation -> {
            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
        }).when(productService).updateExistingProduct(eq(existingId), any(ProductDto.class));

        // Then
        mockMvc.perform(put("/anonymous-artist/api/products/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
