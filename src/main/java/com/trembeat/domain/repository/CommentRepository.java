package com.trembeat.domain.repository;

import com.trembeat.domain.models.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Comment access repository
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * Find all comment by given sound
     * @param sound Sound to search comments
     * @param pageable Pagination and sorting settings
     * @return Page of found comments
     */
    Page<Comment> findAllBySound(Sound sound, Pageable pageable);
}
