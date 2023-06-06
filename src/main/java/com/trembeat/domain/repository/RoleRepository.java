package com.trembeat.domain.repository;

import com.trembeat.domain.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Role access repository
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Find role by name
     * @param name Name of the role to find
     * @return Optionally found role
     */
    Optional<Role> findByName(String name);
}
