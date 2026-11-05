package com.dev.remindeck.service;

import com.dev.remindeck.dto.request.RegisterRequest;
import com.dev.remindeck.dto.request.UserProfileUpdateRequest;
import com.dev.remindeck.dto.response.UserProfileResponse;
import com.dev.remindeck.entity.User;
import com.dev.remindeck.exception.EmailAlreadyExistsException;
import com.dev.remindeck.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CurrentUserProvider currentUserProvider;

    public void registerUser(RegisterRequest request) {
        if(userRepo.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        } else {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole("USER");
            userRepo.save(user);
        }
    }

    public UserProfileResponse getProfile() {
        User user = currentUserProvider.getCurrentUser();
        UserProfileResponse response = new UserProfileResponse();
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setTimezone(user.getTimezone());
        return response;
    }

    public UserProfileResponse updateProfile(UserProfileUpdateRequest request) {
        User user = currentUserProvider.getCurrentUser();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setTimezone(request.getTimezone());
        User saved = userRepo.save(user);

        UserProfileResponse response = new UserProfileResponse();
        response.setUsername(saved.getUsername());
        response.setEmail(saved.getEmail());
        response.setTimezone(saved.getTimezone());
        return response;
    }
}
