package com.trembeat.services;

import com.trembeat.domain.models.ProfilePicture;
import org.springframework.stereotype.Service;

/**
 * Profile picture storage and retrieval service
 */
@Service
public class ProfilePictureStorageService extends StorageService<ProfilePicture> {
    private static String _basePath;


    public ProfilePictureStorageService() {
        _basePath = getClass().getClassLoader().getResource(".").getFile();
    }

    @Override
    protected String getFullPath(ProfilePicture profilePicture) {
        return String.format("%sstatic/uploads/profile-picture/%d.%s", _basePath, profilePicture.getId());
    }

    @Override
    protected String getFileExtension(ProfilePicture sound) {
        return switch (sound.getMimeType()) {
            case "image/gif" -> "gif";
            case "audio/jpeg" -> "jpeg";
            case "audio/png" -> "png";
            default -> "";
        };
    }
}
