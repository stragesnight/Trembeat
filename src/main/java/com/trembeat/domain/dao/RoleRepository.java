package com.trembeat.domain.dao;

import com.trembeat.domain.models.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * Role access repository
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

}
