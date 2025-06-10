package com.cropdeal.auth.repository;

import com.cropdeal.auth.modal.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Integer> {
    UserCredential findByName(String username);
}
