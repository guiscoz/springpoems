package com.api.springpoems.entities;

import java.time.LocalDateTime;

import com.api.springpoems.dto.comment.CreateCommentData;
import com.api.springpoems.dto.comment.SendCommentData;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "comments")
@Entity(name = "Comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Comment {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poem_id")
    private Poem poem;

    private Boolean active;

    public Comment(@Valid CreateCommentData data) {
        this.content = data.content();
        this.author = data.author();
        this.poem = data.poem();
        this.createdAt = LocalDateTime.now();
        this.lastUpdate = LocalDateTime.now();
        this.active = true;
    }

    public void updateComment(@Valid SendCommentData data) {
        if(data.content() != null) {
            this.content = data.content();
        }

        this.lastUpdate = LocalDateTime.now();
    }
}
