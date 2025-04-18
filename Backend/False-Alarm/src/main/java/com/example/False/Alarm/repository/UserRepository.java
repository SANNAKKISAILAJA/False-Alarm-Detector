package com.example.False.Alarm.repository;

import com.example.False.Alarm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

}
