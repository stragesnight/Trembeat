package com.trembeat.services;

import com.trembeat.domain.models.ProfilePicture;
import org.springframework.content.commons.store.ContentStore;

/**
 * Profile picture storage and retrieval interface
 */
public interface ProfilePictureContentStore extends ContentStore<ProfilePicture, String> {

}
