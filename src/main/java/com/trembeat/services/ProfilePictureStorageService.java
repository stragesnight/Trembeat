package com.trembeat.services;

import com.trembeat.domain.models.ProfilePicture;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Profile picture storage and retrieval service
 */
@Service
public class ProfilePictureStorageService extends StorageService<ProfilePicture> {
    private static String _basePath;
    private Map<String, String> _contentTypes;


    public ProfilePictureStorageService() {
        _basePath = getClass().getClassLoader().getResource(".").getFile();
        _contentTypes = new HashMap<>();
        _contentTypes.put("image/jpeg", "jpeg");
        _contentTypes.put("image/png", "png");
        _contentTypes.put("image/gif", "gif");
        _contentTypes.put("image/webp", "webp");
    }

    @Override
    protected String getFullPath(ProfilePicture profilePicture) {
        return String.format("%sstatic/uploads/profile-picture/%d.%s", _basePath, profilePicture.getId());
    }

    @Override
    protected String getFileExtension(ProfilePicture profilePicture) {
        if (!isAcceptedContentType(profilePicture.getMimeType()))
            return "";

        return _contentTypes.get(profilePicture.getMimeType());
    }

    @Override
    public boolean isAcceptedContentType(String contentType) {
        return _contentTypes.containsKey(contentType);
    }
}
