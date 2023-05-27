package com.trembeat.domain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    Long id;

    @Setter
    @NotNull
    @NotEmpty
    @Column(name = "name", length = 64, nullable = false, unique = true)
    String name;

    @OneToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    Set<Sound> sounds;
}
