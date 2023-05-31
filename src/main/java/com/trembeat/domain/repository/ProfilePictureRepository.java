package com.trembeat.domain.repository;

import com.trembeat.domain.models.ProfilePicture;
import org.springframework.data.repository.CrudRepository;

/**
 * Profile picture access repository
 */
public interface ProfilePictureRepository extends CrudRepository<ProfilePicture, Long> {

}
