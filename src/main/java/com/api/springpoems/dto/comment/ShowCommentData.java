package com.api.springpoems.dto.comment;

import java.time.LocalDateTime;

import com.api.springpoems.entities.Comment;

public record ShowCommentData(
    String content,
    String commentAuthorUsername,
    String commentAuthorFirstName,
    String commentAuthorLastName,
    String poemTitle,
    String poemAuthorUsername,
    String poemAuthorFirstname,
    String poemAuthorLastname,
    LocalDateTime createdAt,
    LocalDateTime lastUpdate
) {
    public ShowCommentData(Comment comment) {
        this(
            comment.getContent(), 
            comment.getAuthor().getUsername(),
            comment.getAuthor().getFirstName(),
            comment.getAuthor().getLastName(),
            comment.getPoem().getTitle(),
            comment.getPoem().getAuthor().getUsername(),
            comment.getPoem().getAuthor().getFirstName(),
            comment.getPoem().getAuthor().getLastName(),
            comment.getCreatedAt(), 
            comment.getLastUpdate()
        );
    }
}
