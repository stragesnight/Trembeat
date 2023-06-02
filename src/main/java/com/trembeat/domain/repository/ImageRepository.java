package com.trembeat.domain.repository;

import com.trembeat.domain.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Image access repository
 */
public interface ImageRepository extends JpaRepository<Image, Long> {

}
