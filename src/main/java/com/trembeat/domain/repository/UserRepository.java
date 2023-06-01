package com.trembeat.domain.repository;

import com.trembeat.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User access repository
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find user by username
     * @param username Username to search for
     * @return Optionally found user
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email
     * @param email Email to search for
     * @return Optionally found user
     */
    Optional<User> findByEmail(String email);
}
