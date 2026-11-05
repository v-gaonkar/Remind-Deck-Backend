package com.dev.remindeck.service;

import com.dev.remindeck.entity.User;
import com.dev.remindeck.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {
    @Autowired
    UserRepository userRepository;

    public User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new RuntimeException("No authenticated user found");
        }
        String email = (String) auth.getPrincipal();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Authenticated user not found in database");
        }
        return user;
    }
}
