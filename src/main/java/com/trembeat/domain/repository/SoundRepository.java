package com.trembeat.domain.repository;

import com.trembeat.domain.models.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
     * @param authorId Id of author of sound to find
     * @param pageable Pagination to apply for query
     * @return Set of found sounds
     */
    Page<Sound> findAllByAuthor_Id(Long authorId, Pageable pageable);

    /**
     * Find all sound that match given title and author id
     * @param title Title of sound to be found
     * @param authorId Id of author of sound to find
     * @param pageable Pagination to apply for query
     * @return Set of found sounds
     */
    Page<Sound> findAllByTitleLikeIgnoreCaseAndAuthor_Id(String title, Long authorId, Pageable pageable);

    /**
     * Find first 10 matching sounds by given title
     * @param title Partial title to search for
     * @return Set of found sounds
     */
    List<Sound> findTop10ByTitleLikeIgnoreCase(String title);
}
