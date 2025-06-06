package com.cropdeal.user.service;


import com.cropdeal.user.feign.CropServiceClient;
import com.cropdeal.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CropServiceClient cropServiceClient;

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
            if(!userRepository.existsByEmail(email)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not found");
            }
            userRepository.deleteByEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body("User Deleted Successfully");
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
