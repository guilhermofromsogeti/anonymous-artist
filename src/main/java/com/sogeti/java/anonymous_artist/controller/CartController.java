package com.sogeti.java.anonymous_artist.controller;

import com.sogeti.java.anonymous_artist.dto.CartDto;
import com.sogeti.java.anonymous_artist.dto.CartItemDto;
import com.sogeti.java.anonymous_artist.mapper.CartItemMapper;
import com.sogeti.java.anonymous_artist.mapper.CartMapper;
import com.sogeti.java.anonymous_artist.request.CartItemEditRequest;
import com.sogeti.java.anonymous_artist.request.CartItemRequest;
import com.sogeti.java.anonymous_artist.response.CartResponse;
import com.sogeti.java.anonymous_artist.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/anonymous-artist/api/cart/")
public class CartController {

    private final CartService cartService;


    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("add/")
    public ResponseEntity<String> addCartItemToCart(@RequestBody @Valid CartItemRequest cartItemRequest) {
        CartItemDto cartItemDto = CartItemMapper.fromCartItemRequestToDto(cartItemRequest);
        cartService.addCartItemToCart(cartItemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product with id " + cartItemDto.id() + " is successfully added to the cart");
    }

    @PutMapping("edit/")
    public ResponseEntity<String> editCartItem(@RequestBody @Valid CartItemEditRequest cartItemEditRequest) {
        CartItemDto cartItemDto = CartItemMapper.fromCartItemEditRequestToDto(cartItemEditRequest);
        String responseMessage = cartService.updateQuantityCartItem(cartItemDto);
        return ResponseEntity.ok().body(responseMessage);
    }

    @GetMapping()
    public ResponseEntity<CartResponse> getAllCartItemsInCurrentCart() {
        CartDto cartDto = cartService.getOverviewCurrentCart();
        CartResponse cartResponse = CartMapper.fromCartDtoToCartResponse(cartDto);
        return ResponseEntity.ok().body(cartResponse);
    }
}