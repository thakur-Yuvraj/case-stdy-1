package com.cropdeal.auth.service;

import com.cropdeal.auth.modal.UserCredential;
import com.cropdeal.auth.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    UserCredentialRepository userCredentialRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JWTService jwtService;

    public String addUser(UserCredential userCredential) {
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        userCredentialRepository.save(userCredential);
        return "User added successfully";
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

}
