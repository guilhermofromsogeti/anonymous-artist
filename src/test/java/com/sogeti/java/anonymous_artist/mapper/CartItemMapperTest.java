package com.sogeti.java.anonymous_artist.mapper;

import com.sogeti.java.anonymous_artist.dto.CartItemDto;
import com.sogeti.java.anonymous_artist.factory.CartFactory;
import com.sogeti.java.anonymous_artist.request.CartItemRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartItemMapperTest {

    @MockBean
    private CartItemMapper cartItemMapper;

    @Test
    void givenCartItemRequest_whenMappedToCartItemDTO_thenAllFieldsShouldBeMappedCorrectly() {
        // Given
        CartItemRequest cartItemRequest = CartFactory.aCartItemRequest().build();

        // When
        CartItemDto cartItemDto = CartItemMapper.fromCartItemRequestToDto(cartItemRequest);

        // Then
        assertEquals(cartItemDto.id(), cartItemRequest.id());
        assertEquals(cartItemDto.quantity(), cartItemRequest.quantity());
    }
}
