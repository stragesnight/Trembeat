package com.trembeat.domain.repository;

import com.trembeat.domain.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Genre access repository
 */
public interface GenreRepository extends JpaRepository<Genre, Long> {

}
