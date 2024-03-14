package com.api.springpoems.entities;

import java.time.LocalDate;

import com.api.springpoems.dto.poem.CreatePoemData;
import com.api.springpoems.dto.poem.SendPoemData;

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

@Table(name = "poems")
@Entity(name = "Poem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Poem {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private LocalDate createdAt;
    private LocalDate lastUpdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    private Boolean active;

    public Poem(CreatePoemData data) {
        this.title = data.title();
        this.content = data.content();
        this.createdAt = LocalDate.now();
        this.lastUpdate = LocalDate.now();
        this.author = data.author();
        this.active = true;
    }

    public void updatePoem(@Valid SendPoemData data) {
        if(data.title() != null) {
            this.title = data.title();
        }

        if(data.content() != null) {
            this.content = data.content();
        }

        this.lastUpdate = LocalDate.now();
    }
}
