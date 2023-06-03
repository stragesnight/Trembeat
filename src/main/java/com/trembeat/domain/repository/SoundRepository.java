package com.trembeat.domain.repository;

import com.trembeat.domain.models.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Sound access repository
 */
public interface SoundRepository extends JpaRepository<Sound, Long> {
    /**
     * Find all sounds that match given title
     * @param title Title of sound to be found
     * @param pageable Pagination to apply for query
     * @return Set of found sounds
     */
    Page<Sound> findAllByTitleLikeIgnoreCase(String title, Pageable pageable);

    /**
     * Find all sounds uploaded by user with given id
     * @param author Author of sound to find
     * @param pageable Pagination to apply for query
     * @return Set of found sounds
     */
    Page<Sound> findAllByAuthor(User author, Pageable pageable);
}
