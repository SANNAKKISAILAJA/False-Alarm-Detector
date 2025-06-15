package com.example.False.Alarm.repository;

import com.example.False.Alarm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    List<User> findByUsernameContainingIgnoreCase(String username);
    List<User> findByUserIdContainingIgnoreCase(String userId);
    User findByUserId(String userId);
    Optional<User> findByUsername(String username); // for extracting from Basic Auth
}
