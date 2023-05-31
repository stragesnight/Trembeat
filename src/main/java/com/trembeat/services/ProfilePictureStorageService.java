package com.trembeat.services;

import org.springframework.stereotype.Service;

/**
 * Profile picture storage and retrieval service
 */
@Service
public class ProfilePictureStorageService extends StorageService<Long> {
    private static String _basePath;


    public ProfilePictureStorageService() {
        _basePath = getClass().getClassLoader().getResource(".").getFile();
    }

    @Override
    public String getFullPath(Long id) {
        return String.format("%sstatic/uploads/profile-picture/%d.jpeg", _basePath, id);
    }
}
