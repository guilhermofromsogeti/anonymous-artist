package com.sogeti.java.anonymous_artist.repository;

import com.sogeti.java.anonymous_artist.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    List<CartItem> findByCartCartId(UUID cartId);

    void deleteByCartItemId(UUID cartItemId);
}
