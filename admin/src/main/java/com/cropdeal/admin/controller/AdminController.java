package com.cropdeal.admin.controller;

import com.cropdeal.admin.dto.UserDto;
import com.cropdeal.admin.service.AdminService;
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

    @PostMapping("/create")
    public ResponseEntity<String> addFarmer(@RequestBody UserDto userDto) {
        return adminService.createAdmin(userDto);
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
