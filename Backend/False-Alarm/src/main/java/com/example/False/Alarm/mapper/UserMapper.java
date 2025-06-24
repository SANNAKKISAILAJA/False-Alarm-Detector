package com.example.False.Alarm.mapper;

import com.example.False.Alarm.dto.AddUserRequest;
import com.example.False.Alarm.enums.ObservationStatus;
import com.example.False.Alarm.model.User;
import lombok.experimental.UtilityClass;
import com.example.False.Alarm.enums.UserType;

@UtilityClass
public class UserMapper {
    public static User mapToUser(AddUserRequest userRequest) {
        UserType userType = UserType.USER;
        String authorities = "USER";
        if ("admin".equalsIgnoreCase(userRequest.getRole())) {
            userType = UserType.ADMIN;
            authorities = "ADMIN";
        }
        User user = new User();
        
        user.setUserId(userRequest.getUserId());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setUserType(userType);
        
        user.setObservationStatus(ObservationStatus.OBSERVED);
        user.setEnabled(true);
        return user;
    }
}