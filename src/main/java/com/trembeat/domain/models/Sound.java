package com.trembeat.domain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Sound entity in database, holds metadata and not sound itself
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "sounds", indexes = {
        @Index(columnList = "title")
})
public class Sound {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Setter
    @NotNull
    @NotEmpty
    @Column(name = "title", length = 128, nullable = false)
    String title;

    @Setter
    @NotNull
    @Column(name = "description", length = 512, nullable = false)
    String description;

    @Setter
    @NotNull
    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    Genre genre;

    @Setter
    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    User author;
}
