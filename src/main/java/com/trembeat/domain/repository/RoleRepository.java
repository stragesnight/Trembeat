package com.trembeat.domain.repository;

import com.trembeat.domain.models.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * Role access repository
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

}
