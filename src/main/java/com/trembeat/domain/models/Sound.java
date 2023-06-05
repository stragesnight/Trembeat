package com.trembeat.domain.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

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
    @Transient
    public static final Set<String> orderableFields;

    static {
        orderableFields = new HashSet<>();
        orderableFields.add("title");
        orderableFields.add("uploadDate");
        orderableFields.add("lastBumpDate");
    }

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

    @Setter
    @ManyToOne
    @JoinColumn(name = "cover_id")
    private Image cover;

    @Setter
    @Column(name = "last_bump_date", nullable = false)
    protected Date lastBumpDate;

    @OneToMany(mappedBy = "sound")
    private Set<Comment> comments;


    @Override
    protected void prePersist() {
        super.prePersist();
        lastBumpDate = new Date();
    }
}
