package com.cropdeal.user.controller;

import com.cropdeal.user.dto.UserDto;
import com.cropdeal.user.exception.UserServiceException;
import com.cropdeal.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<String> home() {
        try {
            return userService.home();
        } catch (Exception e) {
            logger.error("Error in home endpoint: {}", e.getMessage());
            throw new UserServiceException("Internal Server Error");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        try {
            logger.info("Register request received for email: {}", userDto.getEmail());
            return userService.register(userDto);
        } catch (Exception e) {
            logger.error("Error in register endpoint: {}", e.getMessage());
            throw new UserServiceException("Registration failed");
        }
    }

    @PostMapping("/remove/{email}")
    public ResponseEntity<String> removeUser(@PathVariable String email) {
        try {
            logger.info("Remove request received for email: {}", email);
            return userService.removeUser(email);
        } catch (Exception e) {
            logger.error("Error in removeUser endpoint: {}", e.getMessage());
            throw new UserServiceException("User removal failed");
        }
    }
}