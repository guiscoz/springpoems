package com.api.springpoems.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserData(
    @NotBlank(message ="The username cannot be empty")
    String username,
    
    @NotBlank(message ="The email cannot be empty")
    @Email(message ="Insert a valid email")
    String email,
    
    @NotBlank(message ="The password cannot be empty")
    @Size(min = 8, message = "The password must be at least 8 characters long.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%!?]).*$", 
        message = "The password must contain at least one lowercase letter, one uppercase letter, one number and one special character.")
    String password,

    @NotBlank(message ="You need to confirm the password")
    String confirmPassword
) {}
