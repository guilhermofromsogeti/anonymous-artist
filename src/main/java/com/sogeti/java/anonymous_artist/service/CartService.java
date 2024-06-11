package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.dto.CartDto;
import com.sogeti.java.anonymous_artist.dto.CartItemDto;
import com.sogeti.java.anonymous_artist.exception.NoDataFoundException;
import com.sogeti.java.anonymous_artist.mapper.CartMapper;
import com.sogeti.java.anonymous_artist.model.Account;
import com.sogeti.java.anonymous_artist.model.Cart;
import com.sogeti.java.anonymous_artist.model.CartItem;
import com.sogeti.java.anonymous_artist.model.Product;
import com.sogeti.java.anonymous_artist.repository.AccountRepository;
import com.sogeti.java.anonymous_artist.repository.CartItemRepository;
import com.sogeti.java.anonymous_artist.repository.CartRepository;
import com.sogeti.java.anonymous_artist.repository.ProductRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemService cartItemService;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    public CartService(CartRepository cartRepository, CartItemService cartItemService, CartItemRepository cartItemRepository, ProductRepository productRepository, AccountRepository accountRepository) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
    }

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public Cart getCurrentUserCart() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Account account = accountRepository.findById(username).orElseThrow();

        return cartRepository.findCartByUserMembershipId(account.getUser().getMembershipId());
    }

    public Optional<CartItem> returnOptionalCartItemFromCartByIsbn(Cart cart, UUID isbn) {
        return cart.getCartItemList().stream()
                .filter(cartItem -> cartItem.getId().equals(isbn)).findFirst();
    }

    public CartItem returnCartItemFromCartByIsbn(Cart cart, String isbn) {
        return cart.getCartItemList().stream()
                .filter(cartItem -> cartItem.getId().equals(isbn))
                .findFirst()
                .orElseThrow(() -> new NoDataFoundException("Cart item with ISBN " + isbn + " not found"));
    }

    public void addCartItemToCart(CartItemDto cartItemDto) {
        UUID receivedID = cartItemDto.id();
        int newQuantity = cartItemDto.quantity();

        Cart cart = getCurrentUserCart();
        Product product = findProductById(receivedID);

        Optional<CartItem> optionalCartItem = returnOptionalCartItemFromCartByIsbn(cart, receivedID);

        if (optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();
            cartItemService.increaseQuantityExistingCartItem(cartItem, newQuantity);
        } else {
            CartItem createdCartItem = cartItemService.createCartItem(product, newQuantity);
            cartItemService.setCartToCartItem(cart, createdCartItem);
            setCartItemToCart(createdCartItem, cart);
        }

        updateCartTotalPrice(cart);
    }

    public String updateQuantityCartItem(CartItemDto cartItemDto) {
        UUID id = cartItemDto.id();
        int newQuantity = cartItemDto.quantity();
        Cart cart = getCurrentUserCart();

        if (newQuantity == 0) {
            return removeCartItemFromCart(cart, id);
        } else {
            CartItem itemToEdit = returnCartItemFromCartByIsbn(cart, String.valueOf(id));
            cartItemService.replaceQuantityExistingCartItem(cart, itemToEdit, newQuantity);
            updateCartTotalPrice(cart);
        }

        return "The quantity of the cart item with  " + id + " is successfully updated to: " + newQuantity;

    }

    String removeCartItemFromCart(Cart cart, UUID isbn) {
        Optional<CartItem> optionalCartItem = returnOptionalCartItemFromCartByIsbn(cart, isbn);
        if (optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();

            cart.removeCartItem(cartItem);
            updateCartTotalPrice(cart);

            cartItemRepository.deleteByCartItemId(cartItem.getCartItemId());
            return "The cart item is removed.";
        }

        return new NoDataFoundException("Cart item").getMessage();
    }

    void setCartItemToCart(CartItem cartItem, Cart cart) {
        cart.addCartItemToCartItemList(cartItem);
        cartRepository.save(cart);
    }

    Product findProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoDataFoundException("Product with ID " + id + " not found"));
    }

    public CartDto getOverviewCurrentCart() {
        Cart cart = getCurrentUserCart();
        cartItemService.updateCartItemPricesAndSubtotals();
        updateCartTotalPrice(cart);
        return CartMapper.fromCartToCartDto(cart);
    }

    void updateCartTotalPrice(Cart cart) {
        List<CartItem> cartItems = cartItemRepository.findByCartCartId(cart.getCartId());
        BigDecimal newTotalPrice = cartItems.stream()
                .map(CartItem::getSubTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(newTotalPrice);
        cartRepository.save(cart);
    }


}
