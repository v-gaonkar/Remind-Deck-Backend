package com.dev.remindeck.repository;

import com.dev.remindeck.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String username);
    boolean existsByEmail(String email);
}
