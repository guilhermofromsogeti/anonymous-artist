package com.sogeti.java.anonymous_artist.repository;

import com.sogeti.java.anonymous_artist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}
