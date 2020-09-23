package com.capg.ow.security.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.capg.ow.security.exception.ResourceNotFoundException;
import com.capg.ow.security.model.details.UserPrincipal;
import com.capg.ow.security.model.entity.User;
import com.capg.ow.security.repository.UserRepository;



@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
    	User user = repository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail)
        );

        return UserPrincipal.create(user);
    }
    
    @Transactional
    public UserDetails loadUserById(int id) {
        User user = repository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }
}
