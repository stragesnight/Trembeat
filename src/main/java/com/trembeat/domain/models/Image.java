package com.trembeat.domain.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * Profile picture file entity
 */
@Entity
@NoArgsConstructor
@Table(name = "profile_pictures")
public class Image extends FileEntity {
    @Getter
    @OneToMany(mappedBy = "profilePicture")
    private Set<User> users;

    @Getter
    @OneToMany(mappedBy = "cover")
    private Set<Sound> sounds;
}
