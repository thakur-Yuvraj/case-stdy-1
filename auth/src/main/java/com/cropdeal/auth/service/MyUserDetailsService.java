package com.cropdeal.auth.service;

import com.cropdeal.auth.modal.UserCredential;
import com.cropdeal.auth.repository.UserCredentialRepository;
import com.cropdeal.auth.modal.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// custom user details service
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserCredentialRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredential user = userRepository.findByName(username);
        if(user == null) {
            System.out.println("User Not found");
            throw new UsernameNotFoundException("User Not found");
        }

        return new UserPrincipal(user);
    }
}
