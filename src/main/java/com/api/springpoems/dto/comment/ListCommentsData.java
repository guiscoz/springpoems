package com.api.springpoems.dto.comment;

import java.time.LocalDateTime;

import com.api.springpoems.entities.Comment;

public record ListCommentsData (
    String content,
    String commentAuthorUsername,
    String commentAuthorFirstName,
    String commentAuthorLastName,
    String poemTitle,
    LocalDateTime createdAt,
    LocalDateTime lastUpdate
) {
    public ListCommentsData(Comment comment) {
        this(
            comment.getContent(), 
            comment.getAuthor().getUsername(),
            comment.getAuthor().getFirstName(),
            comment.getAuthor().getLastName(),
            comment.getPoem().getTitle(),
            comment.getCreatedAt(), 
            comment.getLastUpdate()
        );
    }
}
