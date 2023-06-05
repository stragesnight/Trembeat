package com.trembeat.domain.repository;

import com.trembeat.domain.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Comment access repository
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
