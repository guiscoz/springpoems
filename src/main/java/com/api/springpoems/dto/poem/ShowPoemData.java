package com.api.springpoems.dto.poem;

import java.time.LocalDateTime;

import com.api.springpoems.entities.Poem;

public record ShowPoemData (
    String authorUsername,
    String authorFirstName,
    String authorLastName,
    String title,
    String content,
    LocalDateTime createdAt,
    LocalDateTime lastUpdate
) {
    public ShowPoemData(Poem poem) {
        this(
            poem.getAuthor().getUsername(),
            poem.getAuthor().getLastName(),
            poem.getAuthor().getLastName(),
            poem.getTitle(),
            poem.getContent(), 
            poem.getCreatedAt(), 
            poem.getLastUpdate()
        );
    }
}
