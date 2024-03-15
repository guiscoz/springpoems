package com.api.springpoems.dto.comment;

import com.api.springpoems.entities.Poem;
import com.api.springpoems.entities.User;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentData(
    @NotBlank(message = "The content cannot be empty")
    String content,

    @NotBlank(message = "There must be an authenticated user")
    User author,

    @NotBlank(message = "There must be an poem")
    Poem poem
) {}
