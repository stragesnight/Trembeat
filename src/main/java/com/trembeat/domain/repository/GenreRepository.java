package com.trembeat.domain.repository;

import com.trembeat.domain.models.Genre;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Genre access repository
 */
public interface GenreRepository extends CrudRepository<Genre, Long> {
    /**
     * Find genre by name
     * @param name Genre name to search for
     * @return Optionally found genre
     */
    @Query("SELECT g FROM Genre g WHERE g.name = :name")
    Optional<Genre> findByName(String name);
}
