package com.trembeat.domain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Sound entity in database, holds metadata and not sound itself
 */
@Entity
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Table(name = "sounds", indexes = {
        @Index(columnList = "title")
})
public class Sound {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Setter
    @NonNull
    @NotNull
    @NotEmpty
    @Column(name = "title", length = 128, nullable = false)
    String title;

    @Setter
    @NonNull
    @NotNull
    @Column(name = "description", length = 512, nullable = false)
    String description;

    @Setter
    @NonNull
    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    Genre genre;

    @Setter
    @NonNull
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    User author;
}
