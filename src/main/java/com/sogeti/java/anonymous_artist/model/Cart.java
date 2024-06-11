package com.sogeti.java.anonymous_artist.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "cart")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartId;
    private BigDecimal totalPrice;

    @OneToOne
    @JoinColumn(name = "membership_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItemList = new ArrayList<>();

    public Cart(BigDecimal totalPrice, List<CartItem> cartItemList) {
        this.totalPrice = totalPrice;
        this.cartItemList = cartItemList;
    }

    public void addCartItemToCartItemList(CartItem cartItem) {
        this.cartItemList.add(cartItem);
    }

    public void removeCartItem(CartItem cartItem) {
        this.cartItemList.remove(cartItem);
    }
}