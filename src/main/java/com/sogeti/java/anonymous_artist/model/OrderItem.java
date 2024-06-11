package com.sogeti.java.anonymous_artist.model;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderItemId;

    private String productTitle;

    private int quantity;

    private BigDecimal unitPrice;

    private BigDecimal subTotalPrice;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItem(String productTitle, int quantity, BigDecimal unitPrice, BigDecimal subTotalPrice) {
        this.productTitle = productTitle;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subTotalPrice = subTotalPrice;
    }
}
