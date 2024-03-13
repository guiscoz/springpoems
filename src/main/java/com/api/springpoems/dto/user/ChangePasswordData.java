package com.api.springpoems.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordData(
    @NotBlank(message = "The current password cannot be empty")
    String currentPassword,
    
    @NotBlank(message = "The new password cannot be empty")
    @Size(min = 8, message = "The password must be at least 8 characters long.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%!?]).*$", 
        message = "The password must contain at least one lowercase letter, one uppercase letter, one number and one special character.")
    String newPassword,

    @NotBlank(message ="You need to confirm the password")
    String confirmPassword
) {}
