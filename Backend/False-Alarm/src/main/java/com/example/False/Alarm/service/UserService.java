package com.example.False.Alarm.service;

import com.example.False.Alarm.dto.AddUserRequest;
import com.example.False.Alarm.enums.UserType;
import com.example.False.Alarm.mapper.UserMapper;
import com.example.False.Alarm.model.User;
import com.example.False.Alarm.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    public User addUser(AddUserRequest addUserRequest) {
        User user = UserMapper.mapToUser(addUserRequest);
        user.setUserType(UserType.USER);
//        user.setAuthorities("USER"); //uncomment when added security

        return userRepository.save(user);
    }

    public User addAdmin(@Valid AddUserRequest addUserRequest){
        User user = UserMapper.mapToUser(addUserRequest);
        user.setUserType(UserType.ADMIN);
//        user.setAuthorities("ADMIN");   //uncomment when added security

        return userRepository.save(user);
    }

}
