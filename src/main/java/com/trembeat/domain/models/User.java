package com.trembeat.domain.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;


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
    Long id;

    @Setter
    @NonNull
    @Column(name = "username", length = 64, nullable = false, unique = true)
    String username;

    @Setter
    @NonNull
    // TODO: ensure that safe BCrypt string length is in fact 60
    @Column(name = "password", length = 60, nullable = false)
    String password;

    @Setter
    @NonNull
    @Column(name = "email", length = 256, nullable = false, unique = true)
    String email;

    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    Set<Role> roles;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    Set<Sound> sounds;


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
}
