package com.cropdeal.admin.service;


import com.cropdeal.admin.dto.UserDto;
import com.cropdeal.admin.feign.CropServiceClient;
import com.cropdeal.admin.feign.UserServiceClient;
import com.cropdeal.admin.modal.Admin;
import com.cropdeal.admin.repository.AdminRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    @Autowired
    CropServiceClient cropServiceClient;
    @Autowired
    UserServiceClient userServiceClient;

    @Autowired
    private AdminRepository adminRepository;

    public ResponseEntity<String> createAdmin(UserDto userDto) {
        try {
            Admin admin = Admin.builder()
                    .email(userDto.getEmail())
                    .fullName(userDto.getFullName())
                    .phone(userDto.getPhone())
                    .adminLevel("SUPER_ADMIN")
                    .canManageUsers(true)
                    .canManageCrops(true)
                    .build();

            adminRepository.save(admin);
            return ResponseEntity.ok("Admin registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Admin creation failed");
        }
    }

    public ResponseEntity<String> home() {
        try {
            return ResponseEntity.ok("This is admin service");
        }catch (Exception e) {
            String msg = "Err in AdminService -> home function " + e.getMessage();
            log.error(msg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }


    @Transactional
    public ResponseEntity<String> removeUser(String email) {
        try {
            return userServiceClient.removeUser(email);
        }catch (Exception e) {
            String err ="Err in Admin Service -> remove user " + e.getMessage();

            log.error(err);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }

    public ResponseEntity<String> removeCrop(int cropId) {
        try {
            return cropServiceClient.removeCropById(cropId);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}