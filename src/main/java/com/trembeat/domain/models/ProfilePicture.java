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
public class ProfilePicture extends FileEntity {
    @OneToMany(mappedBy = "profilePicture")
    private Set<User> users;
}
