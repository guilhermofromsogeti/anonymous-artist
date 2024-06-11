package com.sogeti.java.anonymous_artist.repository;

import com.sogeti.java.anonymous_artist.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByEmail(String email);
}
