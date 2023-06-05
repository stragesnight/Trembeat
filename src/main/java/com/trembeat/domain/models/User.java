package com.trembeat.domain.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


/**
 * User entity in database, implements UserDetails
 */
@Entity
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Table(name = "users", indexes = {
        @Index(columnList = "username", unique = true),
        @Index(columnList = "password", unique = true),
        @Index(columnList = "email", unique = true),
        @Index(columnList = "username, password", unique = true)
})
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NonNull
    @Column(name = "username", length = 64, nullable = false, unique = true)
    private String username;

    @Setter
    @NonNull
    @Column(name = "bio", length = 256, nullable = false)
    private String bio;

    @Setter
    @NonNull
    // TODO: ensure that safe BCrypt string length is in fact 60
    @Column(name = "password", length = 60, nullable = false)
    private String password;

    @Setter
    @NonNull
    @Column(name = "email", length = 256, nullable = false, unique = true)
    private String email;

    @Column(name = "registration_date", nullable = false)
    private Date registrationDate;

    @Setter
    @ManyToOne
    @JoinColumn(name = "profile_picture_id")
    private Image profilePicture;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @OneToMany(mappedBy = "author")
    private Set<Sound> sounds;

    @OneToMany(mappedBy = "user")
    private Set<Comment> comments;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @PrePersist
    private void prePersist() {
        registrationDate = new Date();
    }
}
