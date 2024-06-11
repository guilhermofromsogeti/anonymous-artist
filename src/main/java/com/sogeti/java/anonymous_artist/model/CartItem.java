package com.sogeti.java.anonymous_artist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "cart_item")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {

    @Id
    @Column(name = "cart_item_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartItemId;
    private UUID id;
    private String productTitle;
    private int quantity;
    private BigDecimal productPrice;
    private BigDecimal subTotalPrice;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;


    public CartItem(String productTitle, int quantity, BigDecimal productPrice, BigDecimal subTotalPrice) {
        this.productTitle = productTitle;
        this.quantity = quantity;
        this.productPrice = productPrice;
        this.subTotalPrice = subTotalPrice;
    }

    public CartItem(String productTitle, BigDecimal productPrice) {
        this.productTitle = productTitle;
        this.productPrice = productPrice;
    }

    public void calculateSubTotalPrice(int quantity) {
        subTotalPrice = this.productPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
