package com.trembeat.domain.repository;

import com.trembeat.domain.models.Sound;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

/**
 * Sound access repository
 */
public interface SoundRepository extends CrudRepository<Sound, Long> {
    /**
     * Find all sounds that match given title
     * @param title Title of sound to be found
     * @return Set of found sounds
     */
    @Query("SELECT s FROM Sound s WHERE s.title LIKE %:title%")
    Set<Sound> findAllByTitle(String title);
}