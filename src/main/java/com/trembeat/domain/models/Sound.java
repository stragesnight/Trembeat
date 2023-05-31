package com.trembeat.domain.models;

import jakarta.persistence.*;
import lombok.*;

/**
 * Sound file entity
 */
@Entity
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Table(name = "sounds", indexes = {
        @Index(columnList = "title")
})
public class Sound extends FileEntity {
    @Setter
    @NonNull
    @Column(name = "title", length = 128, nullable = false)
    private String title;

    @Setter
    @NonNull
    @Column(name = "description", length = 512, nullable = false)
    private String description;

    @Setter
    @NonNull
    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @Setter
    @NonNull
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
}
