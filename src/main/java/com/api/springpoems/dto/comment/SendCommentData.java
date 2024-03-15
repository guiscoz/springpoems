package com.api.springpoems.dto.comment;
import jakarta.validation.constraints.NotBlank;

public record SendCommentData(
    @NotBlank(message = "The content cannot be empty")
    String content
) {}
