package com.trembeat.domain.repository;

import com.trembeat.domain.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Genre access repository
 */
public interface GenreRepository extends JpaRepository<Genre, Long> {
    /**
     * Find genre by name
     * @param name Genre name to search for
     * @return Optionally found genre
     */
    Optional<Genre> findByNameLikeIgnoreCase(String name);
}
