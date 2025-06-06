package com.cropdeal.user.controller;

import com.cropdeal.user.service.AdminService;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("")
    public ResponseEntity<String> home() {
        return adminService.home();
    }

    @DeleteMapping("remove/user/{email}")
    public ResponseEntity<String> removeUser(@PathVariable String email) {
        return adminService.removeUser(email);
    }

    @DeleteMapping("remove/crop/{cropId}")
    public ResponseEntity<String> removeCrop(@PathVariable int cropId) {
        return adminService.removeCrop(cropId);
    }
}
