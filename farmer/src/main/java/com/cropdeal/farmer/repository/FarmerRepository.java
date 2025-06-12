package com.cropdeal.farmer.repository;

import com.cropdeal.farmer.modal.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Integer> {
    Optional<Farmer> findByEmail(String email);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);
}