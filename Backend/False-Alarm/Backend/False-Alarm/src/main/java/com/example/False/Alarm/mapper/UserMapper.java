package com.example.False.Alarm.mapper;

import com.example.False.Alarm.dto.AddUserRequest;
import com.example.False.Alarm.enums.ObservationStatus;
import com.example.False.Alarm.model.User;
import lombok.experimental.UtilityClass;
import com.example.False.Alarm.enums.UserType;


@UtilityClass
public class UserMapper {
    public User mapToUser(AddUserRequest userRequest) {
    UserType userType = UserType.USER;
    String authorities = "USER";
    if ("admin".equalsIgnoreCase(userRequest.getRole())) {
        userType = UserType.ADMIN;
        authorities = "ADMIN";
    }
    return User.builder()
            .username(userRequest.getUsername())
            .userId(userRequest.getUserId())
            .email(userRequest.getEmail())
            .password(userRequest.getPassword())
            .userType(userType)
            .authorities(authorities)
            .observationStatus(ObservationStatus.NOT_OBSERVED)
            .isEnabled(true)
            .build();
}

}

