package com.cropdeal.dealer.repository;

import com.cropdeal.dealer.modal.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DealerRepository extends JpaRepository<Dealer, Integer> {
    Optional<Dealer> findByEmail(String email);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);
}