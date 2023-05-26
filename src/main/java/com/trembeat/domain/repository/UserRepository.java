package com.trembeat.domain.repository;

import com.trembeat.domain.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * User access repository
 */
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * Find user by username
     * @param username Username to search for
     * @return Optionally found user
     */
    @Query("SELECT u FROM User u WHERE u.username = :username")
    public Optional<User> findByUsername(String username);

    /**
     * Find user by email
     * @param email Email to search for
     * @return Optionally found user
     */
    @Query("SELECT u FROM User u WHERE u.email = :email")
    public Optional<User> findByEmail(String email);
}
