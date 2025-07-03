package com.example.False.Alarm.repository;

import com.example.False.Alarm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.False.Alarm.enums.ObservationStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByUsernameContainingIgnoreCase(String username);
    List<User> findByUserIdContainingIgnoreCase(String userId);
    Optional<User> findByUserId(String userId);
    Optional<User> findByUsername(String username);

    List<User> findByBlockUntilLessThanEqualAndObservationStatus(Date date, ObservationStatus status);
    List<User> findByObservationStatus(ObservationStatus status);
    List<User> findByObservationStatusAndIsEnabledTrue(ObservationStatus status);
    List<User> findByObservationStatusAndIsEnabledFalse(ObservationStatus status);
    List<User> findByObservationStatusNot(ObservationStatus status);

}