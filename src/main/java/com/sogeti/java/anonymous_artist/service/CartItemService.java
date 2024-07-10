package com.sogeti.java.anonymous_artist.service;

import com.sogeti.java.anonymous_artist.exception.NoDataFoundException;
import com.sogeti.java.anonymous_artist.exception.ProductNotFoundException;
import com.sogeti.java.anonymous_artist.model.Cart;
import com.sogeti.java.anonymous_artist.model.CartItem;
import com.sogeti.java.anonymous_artist.model.Product;
import com.sogeti.java.anonymous_artist.repository.CartItemRepository;
import com.sogeti.java.anonymous_artist.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartItemService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    protected CartItem createCartItem(Product product, int quantity) {
        CartItem newCartItem = new CartItem();

        newCartItem.setId(product.getId());
        newCartItem.setQuantity(quantity);
        newCartItem.setProductPrice(product.getPrice());
        newCartItem.setProductTitle(product.getTitle());
        newCartItem.calculateSubTotalPrice(quantity);
        cartItemRepository.save(newCartItem);

        return newCartItem;
    }

    protected CartItem findCartItemById(UUID id) {
        return cartItemRepository.findById(id).orElseThrow(
                () -> new NoDataFoundException("Cart item with Id " + id + " not found"));
    }

    public Optional<CartItem> returnOptionalCartItemById(UUID id) {
        return cartItemRepository.findById(id);
    }

    public void replaceQuantityExistingCartItem(Cart cart, CartItem itemToEdit, int newQuantity) {
        itemToEdit.setQuantity(newQuantity);
        itemToEdit.calculateSubTotalPrice(newQuantity);

        setCartToCartItem(cart, itemToEdit);

        cart.addCartItemToCartItemList(itemToEdit);
    }

    void setCartToCartItem(Cart cart, CartItem cartItem) {
        cartItem.setCart(cart);
        cartItemRepository.save(cartItem);
    }

    public void increaseQuantityExistingCartItem(CartItem itemToEdit, int newQuantity) {
        itemToEdit.setQuantity(itemToEdit.getQuantity() + newQuantity);
        int updatedQuantity = itemToEdit.getQuantity();
        itemToEdit.calculateSubTotalPrice(updatedQuantity);
        cartItemRepository.save(itemToEdit);
    }

    @Transactional
    public void updateCartItemPricesAndSubtotals() {
        List<CartItem> cartItems = cartItemRepository.findAll();
        boolean needsUpdate = false;

        for (CartItem cartItem : cartItems) {
            Product currentProduct = productRepository.findById(cartItem.getId())
                    .orElseThrow(ProductNotFoundException::new);

            BigDecimal currentProductPrice = currentProduct.getPrice();
            if (!Objects.equals(cartItem.getProductPrice(), currentProductPrice)) {
                cartItem.setProductPrice(currentProductPrice);
                needsUpdate = true;
            }

            BigDecimal newSubtotal = currentProductPrice.multiply(new BigDecimal(cartItem.getQuantity()));
            if (!Objects.equals(cartItem.getSubTotalPrice(), newSubtotal)) {
                cartItem.setSubTotalPrice(newSubtotal);
                needsUpdate = true;
            }
        }

        if (needsUpdate) {
            cartItemRepository.saveAll(cartItems);
        }
    }


}
