package com.example.False.Alarm.mapper;

import com.example.False.Alarm.dto.AddUserRequest;
import com.example.False.Alarm.enums.ObservationStatus;
import com.example.False.Alarm.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {
    public User mapToUser(AddUserRequest UserRequest){
        return User.builder()
                .username(UserRequest.getUsername())
                .userId(UserRequest.getUserId())
                .email(UserRequest.getEmail())
//                .password(UserRequest.getPassword())    //add after learning Spring Security
                .observationStatus(ObservationStatus.NOT_OBSERVED)
                .isEnabled(UserRequest.getIsEnabled())
                .build();
    }
}

