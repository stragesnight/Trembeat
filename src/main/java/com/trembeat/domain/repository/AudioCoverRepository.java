package com.trembeat.domain.repository;

import com.trembeat.domain.models.AudioCover;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Audio cover access repository
 */
public interface AudioCoverRepository extends JpaRepository<AudioCover, Long> {
}
