package com.trembeat.domain.repository;

import com.trembeat.domain.models.Sound;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

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
    Iterable<Sound> findAllByTitle(String title);

    /**
     * Find all sounds uploaded by user with given id
     * @param authorId Author's id
     * @return Set of found sounds
     */
    @Query("SELECT s FROM Sound s WHERE s.author.id = :authorId")
    Iterable<Sound> findAllByAuthorId(Long authorId);
}
