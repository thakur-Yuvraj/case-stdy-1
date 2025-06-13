package com.cropdeal.auth.service;

import com.cropdeal.auth.dto.UserDto;
import com.cropdeal.auth.feign.UserServiceClient;
import com.cropdeal.auth.modal.Role;
import com.cropdeal.auth.modal.UserCredential;
import com.cropdeal.auth.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    UserServiceClient userServiceClient;

    public String addUser(UserDto userDto) {

        UserCredential userCredential = UserCredential
                .builder()
                .name(userDto.getFullName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .role(userDto.getRole())
                .build();

        ResponseEntity<String> response = userServiceClient.register(userDto);
        if(response.getBody()!=null && !response.getStatusCode().is2xxSuccessful() ) return response.getBody();
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        userCredentialRepository.save(userCredential);
        return "User added successfully";
    }

    public String generateToken(String username, Role role) {
        return jwtService.generateToken(username, role);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    public String removeUser(String email) {
        userServiceClient.removeUser(email);
        userCredentialRepository.deleteByEmail(email);
        return "User deleted";
    }
}
