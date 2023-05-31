package com.trembeat.services;


import com.trembeat.domain.models.Sound;
import org.springframework.stereotype.Service;

/**
 * Sound data storage and retrieval service
 */
@Service
public class SoundStorageService extends StorageService<Sound> {
    private static String _basePath;


    public SoundStorageService() {
        _basePath = getClass().getClassLoader().getResource(".").getFile();
    }

    @Override
    protected String getFullPath(Sound sound) {
        return String.format("%sstatic/uploads/sound/%d.%s",
                _basePath,
                sound.getId(),
                getFileExtension(sound));
    }

    @Override
    protected String getFileExtension(Sound sound) {
        return switch (sound.getMimeType()) {
            case "audio/mpeg" -> "mp3";
            case "audio/wav" -> "wav";
            case "audio/opus" -> "opus";
            case "audio/ogg" -> "ogg";
            default -> "";
        };
    }
}
