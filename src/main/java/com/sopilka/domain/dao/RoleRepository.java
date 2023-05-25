package com.sopilka.domain.dao;

import com.sopilka.domain.models.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * Role access repository
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

}
