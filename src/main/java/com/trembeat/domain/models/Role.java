package com.trembeat.domain.models;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/**
 * Role entity in database, implements GrantedAuthority
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "roles", indexes = {
        @Index(columnList = "name", unique = true)
})
public class Role implements GrantedAuthority {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "name", length = 32, unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> users;


    @Override
    public String getAuthority() {
        return name;
    }
}