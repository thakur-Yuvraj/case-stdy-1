package com.cropdeal.auth.service;

import com.cropdeal.auth.modal.UserCredential;
import com.cropdeal.auth.modal.UserPrincipal;
import com.cropdeal.auth.repository.UserCredentialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

    @Autowired
    UserCredentialRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user by username: {}", username);

        UserCredential user = userRepository.findByName(username);
        if (user == null) {
            logger.warn("User not found: {}", username);
            throw new UsernameNotFoundException("User not found");
        }

        logger.info("User {} loaded successfully", username);
        return new UserPrincipal(user);
    }
}