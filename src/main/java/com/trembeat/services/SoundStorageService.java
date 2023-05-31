package com.trembeat.services;


import org.springframework.stereotype.Service;

/**
 * Sound data storage and retrieval service
 */
@Service
public class SoundStorageService extends StorageService<Long> {
    private static String _basePath;


    public SoundStorageService() {
        _basePath = getClass().getClassLoader().getResource(".").getFile();
    }

    @Override
    public String getFullPath(Long id) {
        return String.format("%sstatic/uploads/sound/%d.mp3", _basePath, id);
    }
}
