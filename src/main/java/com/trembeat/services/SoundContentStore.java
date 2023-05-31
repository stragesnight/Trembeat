package com.trembeat.services;

import com.trembeat.domain.models.Sound;
import org.springframework.content.commons.store.ContentStore;


/**
 * Sound content storage and retrieval interface
 */
public interface SoundContentStore extends ContentStore<Sound, String> {

}
