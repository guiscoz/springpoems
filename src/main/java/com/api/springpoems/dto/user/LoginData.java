package com.api.springpoems.dto.user;

import jakarta.validation.constraints.NotBlank;

public record LoginData (
    @NotBlank(message ="The username cannot be empty")
    String username,
    
    @NotBlank(message ="The password cannot be empty")
    String password
) {}
