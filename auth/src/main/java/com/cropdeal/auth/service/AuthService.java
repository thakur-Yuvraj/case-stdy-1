package com.cropdeal.auth.service;

import com.cropdeal.auth.dto.UserDto;
import com.cropdeal.auth.feign.UserServiceClient;
import com.cropdeal.auth.modal.Role;
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

        String responseBody = userServiceClient.register(userDto).getBody();
        if(responseBody!=null && !responseBody.equals("Registered successfully")) return responseBody;
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

}
