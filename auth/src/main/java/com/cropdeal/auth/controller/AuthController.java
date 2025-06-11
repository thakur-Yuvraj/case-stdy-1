package com.cropdeal.auth.controller;


import com.cropdeal.auth.dto.AuthRequest;
import com.cropdeal.auth.dto.UserDto;
import com.cropdeal.auth.modal.UserCredential;
import com.cropdeal.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("register")
    public String addNewUser(@RequestBody UserDto userDto) {
        return authService.addUser(userDto);
    }

    // this is for user login
    @PostMapping("token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getName(), authRequest.getPassword()));
        if(authentication.isAuthenticated()) {
            return authService.generateToken(authRequest.getName(), authRequest.getRole());
        }
        return "Invalid Name or password";
    }

    @GetMapping("validate")
    public String validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        return "Token is valid";
    }
}
