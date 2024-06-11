package com.sogeti.java.anonymous_artist.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;
    private String fullName;
    @Column(columnDefinition = "TEXT")
    private String shippingAddress;
    private String email;
    @Column(columnDefinition = "TEXT")
    private String phoneNumber;
    private LocalDateTime dateOfOrder;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<CartItem> cartItemList = new ArrayList<>();

    private BigDecimal totalPrice;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();


    public Order(UUID orderId, String fullName, String shippingAddress, String email, String phoneNumber, LocalDateTime dateOfOrder) {

        this.orderId = orderId;
        this.fullName = fullName;
        this.shippingAddress = shippingAddress;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfOrder = dateOfOrder;
    }

    public Order(String fullName, String shippingAddress, String email, String phoneNumber, LocalDateTime dateOfOrder, BigDecimal totalPrice) {
        this.fullName = fullName;
        this.shippingAddress = shippingAddress;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfOrder = dateOfOrder;
        this.totalPrice = totalPrice;
    }

    public void addCartItem(CartItem cartItem) {
        cartItemList.add(cartItem);
        cartItem.setOrder(this);
    }
}

