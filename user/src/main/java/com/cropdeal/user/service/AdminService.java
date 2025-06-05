package com.cropdeal.user.service;


import com.cropdeal.user.repository.UserRepository;
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

    public ResponseEntity<String> home() {
        try {
            return ResponseEntity.ok("This is admin service");
        }catch (Exception e) {
            String msg = "Err in AdminService -> home function " + e.getMessage();
            log.error(msg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }


}
