package com.trembeat.domain.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * Genre entity in database
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "genres", indexes = {
        @Index(columnList = "name", unique = true)
})
public class Genre {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "name", length = 64, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY)
    private Set<Sound> sounds;
}
