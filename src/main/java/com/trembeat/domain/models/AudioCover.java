package com.trembeat.domain.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * Audio cover image entity
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "audio_covers")
public class AudioCover extends FileEntity {
    @OneToMany(mappedBy = "cover")
    private Set<Sound> sounds;
}
