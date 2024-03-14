package com.api.springpoems.dto.poem;

import java.time.LocalDate;

import com.api.springpoems.entities.Poem;

public record ListAuthorPoemsData(
    String title,
    LocalDate createdAt,
    LocalDate lastUpdate
) {
    public ListAuthorPoemsData(Poem poem) {
        this(
            poem.getTitle(),
            poem.getCreatedAt(), 
            poem.getLastUpdate()
        );
    }
}
