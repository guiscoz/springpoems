package com.api.springpoems.dto.poem;

import java.time.LocalDate;

import com.api.springpoems.entities.Poem;

public record ShowPoemData (
    String authorUsername,
    String authorFirstName,
    String authorLastName,
    String title,
    String content,
    LocalDate createdAt,
    LocalDate lastUpdate
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
