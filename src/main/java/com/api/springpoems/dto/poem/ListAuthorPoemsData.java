package com.api.springpoems.dto.poem;

import java.time.LocalDateTime;

import com.api.springpoems.entities.Poem;

public record ListAuthorPoemsData(
    String title,
    LocalDateTime createdAt,
    LocalDateTime lastUpdate
) {
    public ListAuthorPoemsData(Poem poem) {
        this(
            poem.getTitle(),
            poem.getCreatedAt(), 
            poem.getLastUpdate()
        );
    }
}
