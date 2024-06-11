package com.sogeti.java.anonymous_artist.repository;

import com.sogeti.java.anonymous_artist.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
