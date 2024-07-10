package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.exception.NoDataFoundException;
import com.sogeti.java.anonymous_artist.factory.CartFactory;
import com.sogeti.java.anonymous_artist.factory.ProductFactory;
import com.sogeti.java.anonymous_artist.model.Cart;
import com.sogeti.java.anonymous_artist.model.CartItem;
import com.sogeti.java.anonymous_artist.model.Product;
import com.sogeti.java.anonymous_artist.repository.CartItemRepository;
import com.sogeti.java.anonymous_artist.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    UUID id;
    Cart cart;
    CartItem cartItem;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private CartItemService cartItemService;

    @BeforeEach
    void setup() {
        id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        cart = CartFactory.aCart().build();
        cartItem = CartFactory.aCartItem().build();
    }

    @Test
    void givenProductAndQuantity_whenCreatingCartItem_thenCartItemIsSuccessfullyCreated() {
        // Given
        Product product = ProductFactory.aProduct().id(id).price(BigDecimal.valueOf(25.00)).build();
        int quantity = 3;

        // When
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CartItem cartItem = cartItemService.createCartItem(product, quantity);

        // Then
        assertNotNull(cartItem);
        assertEquals(id, cartItem.getId());
        assertEquals(quantity, cartItem.getQuantity());
        assertEquals(product.getPrice(), cartItem.getProductPrice());
        assertEquals(product.getTitle(), cartItem.getProductTitle());
        assertEquals(product.getPrice().multiply(BigDecimal.valueOf(quantity)), cartItem.getSubTotalPrice());
        verify(cartItemRepository).save(any(CartItem.class));
        verifyNoMoreInteractions(cartItemRepository);
    }

    @Test
    void givenValidId_whenFindCartItemById_thenCartItemIsFound() {
        // Given
        cartItem.setId(id);

        // When
        when(cartItemRepository.findById(id)).thenReturn(Optional.of(cartItem));

        CartItem result = cartItemService.findCartItemById(id);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(cartItemRepository).findById(id);
        verifyNoMoreInteractions(cartItemRepository);
    }

    @Test
    void givenNonExistingId_whenFindCartItemById_thenNoDataFoundExceptionIsThrown() {
        // Given - when
        when(cartItemRepository.findById(id)).thenReturn(Optional.empty());

        // Execution and Verification
        Exception exception = assertThrows(NoDataFoundException.class, () -> {
            cartItemService.findCartItemById(id);
        });

        assertEquals("Cannot find data: Cart item with Id " + id + " not found", exception.getMessage());
        verify(cartItemRepository).findById(id);
        verifyNoMoreInteractions(cartItemRepository);
    }

    @Test
    void givenValidId_whenFindOptionalCartItemById_thenOptionalCartItemIsReturned() {
        // Given
        cartItem.setId(id);

        // When
        when(cartItemRepository.findById(id)).thenReturn(Optional.of(cartItem));

        Optional<CartItem> result = cartItemService.returnOptionalCartItemById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(cartItemRepository).findById(id);
        verifyNoMoreInteractions(cartItemRepository);
    }

    @Test
    void givenNonExistingId_whenFindOptionalCartItemById_thenOptionalEmptyIsReturned() {
        // Given - When
        when(cartItemRepository.findById(id)).thenReturn(Optional.empty());

        Optional<CartItem> result = cartItemService.returnOptionalCartItemById(id);

        // Verification
        assertTrue(result.isEmpty());
        verify(cartItemRepository).findById(id);
        verifyNoMoreInteractions(cartItemRepository);
    }

    @Test
    void givenCartAndCartItemAndNewQuantity_whenReplacingQuantityExistingCartItem_thenNewQuantityIsProperlySetToCartItem() {
        // Given
        int newQuantity = 5;

        // When
        cartItemService.replaceQuantityExistingCartItem(cart, cartItem, newQuantity);

        // Then
        assertEquals(newQuantity, cartItem.getQuantity());
    }

    @Test
    void givenCartAndCartItem_whenSetCartToCartItem_thenCartIsSuccessfullySetToCartItem() {
        // Given - When
        cartItemService.setCartToCartItem(cart, cartItem);

        // Then
        assertEquals(cart, cartItem.getCart());
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    void givenCartItemAndNewQuantity_whenIncreasingQuantityExistingCartItem_thenNewQuantityIsAddedToExistingQuantity() {
        // Given
        int currentQuantity = 2;
        cartItem.setQuantity(currentQuantity);
        int additionalQuantity = 3;
        int expectedQuantity = currentQuantity + additionalQuantity;

        // When
        cartItemService.increaseQuantityExistingCartItem(cartItem, additionalQuantity);

        // Then
        assertEquals(expectedQuantity, cartItem.getQuantity());
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    void givenNewProductPrice_whenUpdatingCartItemPricesAndSubtotals_thenPriceChangesAreSet() {
        // Given
        BigDecimal currentPriceInCartItem = BigDecimal.valueOf(10.00);
        BigDecimal newProductPrice = BigDecimal.valueOf(12.75);
        int quantity = 2;
        cartItem.setId(id);
        cartItem.setProductPrice(currentPriceInCartItem);
        cartItem.setSubTotalPrice(currentPriceInCartItem.multiply(BigDecimal.valueOf(quantity)));

        Product product = ProductFactory.aProduct().price(newProductPrice).build();

        // When
        when(cartItemRepository.findAll()).thenReturn(Collections.singletonList(cartItem));
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        cartItemService.updateCartItemPricesAndSubtotals();

        // Then
        verify(cartItemRepository).findAll();
        verify(productRepository).findById(id);
        assertEquals(newProductPrice, cartItem.getProductPrice());
        assertEquals(newProductPrice.multiply(BigDecimal.valueOf(quantity)), cartItem.getProductPrice().multiply(BigDecimal.valueOf(quantity)));
        verify(cartItemRepository).saveAll(anyList());
        verifyNoMoreInteractions(cartItemRepository);
    }

    @Test
    void givenSameProductPrice_whenUpdatingCartItemPricesAndSubtotals_thenNoPriceIsChanged() {
        // Given
        BigDecimal currentPriceInCartItem = BigDecimal.valueOf(10.00);
        BigDecimal unchangedProductPrice = BigDecimal.valueOf(10.00);
        int quantity = 2;
        cartItem.setId(id);
        cartItem.setProductPrice(currentPriceInCartItem);
        cartItem.setSubTotalPrice(currentPriceInCartItem.multiply(BigDecimal.valueOf(quantity)));

        Product product = ProductFactory.aProduct().price(unchangedProductPrice).build();

        // When - Then
        when(cartItemRepository.findAll()).thenReturn(Collections.singletonList(cartItem));
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        cartItemService.updateCartItemPricesAndSubtotals();

        assertEquals(currentPriceInCartItem, cartItem.getProductPrice());
        assertEquals(currentPriceInCartItem.multiply(BigDecimal.valueOf(quantity)), cartItem.getSubTotalPrice().multiply(BigDecimal.valueOf(quantity)));
    }
}
