package com.trembeat.domain.repository;

import com.trembeat.domain.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Role access repository
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

}
