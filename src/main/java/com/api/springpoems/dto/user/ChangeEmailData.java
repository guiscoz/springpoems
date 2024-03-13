package com.api.springpoems.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangeEmailData(
    @NotBlank(message ="The email cannot be empty")
    @Email(message ="Insert a valid email")
    String email
) {}
