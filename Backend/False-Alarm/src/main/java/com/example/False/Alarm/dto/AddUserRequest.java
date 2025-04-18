package com.example.False.Alarm.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddUserRequest {
    @NotBlank(message = "Username shouldnot be blank")
    String username;

    @NotBlank(message = "UserId shouldnot be blank")
    String userId;

    @NotBlank(message = "User email shouldnot be blank")
    String email;

    @NotBlank(message = "User Password shouldnot be blank")
    String password;

    @AssertTrue(message = "You must agree to terms and conditions")
    Boolean isEnabled;
}
