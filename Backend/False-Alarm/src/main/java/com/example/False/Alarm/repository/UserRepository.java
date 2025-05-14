package com.example.False.Alarm.repository;

import com.example.False.Alarm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    List<User> findByUsernameContainingIgnoreCase(String username);
    List<User> findByUserIdContainingIgnoreCase(String userId);
}
