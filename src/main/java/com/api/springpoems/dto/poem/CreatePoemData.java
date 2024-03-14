package com.api.springpoems.dto.poem;

import com.api.springpoems.entities.User;

import jakarta.validation.constraints.NotBlank;

public record CreatePoemData (
    @NotBlank(message = "The title cannot be empty")
    String title,

    @NotBlank(message = "The content cannot be empty")
    String content,

    @NotBlank(message = "There must be an authenticated user")
    User author
){}
