package com.trembeat.domain.repository;

import com.trembeat.domain.models.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Profile picture access repository
 */
public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {

}
