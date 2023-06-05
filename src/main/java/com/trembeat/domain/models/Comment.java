package com.trembeat.domain.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * Comment entity in database
 */
@Entity
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Table(name = "comments")
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NonNull
    @Column(name = "text", length = 256, nullable = false)
    private String text;

    @Column(name = "creation_date")
    private Date creationDate;

    @Setter
    @NonNull
    @ManyToOne
    @JoinColumn(name = "sound_id")
    private Sound sound;

    @Setter
    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @PrePersist
    private void prePersist() {
        creationDate = new Date();
    }
}
