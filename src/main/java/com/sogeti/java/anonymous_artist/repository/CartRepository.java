package com.sogeti.java.anonymous_artist.repository;

import com.sogeti.java.anonymous_artist.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Cart findCartByUserMembershipId(UUID membershipId);
}
