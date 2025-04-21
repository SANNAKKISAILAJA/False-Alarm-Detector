package com.example.False.Alarm.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddUserRequest {
    @NotBlank(message = "Username shouldnot be blank")
    String username;

    @NotBlank(message = "UserId shouldnot be blank")
    String userId;

    @NotBlank(message = "User email shouldnot be blank")
    @Email(message = "Enter a valid Email")
    String email;

    @NotBlank(message = "User Password shouldnot be blank")
    String password;

    MultipartFile image;

    @AssertTrue(message = "You must agree to terms and conditions")
    Boolean isEnabled;
}
