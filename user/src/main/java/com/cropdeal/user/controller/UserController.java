package com.cropdeal.user.controller;

import com.cropdeal.user.dto.UserDto;
import com.cropdeal.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("")
    public ResponseEntity<String> home() {
        return userService.home();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        return userService.register(userDto);
    }

    @PostMapping("/remove/{email}")
    public ResponseEntity<String> removeUser(@PathVariable String email) {
        return userService.removeUser(email);
    }

}
